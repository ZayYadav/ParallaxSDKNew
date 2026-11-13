#include "AnogsCompat.h"
#include "BoxCore.h"
#include "IO.h"
#include "Log.h"
#include "SandHook/xdl.h"

#include <atomic>
#include <cerrno>
#include <cstdarg>
#include <cstdint>
#include <cstdlib>
#include <cstring>
#include <dlfcn.h>
#include <fcntl.h>
#include <linux/limits.h>
#include <mutex>
#include <pthread.h>
#include <signal.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/syscall.h>
#include <unistd.h>
#include <android/dlext.h>

#if defined(__aarch64__)
#include "Hook/And64InlineHook/And64InlineHook.hpp"
static inline bool installInlineHook(void *sym, void *rep, void **orig) {
    if (!sym || !rep || !orig) return false;
    *orig = nullptr;
    A64HookFunction(sym, rep, orig);
    return *orig != nullptr;
}
static constexpr size_t kMinHookSymSize = 20;
#elif defined(__arm__)
#include "Hook/Substrate/SubstrateHook.h"
static inline bool installInlineHook(void *sym, void *rep, void **orig) {
    if (!sym || !rep || !orig) return false;
    *orig = nullptr;
    MSHookFunction(sym, rep, orig);
    return *orig != nullptr;
}
static constexpr size_t kMinHookSymSize = 12;
#else
static inline bool installInlineHook(void *, void *, void **) { return false; }
static constexpr size_t kMinHookSymSize = 16;
#endif

namespace {

static constexpr int kMaxTrackedRanges = 256;
static constexpr int kFaultRingSlots = 16;

struct TrackedRange {
    uintptr_t start;
    uintptr_t end;
};

struct FaultSlot {
    std::atomic<long> tid;
    std::atomic<uintptr_t> pc;
    std::atomic<uintptr_t> page;
    std::atomic<int> count;
};

// Double-buffered lock-free range cache so SIGSEGV handler never uses fopen()/malloc().
TrackedRange g_ranges[2][kMaxTrackedRanges];
int g_range_counts[2] = {0, 0};
std::atomic<int> g_active_bank{0};
std::mutex g_refresh_mutex;

FaultSlot g_fault_ring[kFaultRingSlots];
std::atomic<int> g_fault_next_slot{0};

std::atomic<bool> g_runtime_initialized{false};
std::atomic<bool> g_native_io_enabled{false};
std::mutex g_sigaction_mutex;

struct sigaction g_chained_sigsegv{};
struct sigaction g_chained_sigbus{};
std::atomic<bool> g_has_chained_sigsegv{false};
std::atomic<bool> g_has_chained_sigbus{false};

typedef int (*sigaction_fn_t)(int, const struct sigaction *, struct sigaction *);
sigaction_fn_t orig_sigaction = nullptr;

typedef void *(*loader_dlopen_fn_t)(const char *, int, const void *);
typedef void *(*loader_android_dlopen_ext_fn_t)(const char *, int, const android_dlextinfo *, const void *);
typedef void *(*dlopen_fn_t)(const char *, int);
typedef void *(*android_dlopen_ext_fn_t)(const char *, int, const android_dlextinfo *);

loader_dlopen_fn_t orig_loader_dlopen = nullptr;
loader_android_dlopen_ext_fn_t orig_loader_android_dlopen_ext = nullptr;
loader_dlopen_fn_t g_real_loader_dlopen = nullptr;
loader_android_dlopen_ext_fn_t g_real_loader_android_dlopen_ext = nullptr;
dlopen_fn_t orig_dlopen = nullptr;
android_dlopen_ext_fn_t orig_android_dlopen_ext = nullptr;

typedef int (*openat_fn_t)(int, const char *, int, ...);
typedef int (*open2_fn_t)(const char *, int);
typedef int (*faccessat_fn_t)(int, const char *, int, int);
typedef int (*fstatat_fn_t)(int, const char *, struct stat *, int);
typedef ssize_t (*readlinkat_fn_t)(int, const char *, char *, size_t);

openat_fn_t orig_openat = nullptr;
open2_fn_t orig_open_2 = nullptr;
faccessat_fn_t orig_faccessat = nullptr;
fstatat_fn_t orig_fstatat = nullptr;
readlinkat_fn_t orig_readlinkat = nullptr;

thread_local bool g_in_dlopen_notify = false;

static bool isProtectedGameLibName(const char *pathOrName) {
    if (!pathOrName || pathOrName[0] == '\0') return false;
    return strstr(pathOrName, "libanogs.so") != nullptr
            || strstr(pathOrName, "libUE4.so") != nullptr
            || strstr(pathOrName, "libtersafe.so") != nullptr
            || strstr(pathOrName, "libgcloud.so") != nullptr
            || strstr(pathOrName, "libTDataMaster.so") != nullptr
            || strstr(pathOrName, "libil2cpp.so") != nullptr
            || strstr(pathOrName, "libunity.so") != nullptr;
}

static long getPageSizeSafe() {
    long ps = sysconf(_SC_PAGESIZE);
    return (ps > 0) ? ps : 4096L;
}

static uintptr_t extractPcFromUcontext(const void *ucontextVoid) {
    if (!ucontextVoid) return 0;
    const auto *uc = reinterpret_cast<const ucontext_t *>(ucontextVoid);
#if defined(__aarch64__)
    return static_cast<uintptr_t>(uc->uc_mcontext.pc);
#elif defined(__arm__)
    return static_cast<uintptr_t>(uc->uc_mcontext.arm_pc);
#elif defined(__x86_64__)
    return static_cast<uintptr_t>(uc->uc_mcontext.gregs[REG_RIP]);
#elif defined(__i386__)
    return static_cast<uintptr_t>(uc->uc_mcontext.gregs[REG_EIP]);
#else
    return 0;
#endif
}

static bool isTrackedGameAddrLockFree(uintptr_t faultAddr) {
    if (faultAddr < 0x10000) return false;
    int bank = g_active_bank.load(std::memory_order_acquire);
    int count = g_range_counts[bank];
    if (count > kMaxTrackedRanges) count = kMaxTrackedRanges;
    for (int i = 0; i < count; ++i) {
        if (faultAddr >= g_ranges[bank][i].start && faultAddr < g_ranges[bank][i].end) {
            return true;
        }
    }
    return false;
}

// Async-signal-safe /proc/self/maps fallback (zero stdio, zero malloc) in case
// a protected library fault occurs right before g_ranges is refreshed.
static bool containsSubstring(const char *hay, size_t hayLen, const char *needle) {
    size_t nLen = strlen(needle);
    if (nLen == 0 || hayLen < nLen) return false;
    for (size_t i = 0; i + nLen <= hayLen; ++i) {
        if (memcmp(hay + i, needle, nLen) == 0) return true;
    }
    return false;
}

static bool parseHexPrefix(const char *p, size_t len, uintptr_t *startOut, uintptr_t *endOut, const char **permsOut) {
    uintptr_t s = 0, e = 0;
    size_t i = 0;
    while (i < len && p[i] != '-') {
        char c = p[i++];
        uintptr_t v;
        if (c >= '0' && c <= '9') v = static_cast<uintptr_t>(c - '0');
        else if (c >= 'a' && c <= 'f') v = static_cast<uintptr_t>(c - 'a' + 10);
        else if (c >= 'A' && c <= 'F') v = static_cast<uintptr_t>(c - 'A' + 10);
        else return false;
        s = (s << 4) | v;
    }
    if (i >= len || p[i] != '-') return false;
    ++i;
    while (i < len && p[i] != ' ') {
        char c = p[i++];
        uintptr_t v;
        if (c >= '0' && c <= '9') v = static_cast<uintptr_t>(c - '0');
        else if (c >= 'a' && c <= 'f') v = static_cast<uintptr_t>(c - 'a' + 10);
        else if (c >= 'A' && c <= 'F') v = static_cast<uintptr_t>(c - 'A' + 10);
        else return false;
        e = (e << 4) | v;
    }
    if (i + 5 >= len || p[i] != ' ') return false;
    *startOut = s;
    *endOut = e;
    *permsOut = p + i + 1;
    return true;
}

static bool isTrackedGameAddrRawMapsFallback(uintptr_t faultAddr) {
    if (faultAddr < 0x10000) return false;
    int fd = static_cast<int>(syscall(__NR_openat, AT_FDCWD, "/proc/self/maps", O_RDONLY | O_CLOEXEC, 0));
    if (fd < 0) return false;

    char buf[2048];
    size_t carry = 0;
    bool matched = false;
    while (!matched) {
        ssize_t n = syscall(__NR_read, fd, buf + carry, sizeof(buf) - carry);
        if (n <= 0) break;
        size_t total = carry + static_cast<size_t>(n);
        size_t lineStart = 0;
        for (size_t i = 0; i < total; ++i) {
            if (buf[i] == '\n') {
                size_t lineLen = i - lineStart;
                const char *line = buf + lineStart;
                uintptr_t startAddr = 0, endAddr = 0;
                const char *perms = nullptr;
                if (parseHexPrefix(line, lineLen, &startAddr, &endAddr, &perms)
                        && faultAddr >= startAddr && faultAddr < endAddr) {
                    if (!(perms[0] == '-' && perms[1] == '-' && perms[2] == '-')) {
                        if (containsSubstring(line, lineLen, "libanogs.so")
                                || containsSubstring(line, lineLen, "libUE4.so")
                                || containsSubstring(line, lineLen, "libtersafe.so")
                                || containsSubstring(line, lineLen, "libgcloud.so")
                                || containsSubstring(line, lineLen, "libTDataMaster.so")
                                || containsSubstring(line, lineLen, "libil2cpp.so")
                                || containsSubstring(line, lineLen, "libunity.so")) {
                            matched = true;
                        }
                    }
                    break;
                }
                lineStart = i + 1;
            }
        }
        if (matched) break;
        carry = total - lineStart;
        if (carry >= sizeof(buf)) {
            carry = 0;
        } else if (carry > 0 && lineStart > 0) {
            memmove(buf, buf + lineStart, carry);
        }
    }
    syscall(__NR_close, fd);
    return matched;
}

static bool recordAndCheckFaultRetryAllowed(uintptr_t pc, uintptr_t pageBase) {
    long tid = static_cast<long>(syscall(__NR_gettid));
    FaultSlot *slot = nullptr;
    for (int i = 0; i < kFaultRingSlots; ++i) {
        if (g_fault_ring[i].tid.load(std::memory_order_relaxed) == tid) {
            slot = &g_fault_ring[i];
            break;
        }
    }
    if (!slot) {
        int idx = g_fault_next_slot.fetch_add(1, std::memory_order_relaxed) % kFaultRingSlots;
        if (idx < 0) idx = -idx;
        slot = &g_fault_ring[idx];
        slot->tid.store(tid, std::memory_order_relaxed);
        slot->pc.store(0, std::memory_order_relaxed);
        slot->page.store(0, std::memory_order_relaxed);
        slot->count.store(0, std::memory_order_relaxed);
    }

    uintptr_t prevPc = slot->pc.load(std::memory_order_relaxed);
    uintptr_t prevPage = slot->page.load(std::memory_order_relaxed);
    if (prevPc == pc && prevPage == pageBase) {
        int c = slot->count.fetch_add(1, std::memory_order_relaxed) + 1;
        return c <= 3;
    }
    slot->pc.store(pc, std::memory_order_relaxed);
    slot->page.store(pageBase, std::memory_order_relaxed);
    slot->count.store(1, std::memory_order_relaxed);
    return true;
}

static void invokeChainedSignal(int sig, siginfo_t *info, void *ucontext) {
    bool hasChained = (sig == SIGSEGV)
            ? g_has_chained_sigsegv.load(std::memory_order_acquire)
            : g_has_chained_sigbus.load(std::memory_order_acquire);
    const struct sigaction &prev = (sig == SIGSEGV) ? g_chained_sigsegv : g_chained_sigbus;

    if (hasChained) {
        if ((prev.sa_flags & SA_SIGINFO) != 0 && prev.sa_sigaction != nullptr) {
            prev.sa_sigaction(sig, info, ucontext);
            return;
        }
        if (prev.sa_handler != nullptr
                && prev.sa_handler != SIG_DFL
                && prev.sa_handler != SIG_IGN
                && prev.sa_handler != SIG_ERR) {
            prev.sa_handler(sig);
            return;
        }
    }

    struct sigaction dfl{};
    dfl.sa_handler = SIG_DFL;
    sigemptyset(&dfl.sa_mask);
    if (orig_sigaction) {
        orig_sigaction(sig, &dfl, nullptr);
    } else {
        sigaction(sig, &dfl, nullptr);
    }
    raise(sig);
}

static void anogsMemoryFaultHandler(int sig, siginfo_t *info, void *ucontext) {
    if (sig == SIGSEGV && info != nullptr && info->si_code == SEGV_ACCERR) {
        uintptr_t faultAddr = reinterpret_cast<uintptr_t>(info->si_addr);
        long pageSize = getPageSizeSafe();
        uintptr_t pageBase = faultAddr & ~static_cast<uintptr_t>(pageSize - 1);

        if (pageBase != 0
                && (isTrackedGameAddrLockFree(faultAddr) || isTrackedGameAddrRawMapsFallback(faultAddr))) {
            uintptr_t pc = extractPcFromUcontext(ucontext);
            if (recordAndCheckFaultRetryAllowed(pc, pageBase)) {
                long rc = syscall(__NR_mprotect, reinterpret_cast<void *>(pageBase),
                                  static_cast<size_t>(pageSize),
                                  PROT_READ | PROT_WRITE | PROT_EXEC);
                if (rc == 0) {
                    __builtin___clear_cache(reinterpret_cast<char *>(pageBase),
                                            reinterpret_cast<char *>(pageBase + pageSize));
                    return;
                }
            }
        }
    }
    invokeChainedSignal(sig, info, ucontext);
}

static int my_sigaction(int signum, const struct sigaction *act, struct sigaction *oldact) {
    if (!orig_sigaction) {
        return -1;
    }
    if (signum == SIGSEGV || signum == SIGBUS) {
        std::lock_guard<std::mutex> lock(g_sigaction_mutex);
        std::atomic<bool> &hasFlag = (signum == SIGSEGV) ? g_has_chained_sigsegv : g_has_chained_sigbus;
        struct sigaction &chained = (signum == SIGSEGV) ? g_chained_sigsegv : g_chained_sigbus;

        if (oldact != nullptr) {
            if (hasFlag.load(std::memory_order_acquire)) {
                *oldact = chained;
            } else {
                orig_sigaction(signum, nullptr, oldact);
            }
        }
        if (act != nullptr) {
            bool isOurs = ((act->sa_flags & SA_SIGINFO) != 0 && act->sa_sigaction == anogsMemoryFaultHandler);
            if (!isOurs) {
                chained = *act;
                hasFlag.store(true, std::memory_order_release);

                struct sigaction guard{};
                guard.sa_sigaction = anogsMemoryFaultHandler;
                sigemptyset(&guard.sa_mask);
                guard.sa_flags = SA_SIGINFO | SA_ONSTACK | SA_NODEFER;
                return orig_sigaction(signum, &guard, nullptr);
            }
        }
        return 0;
    }
    return orig_sigaction(signum, act, oldact);
}

static void refreshAndUnlockSegmentsInternal(const char *targetFilter) {
    std::lock_guard<std::mutex> lock(g_refresh_mutex);
    FILE *fp = fopen("/proc/self/maps", "rt");
    if (!fp) return;

    const char *baseFilter = nullptr;
    if (targetFilter && targetFilter[0] != '\0') {
        const char *slash = strrchr(targetFilter, '/');
        baseFilter = slash ? (slash + 1) : targetFilter;
    }

    long pageSize = getPageSizeSafe();
    int curBank = g_active_bank.load(std::memory_order_relaxed);
    int nextBank = curBank ^ 1;
    int nextCount = 0;

    char line[512];
    while (fgets(line, sizeof(line), fp)) {
        if (!isProtectedGameLibName(line)) continue;

        unsigned long long startAddr = 0, endAddr = 0;
        char perms[8] = {0};
        if (sscanf(line, "%llx-%llx %7s", &startAddr, &endAddr, perms) == 3) {
            if (strstr(perms, "---") != nullptr) continue;

            if (nextCount < kMaxTrackedRanges) {
                g_ranges[nextBank][nextCount].start = static_cast<uintptr_t>(startAddr);
                g_ranges[nextBank][nextCount].end = static_cast<uintptr_t>(endAddr);
                nextCount++;
            }

            // Unlock Execute-Only Memory (--xp -> r-xp, preserving 'w' if present)
            bool matchesFilter = (baseFilter == nullptr) || (strstr(line, baseFilter) != nullptr);
            if (matchesFilter && strchr(perms, 'x') != nullptr && strchr(perms, 'r') == nullptr) {
                uintptr_t alignedStart = static_cast<uintptr_t>(startAddr) & ~static_cast<uintptr_t>(pageSize - 1);
                uintptr_t alignedEnd = (static_cast<uintptr_t>(endAddr) + pageSize - 1) & ~static_cast<uintptr_t>(pageSize - 1);
                int newProt = PROT_READ | PROT_EXEC;
                if (strchr(perms, 'w') != nullptr) newProt |= PROT_WRITE;
                syscall(__NR_mprotect, reinterpret_cast<void *>(alignedStart), alignedEnd - alignedStart, newProt);
            }
        }
    }
    fclose(fp);

    g_range_counts[nextBank] = nextCount;
    g_active_bank.store(nextBank, std::memory_order_release);
}

typedef void (*FeaturesticBoxLibLoadedCallback_t)(const char *);

static void notifyGameLibraryLoadedNative(const char *libPathOrName) {
    if (!libPathOrName || libPathOrName[0] == '\0') return;
    if (!isProtectedGameLibName(libPathOrName)) return;

    refreshAndUnlockSegmentsInternal(libPathOrName);

    if (g_in_dlopen_notify) return;
    g_in_dlopen_notify = true;

    void *cbSym = dlsym(RTLD_DEFAULT, "FeaturesticBox_OnLibraryLoaded");
    if (cbSym) {
        auto cb = reinterpret_cast<FeaturesticBoxLibLoadedCallback_t>(cbSym);
        cb(libPathOrName);
    }

    JavaVM *vm = BoxCore::getJavaVM();
    if (vm != nullptr) {
        JNIEnv *env = nullptr;
        bool attached = false;
        jint getEnvRc = vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
        if (getEnvRc == JNI_EDETACHED) {
            if (vm->AttachCurrentThread(&env, nullptr) == JNI_OK) {
                attached = true;
            } else {
                env = nullptr;
            }
        }
        if (env != nullptr) {
            jclass cachedCls = BoxCore::getNativeCoreClass();
            jclass localCls = cachedCls ? nullptr : env->FindClass(VMCORE_CLASS);
            jclass rnativeCls = cachedCls ? cachedCls : localCls;
            if (rnativeCls) {
                jmethodID mid = env->GetStaticMethodID(rnativeCls, "onGameLibraryLoaded", "(Ljava/lang/String;)V");
                if (mid) {
                    jstring jPath = env->NewStringUTF(libPathOrName);
                    if (jPath) {
                        env->CallStaticVoidMethod(rnativeCls, mid, jPath);
                        env->DeleteLocalRef(jPath);
                    }
                }
            }
            if (localCls) {
                env->DeleteLocalRef(localCls);
            }
            if (env->ExceptionCheck()) {
                env->ExceptionClear();
            }
            if (attached) {
                vm->DetachCurrentThread();
            }
        }
    }

    g_in_dlopen_notify = false;
}

static void *my_loader_dlopen(const char *filename, int flags, const void *callerAddr) {
    char buf[PATH_MAX];
    const char *target = (filename && filename[0] == '/')
            ? IO::redirectPathBuf(filename, buf, sizeof(buf))
            : filename;
    void *handle = orig_loader_dlopen(target, flags, callerAddr);
    if (handle != nullptr && target != nullptr) {
        notifyGameLibraryLoadedNative(target);
    }
    return handle;
}

static void *my_loader_android_dlopen_ext(
        const char *filename, int flags, const android_dlextinfo *extinfo, const void *callerAddr) {
    char buf[PATH_MAX];
    const char *target = (filename && filename[0] == '/')
            ? IO::redirectPathBuf(filename, buf, sizeof(buf))
            : filename;
    void *handle = orig_loader_android_dlopen_ext(target, flags, extinfo, callerAddr);
    if (handle != nullptr && target != nullptr) {
        notifyGameLibraryLoadedNative(target);
    }
    return handle;
}

__attribute__((noinline))
static void *my_dlopen(const char *filename, int flags) {
    const void *callerAddr = __builtin_return_address(0);
    char buf[PATH_MAX];
    const char *target = (filename && filename[0] == '/')
            ? IO::redirectPathBuf(filename, buf, sizeof(buf))
            : filename;
    void *handle = g_real_loader_dlopen
            ? g_real_loader_dlopen(target, flags, callerAddr)
            : (orig_dlopen ? orig_dlopen(target, flags) : nullptr);
    if (handle != nullptr && target != nullptr) {
        notifyGameLibraryLoadedNative(target);
    }
    return handle;
}

__attribute__((noinline))
static void *my_android_dlopen_ext(const char *filename, int flags, const android_dlextinfo *extinfo) {
    const void *callerAddr = __builtin_return_address(0);
    char buf[PATH_MAX];
    const char *target = (filename && filename[0] == '/')
            ? IO::redirectPathBuf(filename, buf, sizeof(buf))
            : filename;
    void *handle = g_real_loader_android_dlopen_ext
            ? g_real_loader_android_dlopen_ext(target, flags, extinfo, callerAddr)
            : (orig_android_dlopen_ext ? orig_android_dlopen_ext(target, flags, extinfo) : nullptr);
    if (handle != nullptr && target != nullptr) {
        notifyGameLibraryLoadedNative(target);
    }
    return handle;
}

static int my_openat(int dirfd, const char *pathname, int flags, ...) {
    mode_t mode = 0;
    bool hasMode = false;
#ifdef O_TMPFILE
    if ((flags & O_CREAT) != 0 || (flags & O_TMPFILE) == O_TMPFILE) {
        hasMode = true;
    }
#else
    if ((flags & O_CREAT) != 0) {
        hasMode = true;
    }
#endif
    if (hasMode) {
        va_list ap;
        va_start(ap, flags);
        mode = static_cast<mode_t>(va_arg(ap, int));
        va_end(ap);
    }

    char buf[PATH_MAX];
    const char *target = IO::redirectPathBuf(pathname, buf, sizeof(buf));
    if (hasMode) {
        return orig_openat(dirfd, target, flags, mode);
    }
    return orig_openat(dirfd, target, flags);
}

static int my_open_2(const char *pathname, int flags) {
    char buf[PATH_MAX];
    const char *target = IO::redirectPathBuf(pathname, buf, sizeof(buf));
    return orig_open_2(target, flags);
}

static int my_faccessat(int dirfd, const char *pathname, int mode, int flags) {
    char buf[PATH_MAX];
    const char *target = IO::redirectPathBuf(pathname, buf, sizeof(buf));
    return orig_faccessat(dirfd, target, mode, flags);
}

static int my_fstatat(int dirfd, const char *pathname, struct stat *statbuf, int flags) {
    char buf[PATH_MAX];
    const char *target = IO::redirectPathBuf(pathname, buf, sizeof(buf));
    return orig_fstatat(dirfd, target, statbuf, flags);
}

static ssize_t my_readlinkat(int dirfd, const char *pathname, char *bufOut, size_t bufsiz) {
    char buf[PATH_MAX];
    const char *target = IO::redirectPathBuf(pathname, buf, sizeof(buf));
    return orig_readlinkat(dirfd, target, bufOut, bufsiz);
}

static bool tryHookSymbol(void *handle, const char *symName, void *replacement, void **origOut) {
    if (!handle || !symName) return false;
    size_t symSize = 0;
    void *sym = xdl_sym(handle, symName, &symSize);
    if (!sym) {
        sym = xdl_dsym(handle, symName, &symSize);
    }
    if (!sym) return false;
    if (symSize != 0 && symSize < kMinHookSymSize) {
        return false;
    }
    return installInlineHook(sym, replacement, origOut);
}

static void installSignalGuardAndHook() {
    void *libc = xdl_open("libc.so", XDL_DEFAULT);
    if (libc) {
        tryHookSymbol(libc, "sigaction", reinterpret_cast<void *>(my_sigaction),
                      reinterpret_cast<void **>(&orig_sigaction));
        xdl_close(libc);
    }

    struct sigaction sa{};
    sa.sa_sigaction = anogsMemoryFaultHandler;
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_SIGINFO | SA_ONSTACK | SA_NODEFER;

    if (orig_sigaction) {
        struct sigaction prevSegv{};
        if (orig_sigaction(SIGSEGV, &sa, &prevSegv) == 0 && prevSegv.sa_sigaction != anogsMemoryFaultHandler) {
            g_chained_sigsegv = prevSegv;
            g_has_chained_sigsegv.store(true, std::memory_order_release);
        }
        struct sigaction prevBus{};
        if (orig_sigaction(SIGBUS, &sa, &prevBus) == 0 && prevBus.sa_sigaction != anogsMemoryFaultHandler) {
            g_chained_sigbus = prevBus;
            g_has_chained_sigbus.store(true, std::memory_order_release);
        }
    } else {
        struct sigaction prevSegv{};
        if (sigaction(SIGSEGV, &sa, &prevSegv) == 0 && prevSegv.sa_sigaction != anogsMemoryFaultHandler) {
            g_chained_sigsegv = prevSegv;
            g_has_chained_sigsegv.store(true, std::memory_order_release);
        }
        struct sigaction prevBus{};
        if (sigaction(SIGBUS, &sa, &prevBus) == 0 && prevBus.sa_sigaction != anogsMemoryFaultHandler) {
            g_chained_sigbus = prevBus;
            g_has_chained_sigbus.store(true, std::memory_order_release);
        }
    }
}

static void *resolveSymbolAny(void *handle, const char *nameA, const char *nameB) {
    void *sym = nullptr;
    if (handle) {
        if (nameA) {
            sym = xdl_sym(handle, nameA, nullptr);
            if (!sym) sym = xdl_dsym(handle, nameA, nullptr);
        }
        if (!sym && nameB) {
            sym = xdl_sym(handle, nameB, nullptr);
            if (!sym) sym = xdl_dsym(handle, nameB, nullptr);
        }
    }
    if (!sym && nameB) {
        sym = dlsym(RTLD_DEFAULT, nameB);
    }
    return sym;
}

static void installNativeDlopenHooks() {
#if defined(__LP64__)
    const char *linkerName = "linker64";
#else
    const char *linkerName = "linker";
#endif
    void *linkerHandle = xdl_open(linkerName, XDL_DEFAULT);
    bool hookedLoaderDlopen = false;
    bool hookedLoaderExt = false;

    g_real_loader_dlopen = reinterpret_cast<loader_dlopen_fn_t>(
            resolveSymbolAny(linkerHandle, "__dl___loader_dlopen", "__loader_dlopen"));
    g_real_loader_android_dlopen_ext = reinterpret_cast<loader_android_dlopen_ext_fn_t>(
            resolveSymbolAny(linkerHandle, "__dl___loader_android_dlopen_ext", "__loader_android_dlopen_ext"));

    if (linkerHandle) {
        hookedLoaderDlopen = tryHookSymbol(
                linkerHandle, "__dl___loader_dlopen",
                reinterpret_cast<void *>(my_loader_dlopen),
                reinterpret_cast<void **>(&orig_loader_dlopen));
        if (!hookedLoaderDlopen) {
            hookedLoaderDlopen = tryHookSymbol(
                    linkerHandle, "__loader_dlopen",
                    reinterpret_cast<void *>(my_loader_dlopen),
                    reinterpret_cast<void **>(&orig_loader_dlopen));
        }

        hookedLoaderExt = tryHookSymbol(
                linkerHandle, "__dl___loader_android_dlopen_ext",
                reinterpret_cast<void *>(my_loader_android_dlopen_ext),
                reinterpret_cast<void **>(&orig_loader_android_dlopen_ext));
        if (!hookedLoaderExt) {
            hookedLoaderExt = tryHookSymbol(
                    linkerHandle, "__loader_android_dlopen_ext",
                    reinterpret_cast<void *>(my_loader_android_dlopen_ext),
                    reinterpret_cast<void **>(&orig_loader_android_dlopen_ext));
        }
        xdl_close(linkerHandle);
    }

    if (!hookedLoaderDlopen || !hookedLoaderExt) {
        void *libdl = xdl_open("libdl.so", XDL_DEFAULT);
        if (libdl) {
            if (!hookedLoaderDlopen && g_real_loader_dlopen != nullptr) {
                tryHookSymbol(libdl, "dlopen",
                              reinterpret_cast<void *>(my_dlopen),
                              reinterpret_cast<void **>(&orig_dlopen));
            }
            if (!hookedLoaderExt && g_real_loader_android_dlopen_ext != nullptr) {
                tryHookSymbol(libdl, "android_dlopen_ext",
                              reinterpret_cast<void *>(my_android_dlopen_ext),
                              reinterpret_cast<void **>(&orig_android_dlopen_ext));
            }
            xdl_close(libdl);
        }
    }
}

} // namespace

void AnogsCompat::initRuntime(JNIEnv * /*env*/, int /*apiLevel*/) {
    bool expected = false;
    if (!g_runtime_initialized.compare_exchange_strong(expected, true)) {
        return;
    }
    installSignalGuardAndHook();
    installNativeDlopenHooks();
    refreshAndUnlockSegmentsInternal(nullptr);
}

void AnogsCompat::enableNativeIO() {
    bool expected = false;
    if (!g_native_io_enabled.compare_exchange_strong(expected, true)) {
        return;
    }
    void *libc = xdl_open("libc.so", XDL_DEFAULT);
    if (!libc) return;

    tryHookSymbol(libc, "openat", reinterpret_cast<void *>(my_openat),
                  reinterpret_cast<void **>(&orig_openat));
    tryHookSymbol(libc, "__open_2", reinterpret_cast<void *>(my_open_2),
                  reinterpret_cast<void **>(&orig_open_2));
    tryHookSymbol(libc, "faccessat", reinterpret_cast<void *>(my_faccessat),
                  reinterpret_cast<void **>(&orig_faccessat));
    tryHookSymbol(libc, "fstatat", reinterpret_cast<void *>(my_fstatat),
                  reinterpret_cast<void **>(&orig_fstatat));
    tryHookSymbol(libc, "readlinkat", reinterpret_cast<void *>(my_readlinkat),
                  reinterpret_cast<void **>(&orig_readlinkat));

    xdl_close(libc);
}

void AnogsCompat::prepareLibrarySegments(const char *libPathOrName) {
    refreshAndUnlockSegmentsInternal(libPathOrName);
}

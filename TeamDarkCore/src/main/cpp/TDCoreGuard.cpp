#include "TDCoreGuard.h"

#include <atomic>
#include <cstdint>
#include <cstring>
#include <ctime>
#include <string>

#include <dlfcn.h>
#include <fcntl.h>
#include <unistd.h>

namespace {

/* Compile-time string encryption. The plaintext never reaches the binary. */
namespace obf {
template <std::size_t N, std::uint8_t K>
struct Literal {
    char data[N];
    constexpr explicit Literal(const char (&text)[N]) : data{} {
        for (std::size_t i = 0; i < N; ++i) {
            data[i] = static_cast<char>(
                static_cast<std::uint8_t>(text[i]) ^
                static_cast<std::uint8_t>(K + i * 31u));
        }
    }
    std::string decode() const {
        std::string out;
        if (N <= 1) return out;
        out.resize(N - 1);
        for (std::size_t i = 0; i + 1 < N; ++i) {
            out[i] = static_cast<char>(
                static_cast<std::uint8_t>(data[i]) ^
                static_cast<std::uint8_t>(K + i * 31u));
        }
        return out;
    }
};
} // namespace obf

#define TD_OBF(text) \
    ([]() { \
        static constexpr ::obf::Literal<sizeof(text), \
            static_cast<std::uint8_t>((__COUNTER__ * 73 + 19) & 0xFF)> \
            value(text); \
        return value.decode(); \
    }())

constexpr std::uint64_t kCanary = 0x5444434F52454731ULL; // "TDCOREG1"
volatile std::uint64_t gCanary = kCanary;

std::atomic<bool> gCompromised{false};
std::atomic<long long> gLastCheckMs{0};

long long monotonicMs() {
    timespec ts{};
    if (clock_gettime(CLOCK_MONOTONIC, &ts) != 0) return 0;
    return static_cast<long long>(ts.tv_sec) * 1000LL +
           static_cast<long long>(ts.tv_nsec / 1000000LL);
}

bool readLimited(const std::string& path, std::string& out, std::size_t limit) {
    out.clear();
    int fd = ::open(path.c_str(), O_RDONLY | O_CLOEXEC);
    if (fd < 0) return false;
    char buffer[8192];
    while (out.size() < limit) {
        ssize_t n = ::read(fd, buffer, sizeof(buffer));
        if (n < 0) { ::close(fd); return false; }
        if (n == 0) break;
        out.append(buffer, static_cast<std::size_t>(n));
    }
    ::close(fd);
    return true;
}

bool canaryIntact() {
    return gCanary == kCanary;
}

/* TracerPid is non-zero whenever a debugger or ptrace agent is attached. */
bool noDebugger() {
    std::string status;
    if (!readLimited(TD_OBF("/proc/self/status"), status, 1u << 20)) return false;
    const std::string key = TD_OBF("TracerPid:");
    std::size_t pos = status.find(key);
    if (pos == std::string::npos) return false;
    pos += key.size();
    while (pos < status.size() && (status[pos] == ' ' || status[pos] == '\t')) ++pos;
    int pid = 0;
    while (pos < status.size() && status[pos] >= '0' && status[pos] <= '9') {
        pid = pid * 10 + (status[pos] - '0');
        if (pid > 100000000) return false;
        ++pos;
    }
    return pid == 0;
}

/* Known instrumentation agents leave recognisable names in process maps. */
bool noInstrumentation() {
    std::string maps;
    if (!readLimited(TD_OBF("/proc/self/maps"), maps, 4u << 20)) return false;
    for (char& c : maps) {
        if (c >= 'A' && c <= 'Z') c = static_cast<char>(c + 32);
    }
    const std::string needles[] = {
        TD_OBF("frida-agent"), TD_OBF("frida-gadget"), TD_OBF("frida_server"),
        TD_OBF("gum-js-loop"), TD_OBF("linjector"), TD_OBF("libsubstrate"),
        TD_OBF("substrate.so"), TD_OBF("xposed"), TD_OBF("libxposed"),
        TD_OBF("lsposed")
    };
    for (const std::string& needle : needles) {
        if (!needle.empty() && maps.find(needle) != std::string::npos) return false;
    }
    return true;
}

/*
 * Inline-hook detection without false positives: snapshot the first bytes of a
 * few hot libc entry points on first use, then require those bytes to stay
 * identical. Any later trampoline (hook framework or patch) changes them.
 */
constexpr int kTrackedCount = 6;
struct Prologue {
    void* fn;
    unsigned char bytes[8];
    bool valid;
};
Prologue gPrologues[kTrackedCount];
std::atomic<bool> gProloguesReady{false};

void capturePrologues() {
    void* functions[kTrackedCount] = {nullptr};
    const std::string names[kTrackedCount] = {
        TD_OBF("open"), TD_OBF("read"), TD_OBF("connect"),
        TD_OBF("getaddrinfo"), TD_OBF("dlsym"), TD_OBF("memcpy")
    };
    for (int i = 0; i < kTrackedCount; ++i) {
        functions[i] = ::dlsym(RTLD_DEFAULT, names[i].c_str());
        gPrologues[i].fn = functions[i];
        gPrologues[i].valid = functions[i] != nullptr;
        std::memset(gPrologues[i].bytes, 0, sizeof(gPrologues[i].bytes));
        if (gPrologues[i].valid) {
            std::memcpy(gPrologues[i].bytes, functions[i],
                        sizeof(gPrologues[i].bytes));
        }
    }
    gProloguesReady.store(true, std::memory_order_release);
}

bool prologuesUnchanged() {
    for (int i = 0; i < kTrackedCount; ++i) {
        if (!gPrologues[i].valid) continue;
        unsigned char current[8] = {0};
        std::memcpy(current, gPrologues[i].fn, sizeof(current));
        if (std::memcmp(current, gPrologues[i].bytes, sizeof(current)) != 0)
            return false;
    }
    return true;
}

} // namespace

namespace tdcore {
namespace guard {

bool compromised() {
    return gCompromised.load(std::memory_order_acquire);
}

void wipe() {
    gCompromised.store(true, std::memory_order_release);
    gCanary = 0;
}

bool selfCheck() {
    if (compromised()) return false;

    const long long now = monotonicMs();
    const long long previous = gLastCheckMs.load(std::memory_order_relaxed);
    if (previous > 0 && now > previous && now - previous < 1200) return true;
    gLastCheckMs.store(now, std::memory_order_relaxed);

    if (!gProloguesReady.load(std::memory_order_acquire)) capturePrologues();

    const bool healthy = canaryIntact() && noDebugger() && noInstrumentation() &&
                         prologuesUnchanged();
    if (!healthy) wipe();
    return healthy;
}

} // namespace guard
} // namespace tdcore

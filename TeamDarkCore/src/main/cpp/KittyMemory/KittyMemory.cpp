#include "KittyMemory.h"
#include <climits>
#include <cstring>
#include <fcntl.h>
#include <mutex>
#include <sys/uio.h>

using KittyMemory::Memory_Status;
using KittyMemory::ProcMap;

struct mapsCache {
    std::string identifier;
    ProcMap map;
};

static std::vector<mapsCache> __mapsCache;
static std::mutex __mapsCacheMutex;

static ProcMap findMapInCache(const std::string &id) {
    std::lock_guard<std::mutex> lock(__mapsCacheMutex);
    ProcMap ret{};
    for (size_t i = 0; i < __mapsCache.size(); i++) {
        if (__mapsCache[i].identifier == id) {
            ret = __mapsCache[i].map;
            break;
        }
    }
    return ret;
}

/**
 * Inspects /proc/self/maps to check if [addr, addr + len) is mapped (not a ---p guard page)
 * and detects whether any page in that range originally had write ('w') or execute ('x') permission.
 */
static bool inspectAddressMapping(const void *addr, size_t len, bool *outHasWrite, bool *outHasExec) {
    if (outHasWrite) *outHasWrite = false;
    if (outHasExec) *outHasExec = false;
    if (addr == nullptr || len == 0) return false;

    uintptr_t targetStart = reinterpret_cast<uintptr_t>(addr);
    uintptr_t targetEnd = targetStart + len;
    if (targetEnd < targetStart) return false;

    FILE *fp = fopen("/proc/self/maps", "rt");
    if (!fp) return true; // If /proc/self/maps cannot be opened, fall back to mprotect

    char line[512];
    uintptr_t coveredUntil = targetStart;
    while (fgets(line, sizeof(line), fp)) {
        unsigned long long segStart = 0, segEnd = 0;
        char perms[8] = {0};
        if (sscanf(line, "%llx-%llx %7s", &segStart, &segEnd, perms) == 3) {
            if (segEnd <= coveredUntil) continue;
            if (segStart > coveredUntil) {
                break; // Unmapped gap!
            }
            if (strstr(perms, "---") != nullptr) {
                break; // Guard page (---p)!
            }
            if (strchr(perms, 'w') != nullptr && outHasWrite) {
                *outHasWrite = true;
            }
            if (strchr(perms, 'x') != nullptr && outHasExec) {
                *outHasExec = true;
            }
            coveredUntil = static_cast<uintptr_t>(segEnd);
            if (coveredUntil >= targetEnd) {
                fclose(fp);
                return true;
            }
        }
    }
    fclose(fp);
    return coveredUntil >= targetEnd;
}

bool KittyMemory::ProtectAddr(void *addr, size_t length, int protection) {
    if (addr == nullptr || length == 0) return false;
    long pageSize = sysconf(_SC_PAGESIZE);
    if (pageSize <= 0) pageSize = 4096;
    uintptr_t uAddr = reinterpret_cast<uintptr_t>(addr);
    uintptr_t pageStart = uAddr & ~static_cast<uintptr_t>(pageSize - 1);
    uintptr_t pageEnd = (uAddr + length + static_cast<uintptr_t>(pageSize) - 1) & ~static_cast<uintptr_t>(pageSize - 1);
    return (mprotect(reinterpret_cast<void *>(pageStart), pageEnd - pageStart, protection) != -1);
}

Memory_Status KittyMemory::memWrite(void *addr, const void *buffer, size_t len) {
    if (addr == NULL)
        return INV_ADDR;
    if (buffer == NULL)
        return INV_BUF;
    if (len < 1 || len > INT_MAX)
        return INV_LEN;

    bool origWritable = false;
    bool origExecutable = false;
    if (!inspectAddressMapping(addr, len, &origWritable, &origExecutable)) {
        return INV_ADDR;
    }

    if (!ProtectAddr(addr, len, _PROT_RWX_)) {
        if (!ProtectAddr(addr, len, PROT_READ | PROT_WRITE)) {
            return INV_PROT;
        }
    }

    if (memcpy(addr, buffer, len) != NULL) {
        __builtin___clear_cache(reinterpret_cast<char *>(addr), reinterpret_cast<char *>(addr) + len);
        // Critical for libanogs.so & Android 15/16 16KB pages:
        // If the page originally had write permission (.data / .got / .bss or shared 16KB boundary),
        // NEVER strip PROT_WRITE, otherwise libanogs.so will crash with SEGV_ACCERR when writing globals!
        int restoreProt = _PROT_RX_;
        if (origWritable) {
            restoreProt = origExecutable ? _PROT_RWX_ : (PROT_READ | PROT_WRITE);
        } else if (sysconf(_SC_PAGESIZE) > 4096) {
            // On 16KB page devices, keep RWX if a page might straddle code and data
            restoreProt = _PROT_RWX_;
        }
        ProtectAddr(addr, len, restoreProt);
        return SUCCESS;
    }
    return FAILED;
}

Memory_Status KittyMemory::memRead(void *buffer, const void *addr, size_t len) {
    if (addr == NULL)
        return INV_ADDR;
    if (buffer == NULL)
        return INV_BUF;
    if (len < 1 || len > INT_MAX)
        return INV_LEN;

    bool origWritable = false;
    bool origExecutable = false;
    if (!inspectAddressMapping(addr, len, &origWritable, &origExecutable)) {
        return INV_ADDR;
    }

    // Android 10-16 ARM64 maps libanogs.so / libUE4.so .text as Execute-Only (XOM: --xp).
    // Preserve PROT_WRITE if the page was writable, while ensuring PROT_READ is enabled.
    int readProt = origWritable
            ? (origExecutable ? _PROT_RWX_ : (PROT_READ | PROT_WRITE))
            : (origExecutable ? _PROT_RX_ : PROT_READ);
    ProtectAddr(const_cast<void *>(addr), len, readProt);

    if (memcpy(buffer, addr, len) != NULL)
        return SUCCESS;
    return FAILED;
}

std::string KittyMemory::read2HexStr(const void *addr, size_t len) {
    if (addr == nullptr || len == 0 || len > INT_MAX / 2)
        return {};
    std::vector<unsigned char> bytes(len, 0);
    if (memRead(bytes.data(), addr, len) != SUCCESS)
        return {};
    static constexpr char HEX[] = "0123456789ABCDEF";
    std::string result(len * 2, '0');
    for (size_t i = 0; i < len; ++i) {
        result[i * 2] = HEX[bytes[i] >> 4];
        result[i * 2 + 1] = HEX[bytes[i] & 0x0F];
    }
    return result;
}

ProcMap KittyMemory::getLibraryMap(const char *libraryName) {
    ProcMap retMap{};
    retMap.startAddr = nullptr;
    retMap.endAddr = nullptr;
    retMap.length = 0;
    retMap.offset = 0;
    retMap.inode = 0;
    if (!libraryName || libraryName[0] == '\\0') return retMap;

    char line[512] = {0};
    FILE *fp = fopen("/proc/self/maps", "rt");
    if (fp != NULL) {
        bool foundBase = false;
        while (fgets(line, sizeof(line), fp)) {
            if (strstr(line, libraryName)) {
                unsigned long long startAddr = 0, endAddr = 0;
                long segOffset = 0;
                int segInode = 0;
                char tmpPerms[8] = {0}, tmpDev[16] = {0}, tmpPathname[444] = {0};
                int parsed = sscanf(line, "%llx-%llx %7s %lx %15s %d %443s",
                       &startAddr, &endAddr,
                       tmpPerms, &segOffset, tmpDev, &segInode, tmpPathname);
                if (parsed < 6) continue;
                if (strstr(tmpPerms, "---") != nullptr) {
                    continue; // Skip unreadable guard pages
                }
                // Ensure XOM (--xp) segment is readable so ELF header & offset checks never fault
                if (strchr(tmpPerms, 'x') != nullptr && strchr(tmpPerms, 'r') == nullptr) {
                    ProtectAddr(reinterpret_cast<void *>(static_cast<uintptr_t>(startAddr)),
                                static_cast<size_t>(endAddr - startAddr),
                                _PROT_RX_);
                }
                if (!foundBase) {
                    retMap.startAddr = reinterpret_cast<void *>(static_cast<uintptr_t>(startAddr));
                    retMap.endAddr   = reinterpret_cast<void *>(static_cast<uintptr_t>(endAddr));
                    retMap.length    = static_cast<size_t>(endAddr - startAddr);
                    retMap.offset    = segOffset;
                    retMap.perms     = tmpPerms;
                    retMap.dev       = tmpDev;
                    retMap.inode     = segInode;
                    retMap.pathname  = tmpPathname;
                    foundBase = true;
                } else {
                    // Expand endAddr across contiguous/subsequent segments of the same library
                    retMap.endAddr = reinterpret_cast<void *>(static_cast<uintptr_t>(endAddr));
                    retMap.length  = static_cast<size_t>(endAddr - reinterpret_cast<uintptr_t>(retMap.startAddr));
                }
            }
        }
        fclose(fp);
    }
    return retMap;
}

uintptr_t KittyMemory::getAbsoluteAddress(const char *libraryName, uintptr_t relativeAddr, bool useCache) {
    if (!libraryName || relativeAddr == 0) return 0;
    ProcMap libMap;
    if (useCache) {
        libMap = findMapInCache(libraryName);
        if (libMap.isValid()) {
            return (reinterpret_cast<uintptr_t>(libMap.startAddr) + relativeAddr);
        }
    }
    libMap = getLibraryMap(libraryName);
    if (!libMap.isValid())
        return 0;
    if (useCache) {
        std::lock_guard<std::mutex> lock(__mapsCacheMutex);
        mapsCache cachedMap;
        cachedMap.identifier = libraryName;
        cachedMap.map        = libMap;
        __mapsCache.push_back(cachedMap);
    }
    return (reinterpret_cast<uintptr_t>(libMap.startAddr) + relativeAddr);
}

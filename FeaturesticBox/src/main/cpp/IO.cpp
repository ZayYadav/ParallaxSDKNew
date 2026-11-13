#include "IO.h"
#include "Log.h"
#include <cstring>
#include <cstdlib>
#include <mutex>
#include <atomic>

jmethodID getAbsolutePathMethodId;
list<IO::RelocateInfo> relocate_rule;

namespace {
std::mutex g_rule_mutex;
std::atomic<bool> g_has_rules{false};

bool isAlreadyVirtualPath(const char *path) {
    if (!path) return true;
    if ((strstr(path, "/SdCard/") != nullptr && strstr(path, "/Android/obb/") == nullptr)
            || strstr(path, "/files/data/") != nullptr
            || strstr(path, "/files/user_de/") != nullptr
            || strstr(path, "/files/profiles/") != nullptr
            || strstr(path, "/files/proc/") != nullptr
            || strstr(path, "/Download/") != nullptr) {
        return true;
    }
    return false;
}
}

bool IO::hasRules() {
    return g_has_rules.load(std::memory_order_acquire);
}

const char *IO::redirectPathBuf(const char *path, char *outBuf, size_t outCap) {
    if (!path || path[0] != '/' || !g_has_rules.load(std::memory_order_acquire)) {
        return path;
    }
    if (!outBuf || outCap == 0 || isAlreadyVirtualPath(path)) {
        return path;
    }
    std::lock_guard<std::mutex> lock(g_rule_mutex);
    for (const auto &info : relocate_rule) {
        if (!info.targetPath || !info.relocatePath) continue;
        if (strstr(path, info.relocatePath) != nullptr) {
            return path;
        }
        size_t targetLen = strlen(info.targetPath);
        if (targetLen == 0) continue;
        if (strncmp(path, info.targetPath, targetLen) == 0
                && (path[targetLen] == '\0' || path[targetLen] == '/')) {
            size_t relocateLen = strlen(info.relocatePath);
            size_t tailLen = strlen(path + targetLen);
            if (relocateLen + tailLen + 1 <= outCap) {
                memcpy(outBuf, info.relocatePath, relocateLen);
                memcpy(outBuf + relocateLen, path + targetLen, tailLen + 1);
                return outBuf;
            }
            return path;
        }
    }
    return path;
}

const char *IO::redirectPath(const char *__path) {
    if (!__path || __path[0] == '\0' || isAlreadyVirtualPath(__path)) {
        return __path;
    }
    std::lock_guard<std::mutex> lock(g_rule_mutex);
    for (const auto &info : relocate_rule) {
        if (!info.targetPath || !info.relocatePath) continue;
        if (strstr(__path, info.relocatePath) != nullptr) {
            return __path;
        }
        size_t targetLen = strlen(info.targetPath);
        if (targetLen == 0) continue;
        if (strncmp(__path, info.targetPath, targetLen) == 0
                && (__path[targetLen] == '\0' || __path[targetLen] == '/')) {
            size_t relocateLen = strlen(info.relocatePath);
            size_t tailLen = strlen(__path + targetLen);
            char *ret = static_cast<char *>(malloc(relocateLen + tailLen + 1));
            if (ret) {
                memcpy(ret, info.relocatePath, relocateLen);
                memcpy(ret + relocateLen, __path + targetLen, tailLen + 1);
                return ret;
            }
        }
    }
    return __path;
}

jstring IO::redirectPath(JNIEnv *env, jstring path) {
    return BoxCore::redirectPathString(env, path);
}

jobject IO::redirectPath(JNIEnv *env, jobject path) {
    return BoxCore::redirectPathFile(env, path);
}

void IO::addRule(const char *targetPath, const char *relocatePath) {
    if (!targetPath || !relocatePath) return;
    std::lock_guard<std::mutex> lock(g_rule_mutex);
    for (const auto &existing : relocate_rule) {
        if (existing.targetPath && strcmp(existing.targetPath, targetPath) == 0) {
            return;
        }
    }
    IO::RelocateInfo info{};
    info.targetPath = strdup(targetPath);
    info.relocatePath = strdup(relocatePath);
    size_t newLen = strlen(targetPath);
    for (auto it = relocate_rule.begin(); it != relocate_rule.end(); ++it) {
        if (it->targetPath && newLen > strlen(it->targetPath)) {
            relocate_rule.insert(it, info);
            g_has_rules.store(true, std::memory_order_release);
            return;
        }
    }
    relocate_rule.push_back(info);
    g_has_rules.store(true, std::memory_order_release);
}

void IO::init(JNIEnv *env) {
    if (!env) return;
    jclass tmpFile = env->FindClass("java/io/File");
    if (tmpFile) {
        getAbsolutePathMethodId = env->GetMethodID(tmpFile, "getAbsolutePath", "()Ljava/lang/String;");
        env->DeleteLocalRef(tmpFile);
    }
}

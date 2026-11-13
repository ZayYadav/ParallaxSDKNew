//
// Created by Milk on 4/10/21.
//

#ifndef VIRTUAL_APP_IO_H
#define VIRTUAL_APP_IO_H

#include <jni.h>
#include <cstddef>
#include <list>
#include "BoxCore.h"

using namespace std;

class IO {
public:
    static void init(JNIEnv *env);

    struct RelocateInfo {
        const char *targetPath;
        const char *relocatePath;
    };

    static void addRule(const char *targetPath, const char *relocatePath);

    static jstring redirectPath(JNIEnv *env, jstring path);

    static jobject redirectPath(JNIEnv *env, jobject path);

    static const char *redirectPath(const char *__path);

    // Thread-safe, zero-allocation stack-buffer path redirect for C/C++ hooks.
    // Returns original `path` if no rule matches; otherwise writes to `outBuf` and returns `outBuf`.
    static const char *redirectPathBuf(const char *path, char *outBuf, size_t outCap);

    static bool hasRules();
};

#endif //VIRTUAL_APP_IO_H

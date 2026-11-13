#ifndef FEATURESTIC_ANOGS_COMPAT_H
#define FEATURESTIC_ANOGS_COMPAT_H

#include <jni.h>

class AnogsCompat {
public:
    // Install async-signal-safe SEGV_ACCERR fault guard, sigaction chain hook,
    // native dlopen/android_dlopen_ext hooks, and XOM unlocker.
    static void initRuntime(JNIEnv *env, int apiLevel);

    // Enable native C/C++ IO redirection hooks (openat, faccessat, fstatat, readlinkat).
    static void enableNativeIO();

    // Unlock Execute-Only Memory (--xp -> r-xp) and refresh protected range cache.
    static void prepareLibrarySegments(const char *libPathOrName);
};

#endif // FEATURESTIC_ANOGS_COMPAT_H

package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRLoadedApkServiceDispatcher {
    public static LoadedApkServiceDispatcherStatic getWithException() {
        return BlackReflection.create(LoadedApkServiceDispatcherStatic.class, null, true);
    }
    public static LoadedApkServiceDispatcherStatic get() {
        return BlackReflection.create(LoadedApkServiceDispatcherStatic.class, null, false);
    }
    public static LoadedApkServiceDispatcherContext getWithException(final Object caller) {
        return BlackReflection.create(LoadedApkServiceDispatcherContext.class, caller, true);
    }
    public static LoadedApkServiceDispatcherContext get(final Object caller) {
        return BlackReflection.create(LoadedApkServiceDispatcherContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LoadedApkServiceDispatcherContext.class);
    }
}

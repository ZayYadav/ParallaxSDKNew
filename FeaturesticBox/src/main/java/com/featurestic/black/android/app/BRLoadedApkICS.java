package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRLoadedApkICS {
    public static LoadedApkICSStatic getWithException() {
        return BlackReflection.create(LoadedApkICSStatic.class, null, true);
    }
    public static LoadedApkICSStatic get() {
        return BlackReflection.create(LoadedApkICSStatic.class, null, false);
    }
    public static LoadedApkICSContext getWithException(final Object caller) {
        return BlackReflection.create(LoadedApkICSContext.class, caller, true);
    }
    public static LoadedApkICSContext get(final Object caller) {
        return BlackReflection.create(LoadedApkICSContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(LoadedApkICSContext.class);
    }
}

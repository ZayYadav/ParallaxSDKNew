package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRLoadedApk {
    public static LoadedApkStatic getWithException() {
        return BlackReflection.create(LoadedApkStatic.class, null, true);
    }
    public static LoadedApkStatic get() {
        return BlackReflection.create(LoadedApkStatic.class, null, false);
    }
    public static LoadedApkContext getWithException(final Object caller) {
        return BlackReflection.create(LoadedApkContext.class, caller, true);
    }
    public static LoadedApkContext get(final Object caller) {
        return BlackReflection.create(LoadedApkContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LoadedApkContext.class);
    }
}

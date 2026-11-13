package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRLoadedApkKitkat {
    public static LoadedApkKitkatStatic getWithException() {
        return BlackReflection.create(LoadedApkKitkatStatic.class, null, true);
    }
    public static LoadedApkKitkatStatic get() {
        return BlackReflection.create(LoadedApkKitkatStatic.class, null, false);
    }
    public static LoadedApkKitkatContext getWithException(final Object caller) {
        return BlackReflection.create(LoadedApkKitkatContext.class, caller, true);
    }
    public static LoadedApkKitkatContext get(final Object caller) {
        return BlackReflection.create(LoadedApkKitkatContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LoadedApkKitkatContext.class);
    }
}

package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRLoadedApkHuaWei {
    public static LoadedApkHuaWeiStatic getWithException() {
        return BlackReflection.create(LoadedApkHuaWeiStatic.class, null, true);
    }
    public static LoadedApkHuaWeiStatic get() {
        return BlackReflection.create(LoadedApkHuaWeiStatic.class, null, false);
    }
    public static LoadedApkHuaWeiContext getWithException(final Object caller) {
        return BlackReflection.create(LoadedApkHuaWeiContext.class, caller, true);
    }
    public static LoadedApkHuaWeiContext get(final Object caller) {
        return BlackReflection.create(LoadedApkHuaWeiContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LoadedApkHuaWeiContext.class);
    }
}

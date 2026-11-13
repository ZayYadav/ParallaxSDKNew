package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRIWallpaperManagerStub {
    public static IWallpaperManagerStubStatic getWithException() {
        return BlackReflection.create(IWallpaperManagerStubStatic.class, null, true);
    }
    public static IWallpaperManagerStubStatic get() {
        return BlackReflection.create(IWallpaperManagerStubStatic.class, null, false);
    }
    public static IWallpaperManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IWallpaperManagerStubContext.class, caller, true);
    }
    public static IWallpaperManagerStubContext get(final Object caller) {
        return BlackReflection.create(IWallpaperManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(IWallpaperManagerStubContext.class);
    }
}

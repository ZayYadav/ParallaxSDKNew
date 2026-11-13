package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRWallpaperManagerGlobals {
    public static WallpaperManagerGlobalsStatic getWithException() {
        return BlackReflection.create(WallpaperManagerGlobalsStatic.class, null, true);
    }
    public static WallpaperManagerGlobalsStatic get() {
        return BlackReflection.create(WallpaperManagerGlobalsStatic.class, null, false);
    }
    public static WallpaperManagerGlobalsContext getWithException(final Object caller) {
        return BlackReflection.create(WallpaperManagerGlobalsContext.class, caller, true);
    }
    public static WallpaperManagerGlobalsContext get(final Object caller) {
        return BlackReflection.create(WallpaperManagerGlobalsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(WallpaperManagerGlobalsContext.class);
    }
}

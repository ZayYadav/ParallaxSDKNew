package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRWallpaperManager {
    public static WallpaperManagerStatic getWithException() {
        return BlackReflection.create(WallpaperManagerStatic.class, null, true);
    }
    public static WallpaperManagerStatic get() {
        return BlackReflection.create(WallpaperManagerStatic.class, null, false);
    }
    public static WallpaperManagerContext getWithException(final Object caller) {
        return BlackReflection.create(WallpaperManagerContext.class, caller, true);
    }
    public static WallpaperManagerContext get(final Object caller) {
        return BlackReflection.create(WallpaperManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(WallpaperManagerContext.class);
    }
}

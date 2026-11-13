package com.featurestic.black.android.content.pm;

import com.featurestic.blackreflection.BlackReflection;

public class BRILauncherApps {
    public static ILauncherAppsStatic getWithException() {
        return BlackReflection.create(ILauncherAppsStatic.class, null, true);
    }
    public static ILauncherAppsStatic get() {
        return BlackReflection.create(ILauncherAppsStatic.class, null, false);
    }
    public static ILauncherAppsContext getWithException(final Object caller) {
        return BlackReflection.create(ILauncherAppsContext.class, caller, true);
    }
    public static ILauncherAppsContext get(final Object caller) {
        return BlackReflection.create(ILauncherAppsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ILauncherAppsContext.class);
    }
}

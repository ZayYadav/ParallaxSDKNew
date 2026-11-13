package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRILauncherAppsStub {
    public static ILauncherAppsStubStatic getWithException() {
        return BlackReflection.create(ILauncherAppsStubStatic.class, null, true);
    }
    public static ILauncherAppsStubStatic get() {
        return BlackReflection.create(ILauncherAppsStubStatic.class, null, false);
    }
    public static ILauncherAppsStubContext getWithException(final Object caller) {
        return BlackReflection.create(ILauncherAppsStubContext.class, caller, true);
    }
    public static ILauncherAppsStubContext get(final Object caller) {
        return BlackReflection.create(ILauncherAppsStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ILauncherAppsStubContext.class);
    }
}

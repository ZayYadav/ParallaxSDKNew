package com.teamdark.black.android.content.pm;

import com.teamdark.blackreflection.BlackReflection;

public class BRICrossProfileApps {
    public static ICrossProfileAppsStatic getWithException() {
        return BlackReflection.create(ICrossProfileAppsStatic.class, null, true);
    }
    public static ICrossProfileAppsStatic get() {
        return BlackReflection.create(ICrossProfileAppsStatic.class, null, false);
    }
    public static ICrossProfileAppsContext getWithException(final Object caller) {
        return BlackReflection.create(ICrossProfileAppsContext.class, caller, true);
    }
    public static ICrossProfileAppsContext get(final Object caller) {
        return BlackReflection.create(ICrossProfileAppsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ICrossProfileAppsContext.class);
    }
}

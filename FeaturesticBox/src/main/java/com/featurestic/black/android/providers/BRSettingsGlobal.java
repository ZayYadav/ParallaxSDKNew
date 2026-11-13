package com.featurestic.black.android.providers;

import com.featurestic.blackreflection.BlackReflection;

public class BRSettingsGlobal {
    public static SettingsGlobalStatic getWithException() {
        return BlackReflection.create(SettingsGlobalStatic.class, null, true);
    }
    public static SettingsGlobalStatic get() {
        return BlackReflection.create(SettingsGlobalStatic.class, null, false);
    }
    public static SettingsGlobalContext getWithException(final Object caller) {
        return BlackReflection.create(SettingsGlobalContext.class, caller, true);
    }
    public static SettingsGlobalContext get(final Object caller) {
        return BlackReflection.create(SettingsGlobalContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SettingsGlobalContext.class);
    }
}

package com.featurestic.black.android.providers;

import com.featurestic.blackreflection.BlackReflection;

public class BRSettings {
    public static SettingsStatic getWithException() {
        return BlackReflection.create(SettingsStatic.class, null, true);
    }
    public static SettingsStatic get() {
        return BlackReflection.create(SettingsStatic.class, null, false);
    }
    public static SettingsContext getWithException(final Object caller) {
        return BlackReflection.create(SettingsContext.class, caller, true);
    }
    public static SettingsContext get(final Object caller) {
        return BlackReflection.create(SettingsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SettingsContext.class);
    }
}

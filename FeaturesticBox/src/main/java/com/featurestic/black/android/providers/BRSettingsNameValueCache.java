package com.featurestic.black.android.providers;

import com.featurestic.blackreflection.BlackReflection;

public class BRSettingsNameValueCache {
    public static SettingsNameValueCacheStatic getWithException() {
        return BlackReflection.create(SettingsNameValueCacheStatic.class, null, true);
    }
    public static SettingsNameValueCacheStatic get() {
        return BlackReflection.create(SettingsNameValueCacheStatic.class, null, false);
    }
    public static SettingsNameValueCacheContext getWithException(final Object caller) {
        return BlackReflection.create(SettingsNameValueCacheContext.class, caller, true);
    }
    public static SettingsNameValueCacheContext get(final Object caller) {
        return BlackReflection.create(SettingsNameValueCacheContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SettingsNameValueCacheContext.class);
    }
}

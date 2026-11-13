package com.teamdark.black.android.providers;

import com.teamdark.blackreflection.BlackReflection;

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
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(SettingsNameValueCacheContext.class);
    }
}

package com.teamdark.black.android.providers;

import com.teamdark.blackreflection.BlackReflection;

public class BRSettingsNameValueCacheOreo {
    public static SettingsNameValueCacheOreoStatic getWithException() {
        return BlackReflection.create(SettingsNameValueCacheOreoStatic.class, null, true);
    }
    public static SettingsNameValueCacheOreoStatic get() {
        return BlackReflection.create(SettingsNameValueCacheOreoStatic.class, null, false);
    }
    public static SettingsNameValueCacheOreoContext getWithException(final Object caller) {
        return BlackReflection.create(SettingsNameValueCacheOreoContext.class, caller, true);
    }
    public static SettingsNameValueCacheOreoContext get(final Object caller) {
        return BlackReflection.create(SettingsNameValueCacheOreoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(SettingsNameValueCacheOreoContext.class);
    }
}

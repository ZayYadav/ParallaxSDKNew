package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRActivityThreadProviderClientRecord {
    public static ActivityThreadProviderClientRecordStatic getWithException() {
        return BlackReflection.create(ActivityThreadProviderClientRecordStatic.class, null, true);
    }
    public static ActivityThreadProviderClientRecordStatic get() {
        return BlackReflection.create(ActivityThreadProviderClientRecordStatic.class, null, false);
    }
    public static ActivityThreadProviderClientRecordContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderClientRecordContext.class, caller, true);
    }
    public static ActivityThreadProviderClientRecordContext get(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderClientRecordContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ActivityThreadProviderClientRecordContext.class);
    }
}

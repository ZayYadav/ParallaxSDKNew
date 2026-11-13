package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRActivityThreadProviderClientRecordP {
    public static ActivityThreadProviderClientRecordPStatic getWithException() {
        return BlackReflection.create(ActivityThreadProviderClientRecordPStatic.class, null, true);
    }
    public static ActivityThreadProviderClientRecordPStatic get() {
        return BlackReflection.create(ActivityThreadProviderClientRecordPStatic.class, null, false);
    }
    public static ActivityThreadProviderClientRecordPContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderClientRecordPContext.class, caller, true);
    }
    public static ActivityThreadProviderClientRecordPContext get(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderClientRecordPContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ActivityThreadProviderClientRecordPContext.class);
    }
}

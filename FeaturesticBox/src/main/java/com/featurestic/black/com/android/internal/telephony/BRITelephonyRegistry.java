package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRITelephonyRegistry {
    public static ITelephonyRegistryStatic getWithException() {
        return BlackReflection.create(ITelephonyRegistryStatic.class, null, true);
    }
    public static ITelephonyRegistryStatic get() {
        return BlackReflection.create(ITelephonyRegistryStatic.class, null, false);
    }
    public static ITelephonyRegistryContext getWithException(final Object caller) {
        return BlackReflection.create(ITelephonyRegistryContext.class, caller, true);
    }
    public static ITelephonyRegistryContext get(final Object caller) {
        return BlackReflection.create(ITelephonyRegistryContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ITelephonyRegistryContext.class);
    }
}

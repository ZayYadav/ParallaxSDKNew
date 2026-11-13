package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRITelephonyRegistryStub {
    public static ITelephonyRegistryStubStatic getWithException() {
        return BlackReflection.create(ITelephonyRegistryStubStatic.class, null, true);
    }
    public static ITelephonyRegistryStubStatic get() {
        return BlackReflection.create(ITelephonyRegistryStubStatic.class, null, false);
    }
    public static ITelephonyRegistryStubContext getWithException(final Object caller) {
        return BlackReflection.create(ITelephonyRegistryStubContext.class, caller, true);
    }
    public static ITelephonyRegistryStubContext get(final Object caller) {
        return BlackReflection.create(ITelephonyRegistryStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ITelephonyRegistryStubContext.class);
    }
}

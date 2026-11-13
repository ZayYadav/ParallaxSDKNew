package com.featurestic.black.com.android.internal.telecom;

import com.featurestic.blackreflection.BlackReflection;

public class BRITelecomServiceStub {
    public static ITelecomServiceStubStatic getWithException() {
        return BlackReflection.create(ITelecomServiceStubStatic.class, null, true);
    }
    public static ITelecomServiceStubStatic get() {
        return BlackReflection.create(ITelecomServiceStubStatic.class, null, false);
    }
    public static ITelecomServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(ITelecomServiceStubContext.class, caller, true);
    }
    public static ITelecomServiceStubContext get(final Object caller) {
        return BlackReflection.create(ITelecomServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ITelecomServiceStubContext.class);
    }
}

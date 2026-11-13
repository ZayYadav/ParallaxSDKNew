package com.featurestic.black.com.android.internal.telecom;

import com.featurestic.blackreflection.BlackReflection;

public class BRITelecomService {
    public static ITelecomServiceStatic getWithException() {
        return BlackReflection.create(ITelecomServiceStatic.class, null, true);
    }
    public static ITelecomServiceStatic get() {
        return BlackReflection.create(ITelecomServiceStatic.class, null, false);
    }
    public static ITelecomServiceContext getWithException(final Object caller) {
        return BlackReflection.create(ITelecomServiceContext.class, caller, true);
    }
    public static ITelecomServiceContext get(final Object caller) {
        return BlackReflection.create(ITelecomServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ITelecomServiceContext.class);
    }
}

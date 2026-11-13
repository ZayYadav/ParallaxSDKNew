package com.featurestic.black.com.android.internal.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIVibratorServiceStub {
    public static IVibratorServiceStubStatic getWithException() {
        return BlackReflection.create(IVibratorServiceStubStatic.class, null, true);
    }
    public static IVibratorServiceStubStatic get() {
        return BlackReflection.create(IVibratorServiceStubStatic.class, null, false);
    }
    public static IVibratorServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IVibratorServiceStubContext.class, caller, true);
    }
    public static IVibratorServiceStubContext get(final Object caller) {
        return BlackReflection.create(IVibratorServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IVibratorServiceStubContext.class);
    }
}

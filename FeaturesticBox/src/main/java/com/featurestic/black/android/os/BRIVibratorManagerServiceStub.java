package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIVibratorManagerServiceStub {
    public static IVibratorManagerServiceStubStatic getWithException() {
        return BlackReflection.create(IVibratorManagerServiceStubStatic.class, null, true);
    }
    public static IVibratorManagerServiceStubStatic get() {
        return BlackReflection.create(IVibratorManagerServiceStubStatic.class, null, false);
    }
    public static IVibratorManagerServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IVibratorManagerServiceStubContext.class, caller, true);
    }
    public static IVibratorManagerServiceStubContext get(final Object caller) {
        return BlackReflection.create(IVibratorManagerServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IVibratorManagerServiceStubContext.class);
    }
}

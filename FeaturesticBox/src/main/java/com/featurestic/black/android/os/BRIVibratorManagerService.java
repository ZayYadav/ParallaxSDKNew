package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRIVibratorManagerService {
    public static IVibratorManagerServiceStatic getWithException() {
        return BlackReflection.create(IVibratorManagerServiceStatic.class, null, true);
    }
    public static IVibratorManagerServiceStatic get() {
        return BlackReflection.create(IVibratorManagerServiceStatic.class, null, false);
    }
    public static IVibratorManagerServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IVibratorManagerServiceContext.class, caller, true);
    }
    public static IVibratorManagerServiceContext get(final Object caller) {
        return BlackReflection.create(IVibratorManagerServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IVibratorManagerServiceContext.class);
    }
}

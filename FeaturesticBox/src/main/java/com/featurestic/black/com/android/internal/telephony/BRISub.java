package com.featurestic.black.com.android.internal.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRISub {
    public static ISubStatic getWithException() {
        return BlackReflection.create(ISubStatic.class, null, true);
    }
    public static ISubStatic get() {
        return BlackReflection.create(ISubStatic.class, null, false);
    }
    public static ISubContext getWithException(final Object caller) {
        return BlackReflection.create(ISubContext.class, caller, true);
    }
    public static ISubContext get(final Object caller) {
        return BlackReflection.create(ISubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ISubContext.class);
    }
}

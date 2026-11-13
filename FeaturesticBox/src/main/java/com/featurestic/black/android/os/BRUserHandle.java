package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRUserHandle {
    public static UserHandleStatic getWithException() {
        return BlackReflection.create(UserHandleStatic.class, null, true);
    }
    public static UserHandleStatic get() {
        return BlackReflection.create(UserHandleStatic.class, null, false);
    }
    public static UserHandleContext getWithException(final Object caller) {
        return BlackReflection.create(UserHandleContext.class, caller, true);
    }
    public static UserHandleContext get(final Object caller) {
        return BlackReflection.create(UserHandleContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(UserHandleContext.class);
    }
}

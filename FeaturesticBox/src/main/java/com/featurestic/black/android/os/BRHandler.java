package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRHandler {
    public static HandlerStatic getWithException() {
        return BlackReflection.create(HandlerStatic.class, null, true);
    }
    public static HandlerStatic get() {
        return BlackReflection.create(HandlerStatic.class, null, false);
    }
    public static HandlerContext getWithException(final Object caller) {
        return BlackReflection.create(HandlerContext.class, caller, true);
    }
    public static HandlerContext get(final Object caller) {
        return BlackReflection.create(HandlerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(HandlerContext.class);
    }
}

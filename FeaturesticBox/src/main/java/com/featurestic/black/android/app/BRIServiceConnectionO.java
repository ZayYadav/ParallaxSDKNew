package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRIServiceConnectionO {
    public static IServiceConnectionOStatic getWithException() {
        return BlackReflection.create(IServiceConnectionOStatic.class, null, true);
    }
    public static IServiceConnectionOStatic get() {
        return BlackReflection.create(IServiceConnectionOStatic.class, null, false);
    }
    public static IServiceConnectionOContext getWithException(final Object caller) {
        return BlackReflection.create(IServiceConnectionOContext.class, caller, true);
    }
    public static IServiceConnectionOContext get(final Object caller) {
        return BlackReflection.create(IServiceConnectionOContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IServiceConnectionOContext.class);
    }
}

package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRApplicationThreadNative {
    public static ApplicationThreadNativeStatic getWithException() {
        return BlackReflection.create(ApplicationThreadNativeStatic.class, null, true);
    }
    public static ApplicationThreadNativeStatic get() {
        return BlackReflection.create(ApplicationThreadNativeStatic.class, null, false);
    }
    public static ApplicationThreadNativeContext getWithException(final Object caller) {
        return BlackReflection.create(ApplicationThreadNativeContext.class, caller, true);
    }
    public static ApplicationThreadNativeContext get(final Object caller) {
        return BlackReflection.create(ApplicationThreadNativeContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ApplicationThreadNativeContext.class);
    }
}

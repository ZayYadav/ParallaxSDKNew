package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRContextImplKitkat {
    public static ContextImplKitkatStatic getWithException() {
        return BlackReflection.create(ContextImplKitkatStatic.class, null, true);
    }
    public static ContextImplKitkatStatic get() {
        return BlackReflection.create(ContextImplKitkatStatic.class, null, false);
    }
    public static ContextImplKitkatContext getWithException(final Object caller) {
        return BlackReflection.create(ContextImplKitkatContext.class, caller, true);
    }
    public static ContextImplKitkatContext get(final Object caller) {
        return BlackReflection.create(ContextImplKitkatContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ContextImplKitkatContext.class);
    }
}

package com.featurestic.black.android.hardware.display;

import com.featurestic.blackreflection.BlackReflection;

public class BRIDisplayManager {
    public static IDisplayManagerStatic getWithException() {
        return BlackReflection.create(IDisplayManagerStatic.class, null, true);
    }
    public static IDisplayManagerStatic get() {
        return BlackReflection.create(IDisplayManagerStatic.class, null, false);
    }
    public static IDisplayManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IDisplayManagerContext.class, caller, true);
    }
    public static IDisplayManagerContext get(final Object caller) {
        return BlackReflection.create(IDisplayManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IDisplayManagerContext.class);
    }
}

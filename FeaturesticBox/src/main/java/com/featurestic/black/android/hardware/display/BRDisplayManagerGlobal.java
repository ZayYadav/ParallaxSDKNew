package com.featurestic.black.android.hardware.display;

import com.featurestic.blackreflection.BlackReflection;

public class BRDisplayManagerGlobal {
    public static DisplayManagerGlobalStatic getWithException() {
        return BlackReflection.create(DisplayManagerGlobalStatic.class, null, true);
    }
    public static DisplayManagerGlobalStatic get() {
        return BlackReflection.create(DisplayManagerGlobalStatic.class, null, false);
    }
    public static DisplayManagerGlobalContext getWithException(final Object caller) {
        return BlackReflection.create(DisplayManagerGlobalContext.class, caller, true);
    }
    public static DisplayManagerGlobalContext get(final Object caller) {
        return BlackReflection.create(DisplayManagerGlobalContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(DisplayManagerGlobalContext.class);
    }
}

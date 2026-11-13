package com.featurestic.black.android.view;

import com.featurestic.blackreflection.BlackReflection;

public class BRWindowManagerGlobal {
    public static WindowManagerGlobalStatic getWithException() {
        return BlackReflection.create(WindowManagerGlobalStatic.class, null, true);
    }
    public static WindowManagerGlobalStatic get() {
        return BlackReflection.create(WindowManagerGlobalStatic.class, null, false);
    }
    public static WindowManagerGlobalContext getWithException(final Object caller) {
        return BlackReflection.create(WindowManagerGlobalContext.class, caller, true);
    }
    public static WindowManagerGlobalContext get(final Object caller) {
        return BlackReflection.create(WindowManagerGlobalContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(WindowManagerGlobalContext.class);
    }
}

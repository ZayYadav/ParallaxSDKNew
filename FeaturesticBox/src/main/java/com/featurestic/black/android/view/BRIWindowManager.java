package com.featurestic.black.android.view;

import com.featurestic.blackreflection.BlackReflection;

public class BRIWindowManager {
    public static IWindowManagerStatic getWithException() {
        return BlackReflection.create(IWindowManagerStatic.class, null, true);
    }
    public static IWindowManagerStatic get() {
        return BlackReflection.create(IWindowManagerStatic.class, null, false);
    }
    public static IWindowManagerContext getWithException(final Object caller) {
        return BlackReflection.create(IWindowManagerContext.class, caller, true);
    }
    public static IWindowManagerContext get(final Object caller) {
        return BlackReflection.create(IWindowManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IWindowManagerContext.class);
    }
}

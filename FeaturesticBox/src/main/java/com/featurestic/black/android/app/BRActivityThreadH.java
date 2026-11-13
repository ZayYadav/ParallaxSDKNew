package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRActivityThreadH {
    public static ActivityThreadHStatic getWithException() {
        return BlackReflection.create(ActivityThreadHStatic.class, null, true);
    }
    public static ActivityThreadHStatic get() {
        return BlackReflection.create(ActivityThreadHStatic.class, null, false);
    }
    public static ActivityThreadHContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadHContext.class, caller, true);
    }
    public static ActivityThreadHContext get(final Object caller) {
        return BlackReflection.create(ActivityThreadHContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ActivityThreadHContext.class);
    }
}

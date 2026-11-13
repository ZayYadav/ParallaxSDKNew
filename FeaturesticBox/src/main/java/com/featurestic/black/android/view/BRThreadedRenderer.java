package com.featurestic.black.android.view;

import com.featurestic.blackreflection.BlackReflection;

public class BRThreadedRenderer {
    public static ThreadedRendererStatic getWithException() {
        return BlackReflection.create(ThreadedRendererStatic.class, null, true);
    }
    public static ThreadedRendererStatic get() {
        return BlackReflection.create(ThreadedRendererStatic.class, null, false);
    }
    public static ThreadedRendererContext getWithException(final Object caller) {
        return BlackReflection.create(ThreadedRendererContext.class, caller, true);
    }
    public static ThreadedRendererContext get(final Object caller) {
        return BlackReflection.create(ThreadedRendererContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ThreadedRendererContext.class);
    }
}

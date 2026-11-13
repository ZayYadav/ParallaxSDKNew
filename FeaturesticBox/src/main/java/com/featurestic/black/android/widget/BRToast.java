package com.featurestic.black.android.widget;

import com.featurestic.blackreflection.BlackReflection;

public class BRToast {
    public static ToastStatic getWithException() {
        return BlackReflection.create(ToastStatic.class, null, true);
    }
    public static ToastStatic get() {
        return BlackReflection.create(ToastStatic.class, null, false);
    }
    public static ToastContext getWithException(final Object caller) {
        return BlackReflection.create(ToastContext.class, caller, true);
    }
    public static ToastContext get(final Object caller) {
        return BlackReflection.create(ToastContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ToastContext.class);
    }
}

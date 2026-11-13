package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRStrictMode {
    public static StrictModeStatic getWithException() {
        return BlackReflection.create(StrictModeStatic.class, null, true);
    }
    public static StrictModeStatic get() {
        return BlackReflection.create(StrictModeStatic.class, null, false);
    }
    public static StrictModeContext getWithException(final Object caller) {
        return BlackReflection.create(StrictModeContext.class, caller, true);
    }
    public static StrictModeContext get(final Object caller) {
        return BlackReflection.create(StrictModeContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(StrictModeContext.class);
    }
}

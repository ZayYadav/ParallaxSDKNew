package com.featurestic.black.android.os;

import com.featurestic.blackreflection.BlackReflection;

public class BRProcess {
    public static ProcessStatic getWithException() {
        return BlackReflection.create(ProcessStatic.class, null, true);
    }
    public static ProcessStatic get() {
        return BlackReflection.create(ProcessStatic.class, null, false);
    }
    public static ProcessContext getWithException(final Object caller) {
        return BlackReflection.create(ProcessContext.class, caller, true);
    }
    public static ProcessContext get(final Object caller) {
        return BlackReflection.create(ProcessContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ProcessContext.class);
    }
}

package com.featurestic.black.com.android.internal;

import com.featurestic.blackreflection.BlackReflection;

public class BRRstyleable {
    public static RstyleableStatic getWithException() {
        return BlackReflection.create(RstyleableStatic.class, null, true);
    }
    public static RstyleableStatic get() {
        return BlackReflection.create(RstyleableStatic.class, null, false);
    }
    public static RstyleableContext getWithException(final Object caller) {
        return BlackReflection.create(RstyleableContext.class, caller, true);
    }
    public static RstyleableContext get(final Object caller) {
        return BlackReflection.create(RstyleableContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(RstyleableContext.class);
    }
}

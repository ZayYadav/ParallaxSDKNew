package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRInstrumentation {
    public static InstrumentationStatic getWithException() {
        return BlackReflection.create(InstrumentationStatic.class, null, true);
    }
    public static InstrumentationStatic get() {
        return BlackReflection.create(InstrumentationStatic.class, null, false);
    }
    public static InstrumentationContext getWithException(final Object caller) {
        return BlackReflection.create(InstrumentationContext.class, caller, true);
    }
    public static InstrumentationContext get(final Object caller) {
        return BlackReflection.create(InstrumentationContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(InstrumentationContext.class);
    }
}

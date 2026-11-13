package com.featurestic.black.android.graphics;

import com.featurestic.blackreflection.BlackReflection;

public class BRCompatibility {
    public static CompatibilityStatic getWithException() {
        return BlackReflection.create(CompatibilityStatic.class, null, true);
    }
    public static CompatibilityStatic get() {
        return BlackReflection.create(CompatibilityStatic.class, null, false);
    }
    public static CompatibilityContext getWithException(final Object caller) {
        return BlackReflection.create(CompatibilityContext.class, caller, true);
    }
    public static CompatibilityContext get(final Object caller) {
        return BlackReflection.create(CompatibilityContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(CompatibilityContext.class);
    }
}

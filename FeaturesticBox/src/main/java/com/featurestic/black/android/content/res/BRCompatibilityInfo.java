package com.featurestic.black.android.content.res;

import com.featurestic.blackreflection.BlackReflection;

public class BRCompatibilityInfo {
    public static CompatibilityInfoStatic getWithException() {
        return BlackReflection.create(CompatibilityInfoStatic.class, null, true);
    }
    public static CompatibilityInfoStatic get() {
        return BlackReflection.create(CompatibilityInfoStatic.class, null, false);
    }
    public static CompatibilityInfoContext getWithException(final Object caller) {
        return BlackReflection.create(CompatibilityInfoContext.class, caller, true);
    }
    public static CompatibilityInfoContext get(final Object caller) {
        return BlackReflection.create(CompatibilityInfoContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(CompatibilityInfoContext.class);
    }
}

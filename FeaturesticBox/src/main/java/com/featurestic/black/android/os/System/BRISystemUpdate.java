package com.featurestic.black.android.os.System;

import com.featurestic.blackreflection.BlackReflection;

public class BRISystemUpdate {
    public static ISystemUpdateStatic getWithException() {
        return BlackReflection.create(ISystemUpdateStatic.class, null, true);
    }
    public static ISystemUpdateStatic get() {
        return BlackReflection.create(ISystemUpdateStatic.class, null, false);
    }
    public static ISystemUpdateContext getWithException(final Object caller) {
        return BlackReflection.create(ISystemUpdateContext.class, caller, true);
    }
    public static ISystemUpdateContext get(final Object caller) {
        return BlackReflection.create(ISystemUpdateContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(ISystemUpdateContext.class);
    }
}

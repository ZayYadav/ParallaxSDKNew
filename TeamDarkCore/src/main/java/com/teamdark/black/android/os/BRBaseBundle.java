package com.teamdark.black.android.os;

import com.teamdark.blackreflection.BlackReflection;

public class BRBaseBundle {
    public static BaseBundleStatic getWithException() {
        return BlackReflection.create(BaseBundleStatic.class, null, true);
    }
    public static BaseBundleStatic get() {
        return BlackReflection.create(BaseBundleStatic.class, null, false);
    }
    public static BaseBundleContext getWithException(final Object caller) {
        return BlackReflection.create(BaseBundleContext.class, caller, true);
    }
    public static BaseBundleContext get(final Object caller) {
        return BlackReflection.create(BaseBundleContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(BaseBundleContext.class);
    }
}

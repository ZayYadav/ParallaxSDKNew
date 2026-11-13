package com.teamdark.black.com.android.internal;

import com.teamdark.blackreflection.BlackReflection;

public class BRRid {
    public static RidStatic getWithException() {
        return BlackReflection.create(RidStatic.class, null, true);
    }
    public static RidStatic get() {
        return BlackReflection.create(RidStatic.class, null, false);
    }
    public static RidContext getWithException(final Object caller) {
        return BlackReflection.create(RidContext.class, caller, true);
    }
    public static RidContext get(final Object caller) {
        return BlackReflection.create(RidContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(RidContext.class);
    }
}

package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRLocationManagerQ {
    public static LocationManagerQStatic getWithException() {
        return BlackReflection.create(LocationManagerQStatic.class, null, true);
    }
    public static LocationManagerQStatic get() {
        return BlackReflection.create(LocationManagerQStatic.class, null, false);
    }
    public static LocationManagerQContext getWithException(final Object caller) {
        return BlackReflection.create(LocationManagerQContext.class, caller, true);
    }
    public static LocationManagerQContext get(final Object caller) {
        return BlackReflection.create(LocationManagerQContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LocationManagerQContext.class);
    }
}

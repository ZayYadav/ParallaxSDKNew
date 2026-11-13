package com.teamdark.black.android.location;

import com.teamdark.blackreflection.BlackReflection;

public class BRLocationRequestL {
    public static LocationRequestLStatic getWithException() {
        return BlackReflection.create(LocationRequestLStatic.class, null, true);
    }
    public static LocationRequestLStatic get() {
        return BlackReflection.create(LocationRequestLStatic.class, null, false);
    }
    public static LocationRequestLContext getWithException(final Object caller) {
        return BlackReflection.create(LocationRequestLContext.class, caller, true);
    }
    public static LocationRequestLContext get(final Object caller) {
        return BlackReflection.create(LocationRequestLContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(LocationRequestLContext.class);
    }
}

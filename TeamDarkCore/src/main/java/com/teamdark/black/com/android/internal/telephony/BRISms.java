package com.teamdark.black.com.android.internal.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRISms {
    public static ISmsStatic getWithException() {
        return BlackReflection.create(ISmsStatic.class, null, true);
    }
    public static ISmsStatic get() {
        return BlackReflection.create(ISmsStatic.class, null, false);
    }
    public static ISmsContext getWithException(final Object caller) {
        return BlackReflection.create(ISmsContext.class, caller, true);
    }
    public static ISmsContext get(final Object caller) {
        return BlackReflection.create(ISmsContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ISmsContext.class);
    }
}

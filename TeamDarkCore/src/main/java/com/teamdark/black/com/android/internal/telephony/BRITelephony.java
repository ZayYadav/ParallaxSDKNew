package com.teamdark.black.com.android.internal.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRITelephony {
    public static ITelephonyStatic getWithException() {
        return BlackReflection.create(ITelephonyStatic.class, null, true);
    }
    public static ITelephonyStatic get() {
        return BlackReflection.create(ITelephonyStatic.class, null, false);
    }
    public static ITelephonyContext getWithException(final Object caller) {
        return BlackReflection.create(ITelephonyContext.class, caller, true);
    }
    public static ITelephonyContext get(final Object caller) {
        return BlackReflection.create(ITelephonyContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ITelephonyContext.class);
    }
}

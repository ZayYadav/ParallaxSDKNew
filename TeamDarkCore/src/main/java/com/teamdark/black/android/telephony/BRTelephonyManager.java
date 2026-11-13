package com.teamdark.black.android.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRTelephonyManager {
    public static TelephonyManagerStatic getWithException() {
        return BlackReflection.create(TelephonyManagerStatic.class, null, true);
    }
    public static TelephonyManagerStatic get() {
        return BlackReflection.create(TelephonyManagerStatic.class, null, false);
    }
    public static TelephonyManagerContext getWithException(final Object caller) {
        return BlackReflection.create(TelephonyManagerContext.class, caller, true);
    }
    public static TelephonyManagerContext get(final Object caller) {
        return BlackReflection.create(TelephonyManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(TelephonyManagerContext.class);
    }
}

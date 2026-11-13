package com.teamdark.black.android.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRCellIdentityGsm {
    public static CellIdentityGsmStatic getWithException() {
        return BlackReflection.create(CellIdentityGsmStatic.class, null, true);
    }
    public static CellIdentityGsmStatic get() {
        return BlackReflection.create(CellIdentityGsmStatic.class, null, false);
    }
    public static CellIdentityGsmContext getWithException(final Object caller) {
        return BlackReflection.create(CellIdentityGsmContext.class, caller, true);
    }
    public static CellIdentityGsmContext get(final Object caller) {
        return BlackReflection.create(CellIdentityGsmContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(CellIdentityGsmContext.class);
    }
}

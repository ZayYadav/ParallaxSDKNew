package com.featurestic.black.android.telephony;

import com.featurestic.blackreflection.BlackReflection;

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
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(CellIdentityGsmContext.class);
    }
}

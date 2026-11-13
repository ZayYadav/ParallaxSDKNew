package com.featurestic.black.android.telephony;

import com.featurestic.blackreflection.BlackReflection;

public class BRCellIdentityCdma {
    public static CellIdentityCdmaStatic getWithException() {
        return BlackReflection.create(CellIdentityCdmaStatic.class, null, true);
    }
    public static CellIdentityCdmaStatic get() {
        return BlackReflection.create(CellIdentityCdmaStatic.class, null, false);
    }
    public static CellIdentityCdmaContext getWithException(final Object caller) {
        return BlackReflection.create(CellIdentityCdmaContext.class, caller, true);
    }
    public static CellIdentityCdmaContext get(final Object caller) {
        return BlackReflection.create(CellIdentityCdmaContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(CellIdentityCdmaContext.class);
    }
}

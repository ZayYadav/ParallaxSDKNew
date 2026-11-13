package com.teamdark.black.android.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRCellInfoCdma {
    public static CellInfoCdmaStatic getWithException() {
        return BlackReflection.create(CellInfoCdmaStatic.class, null, true);
    }
    public static CellInfoCdmaStatic get() {
        return BlackReflection.create(CellInfoCdmaStatic.class, null, false);
    }
    public static CellInfoCdmaContext getWithException(final Object caller) {
        return BlackReflection.create(CellInfoCdmaContext.class, caller, true);
    }
    public static CellInfoCdmaContext get(final Object caller) {
        return BlackReflection.create(CellInfoCdmaContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(CellInfoCdmaContext.class);
    }
}

package com.teamdark.black.android.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRCellSignalStrengthCdma {
    public static CellSignalStrengthCdmaStatic getWithException() {
        return BlackReflection.create(CellSignalStrengthCdmaStatic.class, null, true);
    }
    public static CellSignalStrengthCdmaStatic get() {
        return BlackReflection.create(CellSignalStrengthCdmaStatic.class, null, false);
    }
    public static CellSignalStrengthCdmaContext getWithException(final Object caller) {
        return BlackReflection.create(CellSignalStrengthCdmaContext.class, caller, true);
    }
    public static CellSignalStrengthCdmaContext get(final Object caller) {
        return BlackReflection.create(CellSignalStrengthCdmaContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(CellSignalStrengthCdmaContext.class);
    }
}

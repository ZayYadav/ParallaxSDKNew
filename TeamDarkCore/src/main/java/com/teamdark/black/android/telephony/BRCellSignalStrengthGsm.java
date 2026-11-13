package com.teamdark.black.android.telephony;

import com.teamdark.blackreflection.BlackReflection;

public class BRCellSignalStrengthGsm {
    public static CellSignalStrengthGsmStatic getWithException() {
        return BlackReflection.create(CellSignalStrengthGsmStatic.class, null, true);
    }
    public static CellSignalStrengthGsmStatic get() {
        return BlackReflection.create(CellSignalStrengthGsmStatic.class, null, false);
    }
    public static CellSignalStrengthGsmContext getWithException(final Object caller) {
        return BlackReflection.create(CellSignalStrengthGsmContext.class, caller, true);
    }
    public static CellSignalStrengthGsmContext get(final Object caller) {
        return BlackReflection.create(CellSignalStrengthGsmContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(CellSignalStrengthGsmContext.class);
    }
}

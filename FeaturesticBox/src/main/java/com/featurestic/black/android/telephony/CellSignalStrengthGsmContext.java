package com.featurestic.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellSignalStrengthGsm")
public interface CellSignalStrengthGsmContext {
    @BFieldSetNotProcess
    void _set_mBitErrorRate(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBitErrorRate();
    @BFieldNotProcess
    java.lang.Integer mBitErrorRate();
    @BFieldSetNotProcess
    void _set_mSignalStrength(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSignalStrength();
    @BFieldNotProcess
    java.lang.Integer mSignalStrength();
}

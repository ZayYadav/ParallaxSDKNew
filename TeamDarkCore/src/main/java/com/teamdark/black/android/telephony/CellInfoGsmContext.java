package com.teamdark.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellInfoGsm")
public interface CellInfoGsmContext {
    @BFieldSetNotProcess
    void _set_mCellIdentityGsm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCellIdentityGsm();
    @BFieldNotProcess
    android.telephony.CellIdentityGsm mCellIdentityGsm();
    @BFieldSetNotProcess
    void _set_mCellSignalStrengthGsm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCellSignalStrengthGsm();
    @BFieldNotProcess
    android.telephony.CellSignalStrengthGsm mCellSignalStrengthGsm();
}

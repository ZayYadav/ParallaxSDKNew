package com.featurestic.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellInfoCdma")
public interface CellInfoCdmaContext {
    @BFieldSetNotProcess
    void _set_mCellIdentityCdma(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCellIdentityCdma();
    @BFieldNotProcess
    android.telephony.CellIdentityCdma mCellIdentityCdma();
    @BFieldSetNotProcess
    void _set_mCellSignalStrengthCdma(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCellSignalStrengthCdma();
    @BFieldNotProcess
    android.telephony.CellSignalStrengthCdma mCellSignalStrengthCdma();
}

package com.featurestic.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellSignalStrengthCdma")
public interface CellSignalStrengthCdmaContext {
    @BFieldSetNotProcess
    void _set_mCdmaDbm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCdmaDbm();
    @BFieldNotProcess
    java.lang.Integer mCdmaDbm();
    @BFieldSetNotProcess
    void _set_mCdmaEcio(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCdmaEcio();
    @BFieldNotProcess
    java.lang.Integer mCdmaEcio();
    @BFieldSetNotProcess
    void _set_mEvdoDbm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mEvdoDbm();
    @BFieldNotProcess
    java.lang.Integer mEvdoDbm();
    @BFieldSetNotProcess
    void _set_mEvdoEcio(final Object value);
    @BFieldCheckNotProcess
    Field _check_mEvdoEcio();
    @BFieldNotProcess
    java.lang.Integer mEvdoEcio();
    @BFieldSetNotProcess
    void _set_mEvdoSnr(final Object value);
    @BFieldCheckNotProcess
    Field _check_mEvdoSnr();
    @BFieldNotProcess
    java.lang.Integer mEvdoSnr();
}

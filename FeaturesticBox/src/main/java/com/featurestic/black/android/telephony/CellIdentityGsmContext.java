package com.featurestic.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellIdentityGsm")
public interface CellIdentityGsmContext {
    @BFieldSetNotProcess
    void _set_mCid(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCid();
    @BFieldNotProcess
    java.lang.Integer mCid();
    @BFieldSetNotProcess
    void _set_mLac(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLac();
    @BFieldNotProcess
    java.lang.Integer mLac();
    @BFieldSetNotProcess
    void _set_mMcc(final Object value);
    @BFieldCheckNotProcess
    Field _check_mMcc();
    @BFieldNotProcess
    java.lang.Integer mMcc();
    @BFieldSetNotProcess
    void _set_mMnc(final Object value);
    @BFieldCheckNotProcess
    Field _check_mMnc();
    @BFieldNotProcess
    java.lang.Integer mMnc();
}

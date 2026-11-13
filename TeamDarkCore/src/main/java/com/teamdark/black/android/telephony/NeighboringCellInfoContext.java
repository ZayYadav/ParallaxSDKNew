package com.teamdark.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.NeighboringCellInfo")
public interface NeighboringCellInfoContext {
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
    void _set_mRssi(final Object value);
    @BFieldCheckNotProcess
    Field _check_mRssi();
    @BFieldNotProcess
    java.lang.Integer mRssi();
}

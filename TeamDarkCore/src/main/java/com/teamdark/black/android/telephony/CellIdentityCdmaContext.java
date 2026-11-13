package com.teamdark.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.CellIdentityCdma")
public interface CellIdentityCdmaContext {
    @BFieldSetNotProcess
    void _set_mBasestationId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBasestationId();
    @BFieldNotProcess
    java.lang.Integer mBasestationId();
    @BFieldSetNotProcess
    void _set_mNetworkId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNetworkId();
    @BFieldNotProcess
    java.lang.Integer mNetworkId();
    @BFieldSetNotProcess
    void _set_mSystemId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSystemId();
    @BFieldNotProcess
    java.lang.Integer mSystemId();
}

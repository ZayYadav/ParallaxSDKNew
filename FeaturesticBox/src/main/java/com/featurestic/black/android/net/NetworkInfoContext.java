package com.featurestic.black.android.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.NetworkInfo")
public interface NetworkInfoContext {
    @BFieldSetNotProcess
    void _set_mDetailedState(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDetailedState();
    @BFieldNotProcess
    android.net.NetworkInfo.DetailedState mDetailedState();
    @BFieldSetNotProcess
    void _set_mState(final Object value);
    @BFieldCheckNotProcess
    Field _check_mState();
    @BFieldNotProcess
    android.net.NetworkInfo.State mState();
    @BFieldSetNotProcess
    void _set_mIsAvailable(final Object value);
    @BFieldCheckNotProcess
    Field _check_mIsAvailable();
    @BFieldNotProcess
    java.lang.Boolean mIsAvailable();
    @BFieldSetNotProcess
    void _set_mTypeName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mTypeName();
    @BFieldNotProcess
    java.lang.String mTypeName();
    @BFieldSetNotProcess
    void _set_mNetworkType(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNetworkType();
    @BFieldNotProcess
    java.lang.Integer mNetworkType();
}

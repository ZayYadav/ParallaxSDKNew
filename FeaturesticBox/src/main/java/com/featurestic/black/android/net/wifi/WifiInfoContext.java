package com.featurestic.black.android.net.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.wifi.WifiInfo")
public interface WifiInfoContext {
    @BFieldSetNotProcess
    void _set_mBSSID(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBSSID();
    @BFieldNotProcess
    java.lang.String mBSSID();
    @BFieldSetNotProcess
    void _set_mFrequency(final Object value);
    @BFieldCheckNotProcess
    Field _check_mFrequency();
    @BFieldNotProcess
    java.lang.Integer mFrequency();
    @BFieldSetNotProcess
    void _set_mIpAddress(final Object value);
    @BFieldCheckNotProcess
    Field _check_mIpAddress();
    @BFieldNotProcess
    java.net.InetAddress mIpAddress();
    @BFieldSetNotProcess
    void _set_mLinkSpeed(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLinkSpeed();
    @BFieldNotProcess
    java.lang.Integer mLinkSpeed();
    @BFieldSetNotProcess
    void _set_mMacAddress(final Object value);
    @BFieldCheckNotProcess
    Field _check_mMacAddress();
    @BFieldNotProcess
    java.lang.String mMacAddress();
    @BFieldSetNotProcess
    void _set_mNetworkId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNetworkId();
    @BFieldNotProcess
    java.lang.Integer mNetworkId();
    @BFieldSetNotProcess
    void _set_mRssi(final Object value);
    @BFieldCheckNotProcess
    Field _check_mRssi();
    @BFieldNotProcess
    java.lang.Integer mRssi();
    @BFieldSetNotProcess
    void _set_mSSID(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSSID();
    @BFieldNotProcess
    java.lang.String mSSID();
    @BFieldSetNotProcess
    void _set_mSupplicantState(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSupplicantState();
    @BFieldNotProcess
    android.net.wifi.SupplicantState mSupplicantState();
    @BFieldSetNotProcess
    void _set_mWifiSsid(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWifiSsid();
    @BFieldNotProcess
    java.lang.Object mWifiSsid();
}

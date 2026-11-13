package com.featurestic.black.android.net.wifi;

import android.net.wifi.SupplicantState;

import java.net.InetAddress;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BConstructor;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("android.net.wifi.WifiInfo")
public interface WifiInfo {
    @BConstructor
    WifiInfo _new();

    @BField
    String mBSSID();

    @BField
    int mFrequency();

    @BField
    InetAddress mIpAddress();

    @BField
    int mLinkSpeed();

    @BField
    String mMacAddress();

    @BField
    int mNetworkId();

    @BField
    int mRssi();

    @BField
    String mSSID();

    @BField
    SupplicantState mSupplicantState();

    @BField
    Object mWifiSsid();
}

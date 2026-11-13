package com.teamdark.black.android.net.wifi;

import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("android.net.wifi.WifiManager")
public interface WifiManager {
    @BStaticField
    IInterface sService();

    @BField
    IInterface mService();
}

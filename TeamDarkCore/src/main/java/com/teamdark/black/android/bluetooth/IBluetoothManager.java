package com.teamdark.black.android.bluetooth;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.bluetooth.IBluetoothManager")
public interface IBluetoothManager {
    @BClassName("android.bluetooth.IBluetoothManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

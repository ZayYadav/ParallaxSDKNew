package com.featurestic.black.android.bluetooth;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.bluetooth.IBluetoothManager")
public interface IBluetoothManager {
    @BClassName("android.bluetooth.IBluetoothManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

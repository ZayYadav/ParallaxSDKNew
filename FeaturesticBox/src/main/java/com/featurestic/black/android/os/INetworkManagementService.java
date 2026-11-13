package com.featurestic.black.android.os;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.os.INetworkManagementService")
public interface INetworkManagementService {
    @BClassName("android.os.INetworkManagementService$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

package com.teamdark.black.android.app.admin;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.app.admin.IDevicePolicyManager")
public interface IDevicePolicyManager {
    @BClassName("android.app.admin.IDevicePolicyManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

package com.teamdark.black.android.os;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

/**
 * @author gm
 * @function
 * @date :2024/4/23 16:22
 **/
@BClassName("android.os.IDeviceIdleController")
public interface IDeviceIdleController {
    @BClassName("android.os.IDeviceIdleController$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

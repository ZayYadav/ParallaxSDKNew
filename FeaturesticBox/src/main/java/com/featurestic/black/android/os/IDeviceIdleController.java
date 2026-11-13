package com.featurestic.black.android.os;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

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

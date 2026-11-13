package com.teamdark.black.com.android.internal.app;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("com.android.internal.app.IBatteryStats")
public interface IBatteryStats {
    @BClassName("com.android.internal.app.IBatteryStats$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

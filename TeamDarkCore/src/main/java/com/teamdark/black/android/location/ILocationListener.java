package com.teamdark.black.android.location;

import android.location.Location;
import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BMethod;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.location.ILocationListener")
public interface ILocationListener {
    @BMethod
    void onLocationChanged(Location Location0);

    @BClassName("android.location.ILocationListener$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

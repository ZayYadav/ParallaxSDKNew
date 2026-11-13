package com.teamdark.black.android.app;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.app.ApplicationThreadNative")
public interface ApplicationThreadNative {
    @BStaticMethod
    IInterface asInterface(IBinder IBinder0);
}

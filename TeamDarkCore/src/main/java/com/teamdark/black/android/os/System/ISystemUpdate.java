package com.teamdark.black.android.os.System;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BMethod;

@BClassName("android.os.ISystemUpdate")
public interface ISystemUpdate {

    @BClassName("android.os.ISystemUpdate$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder binder);
    }
}


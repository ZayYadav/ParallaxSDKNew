package com.featurestic.black.android.os.System;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;
import com.featurestic.blackreflection.annotation.BField;
import com.featurestic.blackreflection.annotation.BMethod;

@BClassName("android.os.ISystemUpdate")
public interface ISystemUpdate {

    @BClassName("android.os.ISystemUpdate$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder binder);
    }
}


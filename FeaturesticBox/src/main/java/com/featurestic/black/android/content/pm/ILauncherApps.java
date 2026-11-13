package com.featurestic.black.android.content.pm;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("mirror.android.content.pm.ILauncherApps")
public interface ILauncherApps {
    @BClassName("android.content.pm.ILauncherApps$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder binder);
    }
}

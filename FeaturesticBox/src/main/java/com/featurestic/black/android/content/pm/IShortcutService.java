package com.featurestic.black.android.content.pm;

import android.os.IBinder;
import android.os.IInterface;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.content.pm.IShortcutService")
public interface IShortcutService {
    @BClassName("android.content.pm.IShortcutService$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

package com.teamdark.black.android.view.accessibility;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.view.accessibility.IAccessibilityManager")
public interface IAccessibilityManager {
    @BClassName("android.view.accessibility.IAccessibilityManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

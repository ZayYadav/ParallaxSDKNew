package com.teamdark.black.android.content;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.content.IRestrictionsManager")
public interface IRestrictionsManager {
    @BClassName("android.content.IRestrictionsManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

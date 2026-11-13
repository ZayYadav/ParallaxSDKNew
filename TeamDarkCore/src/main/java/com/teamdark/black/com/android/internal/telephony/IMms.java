package com.teamdark.black.com.android.internal.telephony;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("com.android.internal.telephony.IMms")
public interface IMms {
    @BClassName("com.android.internal.telephony.IMms$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

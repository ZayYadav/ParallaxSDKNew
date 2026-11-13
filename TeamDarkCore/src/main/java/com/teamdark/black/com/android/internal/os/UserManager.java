package com.teamdark.black.com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.os.UserManager")
public interface UserManager {
    @BClassName("android.os.IUserManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

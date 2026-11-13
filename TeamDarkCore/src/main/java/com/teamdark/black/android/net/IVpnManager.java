package com.teamdark.black.android.net;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

/**
 * Created by TeamDarkCore on 2022/2/26.
 */
@BClassName("android.net.IVpnManager")
public interface IVpnManager {

    @BClassName("android.net.IVpnManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

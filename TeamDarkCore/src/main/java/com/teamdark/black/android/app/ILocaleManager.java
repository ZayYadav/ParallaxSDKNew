package com.teamdark.black.android.app;

import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;

/**
 * @author gm
 * @function
 * @date :2024/4/20 20:04
 **/
@BClassName("android.app.ILocaleManager")
public interface ILocaleManager {
    @BClassName("android.app.ILocaleManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

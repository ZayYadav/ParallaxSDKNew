package com.teamdark.black.com.android.internal.view;


import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BStaticField;
import com.teamdark.blackreflection.annotation.BStaticMethod;

//这里可能有点问题
@BClassName("com.android.internal.view.IInputMethodManager")
public interface IInputMethodManager {
    @BClassName("com.android.internal.view.IInputMethodManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder IBinder0);
    }
}

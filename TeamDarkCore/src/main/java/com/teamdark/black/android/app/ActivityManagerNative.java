package com.teamdark.black.android.app;

import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;
import com.teamdark.blackreflection.annotation.BStaticMethod;

@BClassName("android.app.ActivityManagerNative")
public interface ActivityManagerNative {
    @BStaticField
    Object gDefault();

    @BStaticMethod
    IInterface getDefault();
}

package com.teamdark.black.android.app;

import android.content.Intent;
import android.os.IBinder;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BMethod;

@BClassName("android.app.IActivityManager")
public interface IActivityManagerN {
    @BMethod
    Boolean finishActivity(IBinder IBinder0, int int1, Intent Intent2, int int3);
}

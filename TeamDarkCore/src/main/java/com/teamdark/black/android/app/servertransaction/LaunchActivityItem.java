package com.teamdark.black.android.app.servertransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.app.servertransaction.LaunchActivityItem")
public interface LaunchActivityItem {
    @BField
    ActivityInfo mInfo();

    @BField
    Intent mIntent();
}

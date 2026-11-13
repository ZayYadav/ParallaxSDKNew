package com.teamdark.black.android.app;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticField;

@BClassName("android.app.ActivityManager")
public interface ActivityManager {
    @BStaticField
    int START_INTENT_NOT_RESOLVED();

    @BStaticField
    int START_NOT_CURRENT_USER_ACTIVITY();

    @BStaticField
    int START_SUCCESS();

    @BStaticField
    int START_TASK_TO_FRONT();
}

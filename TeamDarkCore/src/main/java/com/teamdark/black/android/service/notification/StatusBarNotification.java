package com.teamdark.black.android.service.notification;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.service.notification.StatusBarNotification")
public interface StatusBarNotification {
    @BField
    Integer id();

    @BField
    String opPkg();

    @BField
    String pkg();

    @BField
    String tag();
}

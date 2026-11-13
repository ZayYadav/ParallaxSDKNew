package com.teamdark.black.android.app;

import java.util.List;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.app.NotificationChannelGroup")
public interface NotificationChannelGroup {
    @BField
    List<android.app.NotificationChannel> mChannels();

    @BField
    String mId();
}

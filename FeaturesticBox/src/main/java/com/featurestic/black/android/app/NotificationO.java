package com.featurestic.black.android.app;


import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;

@BClassName("android.app.Notification")
public interface NotificationO {
    @BField
    String mChannelId();

    @BField
    String mGroupKey();
}

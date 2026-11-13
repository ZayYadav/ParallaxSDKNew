package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationContext {
    @BMethodCheckNotProcess
    Method _check_setLatestEventInfo(android.content.Context Context0, java.lang.CharSequence CharSequence1, java.lang.CharSequence CharSequence2, android.app.PendingIntent PendingIntent3);
    java.lang.Void setLatestEventInfo(android.content.Context Context0, java.lang.CharSequence CharSequence1, java.lang.CharSequence CharSequence2, android.app.PendingIntent PendingIntent3);
}

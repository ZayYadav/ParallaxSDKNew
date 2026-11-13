package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Notification$Builder")
public interface NotificationLBuilderStatic {
    @BMethodCheckNotProcess
    Method _check_rebuild(android.content.Context Context0, android.app.Notification Notification1);
    android.app.Notification rebuild(android.content.Context Context0, android.app.Notification Notification1);
}

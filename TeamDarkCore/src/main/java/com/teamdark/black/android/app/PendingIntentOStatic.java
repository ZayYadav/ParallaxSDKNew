package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.PendingIntent")
public interface PendingIntentOStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.PendingIntentO _new(android.os.IBinder IBinder0, java.lang.Object Object1);
}

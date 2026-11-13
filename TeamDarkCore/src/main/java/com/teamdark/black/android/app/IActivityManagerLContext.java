package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IActivityManager")
public interface IActivityManagerLContext {
    @BMethodCheckNotProcess
    Method _check_finishActivity(android.os.IBinder IBinder0, int int1, android.content.Intent Intent2, boolean boolean3);
    java.lang.Boolean finishActivity(android.os.IBinder IBinder0, int int1, android.content.Intent Intent2, boolean boolean3);
}

package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IActivityManager")
public interface IActivityManagerNContext {
    @BMethodCheckNotProcess
    Method _check_finishActivity(android.os.IBinder IBinder0, int int1, android.content.Intent Intent2, int int3);
    java.lang.Boolean finishActivity(android.os.IBinder IBinder0, int int1, android.content.Intent Intent2, int int3);
}

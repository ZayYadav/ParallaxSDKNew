package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IActivityManager")
public interface IActivityManagerContext {
    @BMethodCheckNotProcess
    Method _check_getTaskForActivity(android.os.IBinder IBinder0, boolean boolean1);
    java.lang.Integer getTaskForActivity(android.os.IBinder IBinder0, boolean boolean1);
    @BMethodCheckNotProcess
    Method _check_overridePendingTransition(android.os.IBinder IBinder0, java.lang.String String1, int int2, int int3);
    java.lang.Void overridePendingTransition(android.os.IBinder IBinder0, java.lang.String String1, int int2, int int3);
    @BMethodCheckNotProcess
    Method _check_setRequestedOrientation(android.os.IBinder IBinder0, int int1);
    java.lang.Void setRequestedOrientation(android.os.IBinder IBinder0, int int1);
    @BMethodCheckNotProcess
    Method _check_startActivities();
    java.lang.Integer startActivities();
    @BMethodCheckNotProcess
    Method _check_startActivity(@BParamClassName("android.app.IApplicationThread") java.lang.Object caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, @BParamClassName("android.app.ProfilerInfo") java.lang.Object profilerInfo, android.os.Bundle bOptions);
    java.lang.Integer startActivity(@BParamClassName("android.app.IApplicationThread") java.lang.Object caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, @BParamClassName("android.app.ProfilerInfo") java.lang.Object profilerInfo, android.os.Bundle bOptions);
}

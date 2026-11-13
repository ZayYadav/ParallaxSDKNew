package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Service")
public interface ServiceContext {
    @BMethodCheckNotProcess
    Method _check_attach(android.content.Context context, @BParamClass(android.app.ActivityThread.class) java.lang.Object thread, java.lang.String className, android.os.IBinder token, android.app.Application application, java.lang.Object activityManager);
    java.lang.Void attach(android.content.Context context, @BParamClass(android.app.ActivityThread.class) java.lang.Object thread, java.lang.String className, android.os.IBinder token, android.app.Application application, java.lang.Object activityManager);
    @BMethodCheckNotProcess
    Method _check_createServiceBaseContext(@BParamClass(android.app.ActivityThread.class) java.lang.Object thread, @BParamClassName("android.app.LoadedApk") java.lang.Object packageInfo);
    android.content.Context createServiceBaseContext(@BParamClass(android.app.ActivityThread.class) java.lang.Object thread, @BParamClassName("android.app.LoadedApk") java.lang.Object packageInfo);
}

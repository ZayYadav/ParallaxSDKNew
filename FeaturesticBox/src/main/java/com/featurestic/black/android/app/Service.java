package com.featurestic.black.android.app;


import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.os.IBinder;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BMethod;
import com.featurestic.blackreflection.annotation.BParamClass;
import com.featurestic.blackreflection.annotation.BParamClassName;

@BClassName("android.app.Service")
public interface Service {
    @BMethod
    void attach(Context context,
                @BParamClass(ActivityThread.class) Object thread, String className, IBinder token,
                Application application, Object activityManager);

    @BMethod
    Context createServiceBaseContext(
            @BParamClass(ActivityThread.class) Object thread,
            @BParamClassName("android.app.LoadedApk") Object packageInfo);
}

package com.teamdark.black.android.app;


import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.os.IBinder;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BMethod;
import com.teamdark.blackreflection.annotation.BParamClass;
import com.teamdark.blackreflection.annotation.BParamClassName;

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

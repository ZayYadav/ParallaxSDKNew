package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadContext {
    @BMethodCheckNotProcess
    Method _check_getApplicationThread();
    android.os.IBinder getApplicationThread();
    @BMethodCheckNotProcess
    Method _check_getHandler();
    android.os.Handler getHandler();
    @BMethodCheckNotProcess
    Method _check_getProcessName();
    java.lang.String getProcessName();
    @BMethodCheckNotProcess
    Method _check_getSystemContext();
    java.lang.Object getSystemContext();
    @BMethodCheckNotProcess
    Method _check_getActivityClient(android.os.IBinder token);
    java.lang.Object getActivityClient(android.os.IBinder token);
    @BMethodCheckNotProcess
    Method _check_getLaunchingActivity(android.os.IBinder token);
    java.lang.Object getLaunchingActivity(android.os.IBinder token);
    @BMethodCheckNotProcess
    Method _check_getPackageInfo(android.content.pm.ApplicationInfo ai, @BParamClassName("android.content.res.CompatibilityInfo") java.lang.Object compatInfo, int flags);
    java.lang.Object getPackageInfo(android.content.pm.ApplicationInfo ai, @BParamClassName("android.content.res.CompatibilityInfo") java.lang.Object compatInfo, int flags);
    @BMethodCheckNotProcess
    Method _check_performNewIntents(android.os.IBinder IBinder0, java.util.List List1);
    java.lang.Void performNewIntents(android.os.IBinder IBinder0, java.util.List List1);
    @BMethodCheckNotProcess
    Method _check_sendActivityResult(android.os.IBinder IBinder0, java.lang.String String1, int int2, int int3, android.content.Intent Intent4);
    java.lang.Void sendActivityResult(android.os.IBinder IBinder0, java.lang.String String1, int int2, int int3, android.content.Intent Intent4);
    @BFieldSetNotProcess
    void _set_mAppThread(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAppThread();
    @BFieldNotProcess
    java.lang.Object mAppThread();
    @BFieldSetNotProcess
    void _set_mActivities(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActivities();
    @BFieldNotProcess
    java.util.Map<android.os.IBinder,java.lang.Object> mActivities();
    @BFieldSetNotProcess
    void _set_mBoundApplication(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBoundApplication();
    @BFieldNotProcess
    java.lang.Object mBoundApplication();
    @BFieldSetNotProcess
    void _set_mH(final Object value);
    @BFieldCheckNotProcess
    Field _check_mH();
    @BFieldNotProcess
    android.os.Handler mH();
    @BFieldSetNotProcess
    void _set_mInitialApplication(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInitialApplication();
    @BFieldNotProcess
    android.app.Application mInitialApplication();
    @BFieldSetNotProcess
    void _set_mInstrumentation(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInstrumentation();
    @BFieldNotProcess
    android.app.Instrumentation mInstrumentation();
    @BFieldSetNotProcess
    void _set_mPackages(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackages();
    @BFieldNotProcess
    java.util.Map<java.lang.String,java.lang.ref.WeakReference<?>> mPackages();
    @BFieldSetNotProcess
    void _set_mProviderMap(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProviderMap();
    @BFieldNotProcess
    java.util.Map<?,?> mProviderMap();
    @BFieldSetNotProcess
    void _set_mLocalProvidersByName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLocalProvidersByName();
    @BFieldNotProcess
    java.util.Map<?,?> mLocalProvidersByName();
}

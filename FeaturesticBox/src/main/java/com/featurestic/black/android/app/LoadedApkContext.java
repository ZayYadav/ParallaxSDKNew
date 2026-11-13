package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkContext {
    @BMethodCheckNotProcess
    Method _check_getResources();
    java.lang.Object getResources();
    @BMethodCheckNotProcess
    Method _check_forgetServiceDispatcher(android.content.Context Context0, android.content.ServiceConnection ServiceConnection1);
    android.app.IServiceConnection forgetServiceDispatcher(android.content.Context Context0, android.content.ServiceConnection ServiceConnection1);
    @BMethodCheckNotProcess
    Method _check_getClassLoader();
    java.lang.ClassLoader getClassLoader();
    @BMethodCheckNotProcess
    Method _check_getServiceDispatcher(android.content.ServiceConnection ServiceConnection0, android.content.Context Context1, android.os.Handler Handler2, int int3);
    android.app.IServiceConnection getServiceDispatcher(android.content.ServiceConnection ServiceConnection0, android.content.Context Context1, android.os.Handler Handler2, int int3);
    @BMethodCheckNotProcess
    Method _check_makeApplication(boolean boolean0, android.app.Instrumentation Instrumentation1);
    android.app.Application makeApplication(boolean boolean0, android.app.Instrumentation Instrumentation1);
    @BFieldSetNotProcess
    void _set_mApplication(final Object value);
    @BFieldCheckNotProcess
    Field _check_mApplication();
    @BFieldNotProcess
    android.app.Application mApplication();
    @BFieldSetNotProcess
    void _set_mApplicationInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_mApplicationInfo();
    @BFieldNotProcess
    android.content.pm.ApplicationInfo mApplicationInfo();
    @BFieldSetNotProcess
    void _set_mCredentialProtectedDataDirFile(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCredentialProtectedDataDirFile();
    @BFieldNotProcess
    java.io.File mCredentialProtectedDataDirFile();
    @BFieldSetNotProcess
    void _set_mDataDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDataDir();
    @BFieldNotProcess
    java.lang.String mDataDir();
    @BFieldSetNotProcess
    void _set_mDataDirFile(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDataDirFile();
    @BFieldNotProcess
    java.io.File mDataDirFile();
    @BFieldSetNotProcess
    void _set_mDeviceProtectedDataDirFile(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDeviceProtectedDataDirFile();
    @BFieldNotProcess
    java.io.File mDeviceProtectedDataDirFile();
    @BFieldSetNotProcess
    void _set_mLibDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLibDir();
    @BFieldNotProcess
    java.lang.String mLibDir();
    @BFieldSetNotProcess
    void _set_mSecurityViolation(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSecurityViolation();
    @BFieldNotProcess
    java.lang.Boolean mSecurityViolation();
    @BFieldSetNotProcess
    void _set_mPackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackageName();
    @BFieldNotProcess
    java.lang.Boolean mPackageName();
    @BFieldSetNotProcess
    void _set_mAppComponentFactory(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAppComponentFactory();
    @BFieldNotProcess
    java.lang.Object mAppComponentFactory();
}

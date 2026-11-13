package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadStatic {
    @BMethodCheckNotProcess
    Method _check_currentActivityThread();
    java.lang.Object currentActivityThread();
    @BMethodCheckNotProcess
    Method _check_currentApplication();
    android.app.Application currentApplication();
    @BMethodCheckNotProcess
    Method _check_currentPackageName();
    java.lang.String currentPackageName();
    @BFieldSetNotProcess
    void _set_sPackageManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_sPackageManager();
    @BFieldNotProcess
    android.os.IInterface sPackageManager();
    @BFieldSetNotProcess
    void _set_sPermissionManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_sPermissionManager();
    @BFieldNotProcess
    android.os.IInterface sPermissionManager();
}

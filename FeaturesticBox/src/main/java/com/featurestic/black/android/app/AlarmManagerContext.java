package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.AlarmManager")
public interface AlarmManagerContext {
    @BFieldSetNotProcess
    void _set_mService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mService();
    @BFieldNotProcess
    android.os.IInterface mService();
    @BFieldSetNotProcess
    void _set_mTargetSdkVersion(final Object value);
    @BFieldCheckNotProcess
    Field _check_mTargetSdkVersion();
    @BFieldNotProcess
    java.lang.Integer mTargetSdkVersion();
}

package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkICSContext {
    @BFieldSetNotProcess
    void _set_mCompatibilityInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCompatibilityInfo();
    @BFieldNotProcess
    java.lang.Object mCompatibilityInfo();
}

package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkKitkatContext {
    @BFieldSetNotProcess
    void _set_mDisplayAdjustments(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDisplayAdjustments();
    @BFieldNotProcess
    java.lang.Object mDisplayAdjustments();
}

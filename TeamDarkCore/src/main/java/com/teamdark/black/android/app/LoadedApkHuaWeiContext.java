package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk")
public interface LoadedApkHuaWeiContext {
    @BFieldSetNotProcess
    void _set_mReceiverResource(final Object value);
    @BFieldCheckNotProcess
    Field _check_mReceiverResource();
    @BFieldNotProcess
    java.lang.Object mReceiverResource();
}

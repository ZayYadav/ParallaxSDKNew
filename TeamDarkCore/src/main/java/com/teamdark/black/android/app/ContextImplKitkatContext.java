package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplKitkatContext {
    @BFieldSetNotProcess
    void _set_mDisplayAdjustments(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDisplayAdjustments();
    @BFieldNotProcess
    java.lang.Object mDisplayAdjustments();
    @BFieldSetNotProcess
    void _set_mExternalCacheDirs(final Object value);
    @BFieldCheckNotProcess
    Field _check_mExternalCacheDirs();
    @BFieldNotProcess
    java.io.File[] mExternalCacheDirs();
    @BFieldSetNotProcess
    void _set_mExternalFilesDirs(final Object value);
    @BFieldCheckNotProcess
    Field _check_mExternalFilesDirs();
    @BFieldNotProcess
    java.io.File[] mExternalFilesDirs();
    @BFieldSetNotProcess
    void _set_mOpPackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mOpPackageName();
    @BFieldNotProcess
    java.lang.String mOpPackageName();
}

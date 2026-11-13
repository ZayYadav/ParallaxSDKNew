package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplICSContext {
    @BFieldSetNotProcess
    void _set_mExternalCacheDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_mExternalCacheDir();
    @BFieldNotProcess
    java.io.File mExternalCacheDir();
    @BFieldSetNotProcess
    void _set_mExternalFilesDir(final Object value);
    @BFieldCheckNotProcess
    Field _check_mExternalFilesDir();
    @BFieldNotProcess
    java.io.File mExternalFilesDir();
}

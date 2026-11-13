package com.featurestic.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.WindowManagerGlobal")
public interface WindowManagerGlobalStatic {
    @BFieldSetNotProcess
    void _set_ADD_PERMISSION_DENIED(final Object value);
    @BFieldCheckNotProcess
    Field _check_ADD_PERMISSION_DENIED();
    @BFieldNotProcess
    java.lang.Integer ADD_PERMISSION_DENIED();
    @BFieldSetNotProcess
    void _set_sWindowManagerService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sWindowManagerService();
    @BFieldNotProcess
    android.os.IInterface sWindowManagerService();
}

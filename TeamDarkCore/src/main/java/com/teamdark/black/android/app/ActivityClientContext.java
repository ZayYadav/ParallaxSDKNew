package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityClient")
public interface ActivityClientContext {
    @BFieldSetNotProcess
    void _set_INTERFACE_SINGLETON(final Object value);
    @BFieldCheckNotProcess
    Field _check_INTERFACE_SINGLETON();
    @BFieldNotProcess
    java.lang.Object INTERFACE_SINGLETON();
}

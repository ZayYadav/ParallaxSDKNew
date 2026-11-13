package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityClient$ActivityClientControllerSingleton")
public interface ActivityClientActivityClientControllerSingletonContext {
    @BFieldSetNotProcess
    void _set_mKnownInstance(final Object value);
    @BFieldCheckNotProcess
    Field _check_mKnownInstance();
    @BFieldNotProcess
    android.os.IInterface mKnownInstance();
}

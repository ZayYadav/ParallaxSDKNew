package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityManagerNative")
public interface ActivityManagerNativeStatic {
    @BMethodCheckNotProcess
    Method _check_getDefault();
    android.os.IInterface getDefault();
    @BFieldSetNotProcess
    void _set_gDefault(final Object value);
    @BFieldCheckNotProcess
    Field _check_gDefault();
    @BFieldNotProcess
    java.lang.Object gDefault();
}

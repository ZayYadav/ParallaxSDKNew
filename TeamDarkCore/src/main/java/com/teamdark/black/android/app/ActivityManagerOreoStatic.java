package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityManager")
public interface ActivityManagerOreoStatic {
    @BMethodCheckNotProcess
    Method _check_getService();
    android.os.IInterface getService();
    @BFieldSetNotProcess
    void _set_IActivityManagerSingleton(final Object value);
    @BFieldCheckNotProcess
    Field _check_IActivityManagerSingleton();
    @BFieldNotProcess
    java.lang.Object IActivityManagerSingleton();
}

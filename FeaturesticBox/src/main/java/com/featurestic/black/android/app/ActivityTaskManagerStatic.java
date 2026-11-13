package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityTaskManager")
public interface ActivityTaskManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getService();
    java.lang.Object getService();
    @BFieldSetNotProcess
    void _set_IActivityTaskManagerSingleton(final Object value);
    @BFieldCheckNotProcess
    Field _check_IActivityTaskManagerSingleton();
    @BFieldNotProcess
    java.lang.Object IActivityTaskManagerSingleton();
}

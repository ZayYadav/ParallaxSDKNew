package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityClient")
public interface ActivityClientStatic {
    @BMethodCheckNotProcess
    Method _check_getInstance();
    java.lang.Object getInstance();
    @BMethodCheckNotProcess
    Method _check_getActivityClientController();
    java.lang.Object getActivityClientController();
    @BMethodCheckNotProcess
    Method _check_setActivityClientController(java.lang.Object iInterface);
    java.lang.Object setActivityClientController(java.lang.Object iInterface);
}

package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$CreateServiceData")
public interface ActivityThreadCreateServiceDataContext {
    @BFieldSetNotProcess
    void _set_compatInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_compatInfo();
    @BFieldNotProcess
    java.lang.Object compatInfo();
    @BFieldSetNotProcess
    void _set_info(final Object value);
    @BFieldCheckNotProcess
    Field _check_info();
    @BFieldNotProcess
    android.content.pm.ServiceInfo info();
    @BFieldSetNotProcess
    void _set_intent(final Object value);
    @BFieldCheckNotProcess
    Field _check_intent();
    @BFieldNotProcess
    android.content.Intent intent();
    @BFieldSetNotProcess
    void _set_token(final Object value);
    @BFieldCheckNotProcess
    Field _check_token();
    @BFieldNotProcess
    android.os.IBinder token();
}

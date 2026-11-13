package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$AppBindData")
public interface ActivityThreadAppBindDataContext {
    @BFieldSetNotProcess
    void _set_appInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_appInfo();
    @BFieldNotProcess
    android.content.pm.ApplicationInfo appInfo();
    @BFieldSetNotProcess
    void _set_info(final Object value);
    @BFieldCheckNotProcess
    Field _check_info();
    @BFieldNotProcess
    java.lang.Object info();
    @BFieldSetNotProcess
    void _set_instrumentationName(final Object value);
    @BFieldCheckNotProcess
    Field _check_instrumentationName();
    @BFieldNotProcess
    android.content.ComponentName instrumentationName();
    @BFieldSetNotProcess
    void _set_processName(final Object value);
    @BFieldCheckNotProcess
    Field _check_processName();
    @BFieldNotProcess
    java.lang.String processName();
    @BFieldSetNotProcess
    void _set_providers(final Object value);
    @BFieldCheckNotProcess
    Field _check_providers();
    @BFieldNotProcess
    java.util.List<android.content.pm.ProviderInfo> providers();
}

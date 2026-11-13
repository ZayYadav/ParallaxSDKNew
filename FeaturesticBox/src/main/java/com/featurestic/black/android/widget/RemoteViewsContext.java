package com.featurestic.black.android.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.widget.RemoteViews")
public interface RemoteViewsContext {
    @BFieldSetNotProcess
    void _set_mActions(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActions();
    @BFieldNotProcess
    java.util.ArrayList<java.lang.Object> mActions();
    @BFieldSetNotProcess
    void _set_mApplication(final Object value);
    @BFieldCheckNotProcess
    Field _check_mApplication();
    @BFieldNotProcess
    android.content.pm.ApplicationInfo mApplication();
    @BFieldSetNotProcess
    void _set_mPackage(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackage();
    @BFieldNotProcess
    java.lang.String mPackage();
}

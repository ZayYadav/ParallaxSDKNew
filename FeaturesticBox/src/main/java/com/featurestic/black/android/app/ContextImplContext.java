package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplContext {
    @BMethodCheckNotProcess
    Method _check_getReceiverRestrictedContext();
    android.content.Context getReceiverRestrictedContext();
    @BMethodCheckNotProcess
    Method _check_setOuterContext(android.content.Context Context0);
    java.lang.Void setOuterContext(android.content.Context Context0);
    @BMethodCheckNotProcess
    Method _check_getAttributionSource();
    java.lang.Object getAttributionSource();
    @BMethodCheckNotProcess
    Method _check_getPackageManager();
    android.content.pm.PackageManager getPackageManager();
    @BFieldSetNotProcess
    void _set_mBasePackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBasePackageName();
    @BFieldNotProcess
    java.lang.String mBasePackageName();
    @BFieldSetNotProcess
    void _set_mContentResolver(final Object value);
    @BFieldCheckNotProcess
    Field _check_mContentResolver();
    @BFieldNotProcess
    android.content.ContentResolver mContentResolver();
    @BFieldSetNotProcess
    void _set_mPackageInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackageInfo();
    @BFieldNotProcess
    java.lang.Object mPackageInfo();
    @BFieldSetNotProcess
    void _set_mPackageManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackageManager();
    @BFieldNotProcess
    android.content.pm.PackageManager mPackageManager();
}

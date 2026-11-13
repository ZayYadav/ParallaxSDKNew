package com.featurestic.black.android.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.provider.Settings$NameValueCache")
public interface SettingsNameValueCacheContext {
    @BFieldSetNotProcess
    void _set_mContentProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mContentProvider();
    @BFieldNotProcess
    java.lang.Object mContentProvider();
}

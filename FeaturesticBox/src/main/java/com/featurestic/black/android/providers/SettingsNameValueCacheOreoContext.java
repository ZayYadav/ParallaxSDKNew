package com.featurestic.black.android.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.provider.Settings$NameValueCache")
public interface SettingsNameValueCacheOreoContext {
    @BFieldSetNotProcess
    void _set_mProviderHolder(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProviderHolder();
    @BFieldNotProcess
    java.lang.Object mProviderHolder();
}

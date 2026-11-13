package com.featurestic.black.android.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.provider.Settings$Secure")
public interface SettingsSecureStatic {
    @BFieldSetNotProcess
    void _set_sNameValueCache(final Object value);
    @BFieldCheckNotProcess
    Field _check_sNameValueCache();
    @BFieldNotProcess
    java.lang.Object sNameValueCache();
}

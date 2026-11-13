package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser$Component")
public interface PackageParserComponentContext {
    @BFieldSetNotProcess
    void _set_className(final Object value);
    @BFieldCheckNotProcess
    Field _check_className();
    @BFieldNotProcess
    java.lang.String className();
    @BFieldSetNotProcess
    void _set_componentName(final Object value);
    @BFieldCheckNotProcess
    Field _check_componentName();
    @BFieldNotProcess
    android.content.ComponentName componentName();
    @BFieldSetNotProcess
    void _set_intents(final Object value);
    @BFieldCheckNotProcess
    Field _check_intents();
    @BFieldNotProcess
    java.util.List<android.content.IntentFilter> intents();
}

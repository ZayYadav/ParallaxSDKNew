package com.featurestic.black.android.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.util.Singleton")
public interface SingletonContext {
    @BMethodCheckNotProcess
    Method _check_get();
    java.lang.Object get();
    @BFieldSetNotProcess
    void _set_mInstance(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInstance();
    @BFieldNotProcess
    java.lang.Object mInstance();
}

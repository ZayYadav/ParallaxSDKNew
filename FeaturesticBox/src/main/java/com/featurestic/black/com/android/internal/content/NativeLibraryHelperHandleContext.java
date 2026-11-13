package com.featurestic.black.com.android.internal.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.content.NativeLibraryHelper$Handle")
public interface NativeLibraryHelperHandleContext {
    @BFieldSetNotProcess
    void _set_extractNativeLibs(final Object value);
    @BFieldCheckNotProcess
    Field _check_extractNativeLibs();
    @BFieldNotProcess
    java.lang.Boolean extractNativeLibs();
}

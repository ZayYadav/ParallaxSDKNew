package com.featurestic.black.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.BaseDexClassLoader")
public interface BaseDexClassLoaderContext {
    @BFieldSetNotProcess
    void _set_sharedLibraryLoaders(final Object value);
    @BFieldCheckNotProcess
    Field _check_sharedLibraryLoaders();
    @BFieldNotProcess
    java.lang.Object sharedLibraryLoaders();
}

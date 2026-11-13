package com.teamdark.black.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.BaseDexClassLoader")
public interface BaseDexClassLoaderContext {
    @BFieldSetNotProcess
    void _set_sharedLibraryLoaders(final Object value);
    @BFieldCheckNotProcess
    Field _check_sharedLibraryLoaders();
    @BFieldNotProcess
    java.lang.Object sharedLibraryLoaders();
}

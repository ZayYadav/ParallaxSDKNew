package com.featurestic.black.java.io;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("java.io.File")
public interface FileStatic {
    @BFieldSetNotProcess
    void _set_fs(final Object value);
    @BFieldCheckNotProcess
    Field _check_fs();
    @BFieldNotProcess
    java.lang.Object fs();
}

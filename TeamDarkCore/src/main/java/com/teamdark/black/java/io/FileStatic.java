package com.teamdark.black.java.io;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("java.io.File")
public interface FileStatic {
    @BFieldSetNotProcess
    void _set_fs(final Object value);
    @BFieldCheckNotProcess
    Field _check_fs();
    @BFieldNotProcess
    java.lang.Object fs();
}

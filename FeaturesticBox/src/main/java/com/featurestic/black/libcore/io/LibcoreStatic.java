package com.featurestic.black.libcore.io;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("libcore.io.Libcore")
public interface LibcoreStatic {
    @BFieldSetNotProcess
    void _set_os(final Object value);
    @BFieldCheckNotProcess
    Field _check_os();
    @BFieldNotProcess
    java.lang.Object os();
}

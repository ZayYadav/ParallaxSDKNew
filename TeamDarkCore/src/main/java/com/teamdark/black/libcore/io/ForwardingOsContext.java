package com.teamdark.black.libcore.io;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("libcore.io.ForwardingOs")
public interface ForwardingOsContext {
    @BFieldSetNotProcess
    void _set_os(final Object value);
    @BFieldCheckNotProcess
    Field _check_os();
    @BFieldNotProcess
    java.lang.Object os();
}

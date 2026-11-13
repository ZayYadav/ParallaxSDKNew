package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ServiceStartArgs")
public interface ServiceStartArgsStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.ServiceStartArgs _new(boolean boolean0, int int1, int int2, android.content.Intent Intent3);
}

package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ServiceStartArgs")
public interface ServiceStartArgsContext {
    @BFieldSetNotProcess
    void _set_args(final Object value);
    @BFieldCheckNotProcess
    Field _check_args();
    @BFieldNotProcess
    android.content.Intent args();
    @BFieldSetNotProcess
    void _set_flags(final Object value);
    @BFieldCheckNotProcess
    Field _check_flags();
    @BFieldNotProcess
    java.lang.Integer flags();
    @BFieldSetNotProcess
    void _set_startId(final Object value);
    @BFieldCheckNotProcess
    Field _check_startId();
    @BFieldNotProcess
    java.lang.Integer startId();
    @BFieldSetNotProcess
    void _set_taskRemoved(final Object value);
    @BFieldCheckNotProcess
    Field _check_taskRemoved();
    @BFieldNotProcess
    java.lang.Boolean taskRemoved();
}

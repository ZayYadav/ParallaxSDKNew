package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.AttributionSourceState")
public interface AttributionSourceStateContext {
    @BFieldSetNotProcess
    void _set_packageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_packageName();
    @BFieldNotProcess
    java.lang.String packageName();
    @BFieldSetNotProcess
    void _set_pid(final Object value);
    @BFieldCheckNotProcess
    Field _check_pid();
    @BFieldNotProcess
    java.lang.Integer pid();
    @BFieldSetNotProcess
    void _set_uid(final Object value);
    @BFieldCheckNotProcess
    Field _check_uid();
    @BFieldNotProcess
    java.lang.Integer uid();
}

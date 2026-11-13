package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ParceledListSlice")
public interface ParceledListSliceStatic {
    @BFieldSetNotProcess
    void _set_CREATOR(final Object value);
    @BFieldCheckNotProcess
    Field _check_CREATOR();
    @BFieldNotProcess
    android.os.Parcelable.Creator CREATOR();
    @BConstructorNotProcess
    java.lang.Object _new();
    @BConstructorNotProcess
    java.lang.Object _new(java.util.List<?> List0);
}

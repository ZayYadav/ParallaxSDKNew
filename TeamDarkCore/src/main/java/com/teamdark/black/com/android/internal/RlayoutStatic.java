package com.teamdark.black.com.android.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.R$layout")
public interface RlayoutStatic {
    @BFieldSetNotProcess
    void _set_resolver_list(final Object value);
    @BFieldCheckNotProcess
    Field _check_resolver_list();
    @BFieldNotProcess
    java.lang.Integer resolver_list();
}

package com.featurestic.black.java.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("java.lang.ThreadGroup")
public interface ThreadGroupNContext {
    @BFieldSetNotProcess
    void _set_groups(final Object value);
    @BFieldCheckNotProcess
    Field _check_groups();
    @BFieldNotProcess
    java.lang.ThreadGroup[] groups();
    @BFieldSetNotProcess
    void _set_ngroups(final Object value);
    @BFieldCheckNotProcess
    Field _check_ngroups();
    @BFieldNotProcess
    java.lang.Integer ngroups();
    @BFieldSetNotProcess
    void _set_parent(final Object value);
    @BFieldCheckNotProcess
    Field _check_parent();
    @BFieldNotProcess
    java.lang.ThreadGroup parent();
}

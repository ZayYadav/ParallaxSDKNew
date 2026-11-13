package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Environment")
public interface EnvironmentStatic {
    @BFieldSetNotProcess
    void _set_sCurrentUser(final Object value);
    @BFieldCheckNotProcess
    Field _check_sCurrentUser();
    @BFieldNotProcess
    java.lang.Object sCurrentUser();
}

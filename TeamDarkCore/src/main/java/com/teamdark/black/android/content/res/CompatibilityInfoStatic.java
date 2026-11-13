package com.teamdark.black.android.content.res;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.res.CompatibilityInfo")
public interface CompatibilityInfoStatic {
    @BFieldSetNotProcess
    void _set_DEFAULT_COMPATIBILITY_INFO(final Object value);
    @BFieldCheckNotProcess
    Field _check_DEFAULT_COMPATIBILITY_INFO();
    @BFieldNotProcess
    java.lang.Object DEFAULT_COMPATIBILITY_INFO();
    @BConstructorNotProcess
    com.teamdark.black.android.content.res.CompatibilityInfo _new(android.content.pm.ApplicationInfo ApplicationInfo0, int int1, int int2, boolean boolean3);
    @BConstructorNotProcess
    com.teamdark.black.android.content.res.CompatibilityInfo _new(android.content.pm.ApplicationInfo ApplicationInfo0, int int1, int int2, boolean boolean3, int int4);
}

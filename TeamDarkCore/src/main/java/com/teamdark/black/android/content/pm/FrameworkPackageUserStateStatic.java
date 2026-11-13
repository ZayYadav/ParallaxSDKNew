package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.pkg.FrameworkPackageUserState")
public interface FrameworkPackageUserStateStatic {
    @BFieldSetNotProcess
    void _set_gDefault(final Object value);
    @BFieldCheckNotProcess
    Field _check_gDefault();
    @BFieldNotProcess
    java.lang.Object gDefault();
}

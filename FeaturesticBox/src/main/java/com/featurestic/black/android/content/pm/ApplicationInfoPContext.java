package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ApplicationInfo")
public interface ApplicationInfoPContext {
    @BMethodCheckNotProcess
    Method _check_setHiddenApiEnforcementPolicy(int int0);
    java.lang.Void setHiddenApiEnforcementPolicy(int int0);
    @BFieldSetNotProcess
    void _set_splitClassLoaderNames(final Object value);
    @BFieldCheckNotProcess
    Field _check_splitClassLoaderNames();
    @BFieldNotProcess
    java.lang.String[] splitClassLoaderNames();
}

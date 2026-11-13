package com.featurestic.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser$Provider")
public interface PackageParserProviderContext {
    @BFieldSetNotProcess
    void _set_info(final Object value);
    @BFieldCheckNotProcess
    Field _check_info();
    @BFieldNotProcess
    android.content.pm.ProviderInfo info();
}

package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ContentResolver")
public interface ContentResolverContext {
    @BFieldSetNotProcess
    void _set_mPackageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mPackageName();
    @BFieldNotProcess
    java.lang.String mPackageName();
}

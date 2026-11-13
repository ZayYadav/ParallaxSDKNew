package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ContentProviderClient")
public interface ContentProviderClientContext {
    @BFieldSetNotProcess
    void _set_mContentProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mContentProvider();
    @BFieldNotProcess
    android.os.IInterface mContentProvider();
}

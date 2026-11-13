package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ContentResolver")
public interface ContentResolverStatic {
    @BFieldSetNotProcess
    void _set_sContentService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sContentService();
    @BFieldNotProcess
    android.os.IInterface sContentService();
}

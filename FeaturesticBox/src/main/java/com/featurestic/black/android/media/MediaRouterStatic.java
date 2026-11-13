package com.featurestic.black.android.media;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.media.MediaRouter")
public interface MediaRouterStatic {
    @BMethodCheckNotProcess
    Method _check_getInstance(android.content.Context context);
    java.lang.Object getInstance(android.content.Context context);
    @BFieldSetNotProcess
    void _set_sStatic(final Object value);
    @BFieldCheckNotProcess
    Field _check_sStatic();
    @BFieldNotProcess
    java.lang.Object sStatic();
}

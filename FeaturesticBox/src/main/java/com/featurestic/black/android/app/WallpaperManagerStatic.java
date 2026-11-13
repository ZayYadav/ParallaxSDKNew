package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.WallpaperManager")
public interface WallpaperManagerStatic {
    @BFieldSetNotProcess
    void _set_sGlobals(final Object value);
    @BFieldCheckNotProcess
    Field _check_sGlobals();
    @BFieldNotProcess
    java.lang.Object sGlobals();
}

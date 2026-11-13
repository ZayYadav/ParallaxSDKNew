package com.featurestic.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.renderscript.RenderScript")
public interface RenderScriptStatic {
    @BMethodCheckNotProcess
    Method _check_setupDiskCache(java.io.File File0);
    java.lang.Void setupDiskCache(java.io.File File0);
}

package com.featurestic.black.android.renderscript;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.renderscript.RenderScriptCacheDir")
public interface RenderScriptCacheDirStatic {
    @BMethodCheckNotProcess
    Method _check_setupDiskCache(java.io.File File0);
    java.lang.Void setupDiskCache(java.io.File File0);
}

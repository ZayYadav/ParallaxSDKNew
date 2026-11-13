package com.teamdark.black.android.content.res;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.res.AssetManager")
public interface AssetManagerContext {
    @BMethodCheckNotProcess
    Method _check_addAssetPath(java.lang.String String0);
    java.lang.Integer addAssetPath(java.lang.String String0);
    @BMethodCheckNotProcess
    Method _check_getConfiguration();
    android.content.res.Configuration getConfiguration();
    @BMethodCheckNotProcess
    Method _check_getDisplayMetrics();
    android.util.DisplayMetrics getDisplayMetrics();
}

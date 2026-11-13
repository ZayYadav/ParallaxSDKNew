package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ContextImpl")
public interface ContextImplStatic {
    @BMethodCheckNotProcess
    Method _check_createAppContext(@BParamClassName("android.app.ActivityThread") java.lang.Object mainThread, @BParamClassName("android.app.LoadedApk") java.lang.Object packageInfo);
    java.lang.Object createAppContext(@BParamClassName("android.app.ActivityThread") java.lang.Object mainThread, @BParamClassName("android.app.LoadedApk") java.lang.Object packageInfo);
}

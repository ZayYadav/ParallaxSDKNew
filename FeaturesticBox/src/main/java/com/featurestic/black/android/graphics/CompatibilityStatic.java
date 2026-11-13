package com.featurestic.black.android.graphics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.graphics.Compatibility")
public interface CompatibilityStatic {
    @BMethodCheckNotProcess
    Method _check_setTargetSdkVersion(int targetSdkVersion);
    java.lang.Void setTargetSdkVersion(int targetSdkVersion);
}

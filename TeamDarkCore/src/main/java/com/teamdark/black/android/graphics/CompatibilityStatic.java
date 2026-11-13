package com.teamdark.black.android.graphics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.graphics.Compatibility")
public interface CompatibilityStatic {
    @BMethodCheckNotProcess
    Method _check_setTargetSdkVersion(int targetSdkVersion);
    java.lang.Void setTargetSdkVersion(int targetSdkVersion);
}

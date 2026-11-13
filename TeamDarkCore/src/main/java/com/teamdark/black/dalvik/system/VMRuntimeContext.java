package com.teamdark.black.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.VMRuntime")
public interface VMRuntimeContext {
    @BMethodCheckNotProcess
    Method _check_is64Bit();
    java.lang.Boolean is64Bit();
    @BMethodCheckNotProcess
    Method _check_isJavaDebuggable();
    java.lang.Boolean isJavaDebuggable();
    @BMethodCheckNotProcess
    Method _check_setTargetSdkVersion(int int0);
    java.lang.Void setTargetSdkVersion(int int0);
}

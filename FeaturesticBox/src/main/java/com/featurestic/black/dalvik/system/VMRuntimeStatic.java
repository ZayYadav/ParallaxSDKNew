package com.featurestic.black.dalvik.system;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("dalvik.system.VMRuntime")
public interface VMRuntimeStatic {
    @BMethodCheckNotProcess
    Method _check_getCurrentInstructionSet();
    java.lang.String getCurrentInstructionSet();
    @BMethodCheckNotProcess
    Method _check_getRuntime();
    java.lang.Object getRuntime();
    @BMethodCheckNotProcess
    Method _check_is64BitAbi(java.lang.String String0);
    java.lang.Boolean is64BitAbi(java.lang.String String0);
}

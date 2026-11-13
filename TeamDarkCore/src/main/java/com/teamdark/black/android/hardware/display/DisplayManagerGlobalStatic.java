package com.teamdark.black.android.hardware.display;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.hardware.display.DisplayManagerGlobal")
public interface DisplayManagerGlobalStatic {
    @BMethodCheckNotProcess
    Method _check_getInstance();
    java.lang.Object getInstance();
}

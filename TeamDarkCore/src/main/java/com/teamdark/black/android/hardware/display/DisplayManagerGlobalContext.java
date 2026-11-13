package com.teamdark.black.android.hardware.display;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.hardware.display.DisplayManagerGlobal")
public interface DisplayManagerGlobalContext {
    @BFieldSetNotProcess
    void _set_mDm(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDm();
    @BFieldNotProcess
    android.os.IInterface mDm();
}

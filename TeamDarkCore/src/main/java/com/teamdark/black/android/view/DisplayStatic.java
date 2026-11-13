package com.teamdark.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.Display")
public interface DisplayStatic {
    @BFieldSetNotProcess
    void _set_sWindowManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_sWindowManager();
    @BFieldNotProcess
    android.os.IInterface sWindowManager();
}

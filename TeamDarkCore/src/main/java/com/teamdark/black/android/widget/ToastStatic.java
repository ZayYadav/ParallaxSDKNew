package com.teamdark.black.android.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.widget.Toast")
public interface ToastStatic {
    @BFieldSetNotProcess
    void _set_sService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sService();
    @BFieldNotProcess
    android.os.IInterface sService();
}

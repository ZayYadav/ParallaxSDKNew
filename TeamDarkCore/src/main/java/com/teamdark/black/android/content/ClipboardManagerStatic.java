package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ClipboardManager")
public interface ClipboardManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getService();
    android.os.IInterface getService();
    @BFieldSetNotProcess
    void _set_sService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sService();
    @BFieldNotProcess
    android.os.IInterface sService();
}

package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ClipboardManager")
public interface ClipboardManagerOreoStatic {
    @BFieldSetNotProcess
    void _set_sService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sService();
    @BFieldNotProcess
    android.os.IInterface sService();
}

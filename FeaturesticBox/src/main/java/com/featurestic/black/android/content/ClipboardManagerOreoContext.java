package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.ClipboardManager")
public interface ClipboardManagerOreoContext {
    @BFieldSetNotProcess
    void _set_mService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mService();
    @BFieldNotProcess
    android.os.IInterface mService();
}

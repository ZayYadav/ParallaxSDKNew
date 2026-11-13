package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Handler")
public interface HandlerContext {
    @BFieldSetNotProcess
    void _set_mCallback(final Object value);
    @BFieldCheckNotProcess
    Field _check_mCallback();
    @BFieldNotProcess
    android.os.Handler.Callback mCallback();
}

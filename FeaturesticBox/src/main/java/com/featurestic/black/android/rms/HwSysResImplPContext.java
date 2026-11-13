package com.featurestic.black.android.rms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.rms.HwSysResImpl")
public interface HwSysResImplPContext {
    @BFieldSetNotProcess
    void _set_mWhiteListMap(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWhiteListMap();
    @BFieldNotProcess
    java.util.Map<java.lang.Integer,java.util.ArrayList<java.lang.String>> mWhiteListMap();
}

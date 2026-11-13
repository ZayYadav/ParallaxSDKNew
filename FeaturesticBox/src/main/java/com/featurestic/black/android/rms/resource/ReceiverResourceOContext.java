package com.featurestic.black.android.rms.resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.rms.resource.ReceiverResource")
public interface ReceiverResourceOContext {
    @BFieldSetNotProcess
    void _set_mWhiteListMap(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWhiteListMap();
    @BFieldNotProcess
    java.util.Map<java.lang.Integer,java.util.List<java.lang.String>> mWhiteListMap();
}

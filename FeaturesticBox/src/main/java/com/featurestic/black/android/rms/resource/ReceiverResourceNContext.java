package com.featurestic.black.android.rms.resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.rms.resource.ReceiverResource")
public interface ReceiverResourceNContext {
    @BFieldSetNotProcess
    void _set_mWhiteList(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWhiteList();
    @BFieldNotProcess
    java.util.List<java.lang.String> mWhiteList();
}

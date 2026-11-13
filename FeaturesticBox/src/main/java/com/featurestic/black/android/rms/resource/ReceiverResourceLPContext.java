package com.featurestic.black.android.rms.resource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.rms.resource.ReceiverResource")
public interface ReceiverResourceLPContext {
    @BFieldSetNotProcess
    void _set_mResourceConfig(final Object value);
    @BFieldCheckNotProcess
    Field _check_mResourceConfig();
    @BFieldNotProcess
    java.lang.Object mResourceConfig();
}

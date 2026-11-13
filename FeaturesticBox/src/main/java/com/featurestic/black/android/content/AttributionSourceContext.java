package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.AttributionSource")
public interface AttributionSourceContext {
    @BMethodCheckNotProcess
    Method _check_getNext();
    java.lang.Object getNext();
    @BFieldSetNotProcess
    void _set_mAttributionSourceState(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAttributionSourceState();
    @BFieldNotProcess
    java.lang.Object mAttributionSourceState();
}

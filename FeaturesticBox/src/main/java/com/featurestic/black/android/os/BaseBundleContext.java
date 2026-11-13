package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.BaseBundle")
public interface BaseBundleContext {
    @BFieldSetNotProcess
    void _set_mParcelledData(final Object value);
    @BFieldCheckNotProcess
    Field _check_mParcelledData();
    @BFieldNotProcess
    android.os.Parcel mParcelledData();
}

package com.featurestic.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Parcel")
public interface ParcelStatic {
    @BFieldSetNotProcess
    void _set_VAL_PARCELABLE(final Object value);
    @BFieldCheckNotProcess
    Field _check_VAL_PARCELABLE();
    @BFieldNotProcess
    java.lang.Integer VAL_PARCELABLE();
    @BFieldSetNotProcess
    void _set_VAL_PARCELABLEARRAY(final Object value);
    @BFieldCheckNotProcess
    Field _check_VAL_PARCELABLEARRAY();
    @BFieldNotProcess
    java.lang.Integer VAL_PARCELABLEARRAY();
}

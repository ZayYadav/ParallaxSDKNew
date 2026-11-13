package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationRequest")
public interface LocationRequestLContext {
    @BMethodCheckNotProcess
    Method _check_getProvider();
    java.lang.String getProvider();
    @BFieldSetNotProcess
    void _set_mHideFromAppOps(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHideFromAppOps();
    @BFieldNotProcess
    java.lang.Boolean mHideFromAppOps();
    @BFieldSetNotProcess
    void _set_mProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProvider();
    @BFieldNotProcess
    java.lang.String mProvider();
    @BFieldSetNotProcess
    void _set_mWorkSource(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWorkSource();
    @BFieldNotProcess
    java.lang.Object mWorkSource();
}

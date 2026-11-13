package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordPContext {
    @BFieldSetNotProcess
    void _set_mNames(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNames();
    @BFieldNotProcess
    java.lang.String[] mNames();
    @BFieldSetNotProcess
    void _set_mProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProvider();
    @BFieldNotProcess
    android.os.IInterface mProvider();
}

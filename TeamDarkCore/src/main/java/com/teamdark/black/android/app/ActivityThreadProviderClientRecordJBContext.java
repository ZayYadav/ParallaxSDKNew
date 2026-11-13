package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordJBContext {
    @BFieldSetNotProcess
    void _set_mHolder(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHolder();
    @BFieldNotProcess
    java.lang.Object mHolder();
    @BFieldSetNotProcess
    void _set_mProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProvider();
    @BFieldNotProcess
    android.os.IInterface mProvider();
}

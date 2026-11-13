package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordContext {
    @BFieldSetNotProcess
    void _set_mName(final Object value);
    @BFieldCheckNotProcess
    Field _check_mName();
    @BFieldNotProcess
    java.lang.String mName();
    @BFieldSetNotProcess
    void _set_mProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mProvider();
    @BFieldNotProcess
    android.os.IInterface mProvider();
}

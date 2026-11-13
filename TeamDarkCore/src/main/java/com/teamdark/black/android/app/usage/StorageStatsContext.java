package com.teamdark.black.android.app.usage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.usage.StorageStats")
public interface StorageStatsContext {
    @BFieldSetNotProcess
    void _set_cacheBytes(final Object value);
    @BFieldCheckNotProcess
    Field _check_cacheBytes();
    @BFieldNotProcess
    java.lang.Long cacheBytes();
    @BFieldSetNotProcess
    void _set_codeBytes(final Object value);
    @BFieldCheckNotProcess
    Field _check_codeBytes();
    @BFieldNotProcess
    java.lang.Long codeBytes();
    @BFieldSetNotProcess
    void _set_dataBytes(final Object value);
    @BFieldCheckNotProcess
    Field _check_dataBytes();
    @BFieldNotProcess
    java.lang.Long dataBytes();
}

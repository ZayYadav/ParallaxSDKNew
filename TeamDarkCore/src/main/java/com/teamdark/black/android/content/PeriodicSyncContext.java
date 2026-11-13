package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.PeriodicSync")
public interface PeriodicSyncContext {
    @BFieldSetNotProcess
    void _set_flexTime(final Object value);
    @BFieldCheckNotProcess
    Field _check_flexTime();
    @BFieldNotProcess
    java.lang.Long flexTime();
}

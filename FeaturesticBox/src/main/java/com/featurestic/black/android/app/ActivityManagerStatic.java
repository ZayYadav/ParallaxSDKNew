package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityManager")
public interface ActivityManagerStatic {
    @BFieldSetNotProcess
    void _set_START_INTENT_NOT_RESOLVED(final Object value);
    @BFieldCheckNotProcess
    Field _check_START_INTENT_NOT_RESOLVED();
    @BFieldNotProcess
    java.lang.Integer START_INTENT_NOT_RESOLVED();
    @BFieldSetNotProcess
    void _set_START_NOT_CURRENT_USER_ACTIVITY(final Object value);
    @BFieldCheckNotProcess
    Field _check_START_NOT_CURRENT_USER_ACTIVITY();
    @BFieldNotProcess
    java.lang.Integer START_NOT_CURRENT_USER_ACTIVITY();
    @BFieldSetNotProcess
    void _set_START_SUCCESS(final Object value);
    @BFieldCheckNotProcess
    Field _check_START_SUCCESS();
    @BFieldNotProcess
    java.lang.Integer START_SUCCESS();
    @BFieldSetNotProcess
    void _set_START_TASK_TO_FRONT(final Object value);
    @BFieldCheckNotProcess
    Field _check_START_TASK_TO_FRONT();
    @BFieldNotProcess
    java.lang.Integer START_TASK_TO_FRONT();
}

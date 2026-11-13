package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$H")
public interface ActivityThreadHStatic {
    @BFieldSetNotProcess
    void _set_CREATE_SERVICE(final Object value);
    @BFieldCheckNotProcess
    Field _check_CREATE_SERVICE();
    @BFieldNotProcess
    java.lang.Integer CREATE_SERVICE();
    @BFieldSetNotProcess
    void _set_EXECUTE_TRANSACTION(final Object value);
    @BFieldCheckNotProcess
    Field _check_EXECUTE_TRANSACTION();
    @BFieldNotProcess
    java.lang.Integer EXECUTE_TRANSACTION();
    @BFieldSetNotProcess
    void _set_LAUNCH_ACTIVITY(final Object value);
    @BFieldCheckNotProcess
    Field _check_LAUNCH_ACTIVITY();
    @BFieldNotProcess
    java.lang.Integer LAUNCH_ACTIVITY();
    @BFieldSetNotProcess
    void _set_SCHEDULE_CRASH(final Object value);
    @BFieldCheckNotProcess
    Field _check_SCHEDULE_CRASH();
    @BFieldNotProcess
    java.lang.Integer SCHEDULE_CRASH();
}

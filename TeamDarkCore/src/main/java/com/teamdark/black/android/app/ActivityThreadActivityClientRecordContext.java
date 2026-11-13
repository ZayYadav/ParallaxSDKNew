package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$ActivityClientRecord")
public interface ActivityThreadActivityClientRecordContext {
    @BFieldSetNotProcess
    void _set_activity(final Object value);
    @BFieldCheckNotProcess
    Field _check_activity();
    @BFieldNotProcess
    android.app.Activity activity();
    @BFieldSetNotProcess
    void _set_activityInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_activityInfo();
    @BFieldNotProcess
    android.content.pm.ActivityInfo activityInfo();
    @BFieldSetNotProcess
    void _set_intent(final Object value);
    @BFieldCheckNotProcess
    Field _check_intent();
    @BFieldNotProcess
    android.content.Intent intent();
    @BFieldSetNotProcess
    void _set_isTopResumedActivity(final Object value);
    @BFieldCheckNotProcess
    Field _check_isTopResumedActivity();
    @BFieldNotProcess
    java.lang.Boolean isTopResumedActivity();
    @BFieldSetNotProcess
    void _set_token(final Object value);
    @BFieldCheckNotProcess
    Field _check_token();
    @BFieldNotProcess
    android.os.IBinder token();
    @BFieldSetNotProcess
    void _set_packageInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_packageInfo();
    @BFieldNotProcess
    java.lang.Object packageInfo();
}

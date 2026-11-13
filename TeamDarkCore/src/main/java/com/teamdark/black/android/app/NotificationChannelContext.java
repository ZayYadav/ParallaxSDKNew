package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.NotificationChannel")
public interface NotificationChannelContext {
    @BFieldSetNotProcess
    void _set_mId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mId();
    @BFieldNotProcess
    java.lang.String mId();
}

package com.featurestic.black.android.service.notification;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.service.notification.StatusBarNotification")
public interface StatusBarNotificationContext {
    @BFieldSetNotProcess
    void _set_id(final Object value);
    @BFieldCheckNotProcess
    Field _check_id();
    @BFieldNotProcess
    java.lang.Integer id();
    @BFieldSetNotProcess
    void _set_opPkg(final Object value);
    @BFieldCheckNotProcess
    Field _check_opPkg();
    @BFieldNotProcess
    java.lang.String opPkg();
    @BFieldSetNotProcess
    void _set_pkg(final Object value);
    @BFieldCheckNotProcess
    Field _check_pkg();
    @BFieldNotProcess
    java.lang.String pkg();
    @BFieldSetNotProcess
    void _set_tag(final Object value);
    @BFieldCheckNotProcess
    Field _check_tag();
    @BFieldNotProcess
    java.lang.String tag();
}

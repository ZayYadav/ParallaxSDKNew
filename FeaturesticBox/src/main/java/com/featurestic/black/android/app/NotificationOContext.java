package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationOContext {
    @BFieldSetNotProcess
    void _set_mChannelId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mChannelId();
    @BFieldNotProcess
    java.lang.String mChannelId();
    @BFieldSetNotProcess
    void _set_mGroupKey(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGroupKey();
    @BFieldNotProcess
    java.lang.String mGroupKey();
}

package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Notification")
public interface NotificationMContext {
    @BFieldSetNotProcess
    void _set_mLargeIcon(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLargeIcon();
    @BFieldNotProcess
    android.graphics.drawable.Icon mLargeIcon();
    @BFieldSetNotProcess
    void _set_mSmallIcon(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSmallIcon();
    @BFieldNotProcess
    android.graphics.drawable.Icon mSmallIcon();
}

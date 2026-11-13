package com.featurestic.black.android.app.servertransaction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.servertransaction.LaunchActivityItem")
public interface LaunchActivityItemContext {
    @BFieldSetNotProcess
    void _set_mInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInfo();
    @BFieldNotProcess
    android.content.pm.ActivityInfo mInfo();
    @BFieldSetNotProcess
    void _set_mIntent(final Object value);
    @BFieldCheckNotProcess
    Field _check_mIntent();
    @BFieldNotProcess
    android.content.Intent mIntent();
}

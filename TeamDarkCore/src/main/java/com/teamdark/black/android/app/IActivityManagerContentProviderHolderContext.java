package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IActivityManager$ContentProviderHolder")
public interface IActivityManagerContentProviderHolderContext {
    @BFieldSetNotProcess
    void _set_info(final Object value);
    @BFieldCheckNotProcess
    Field _check_info();
    @BFieldNotProcess
    android.content.pm.ProviderInfo info();
    @BFieldSetNotProcess
    void _set_noReleaseNeeded(final Object value);
    @BFieldCheckNotProcess
    Field _check_noReleaseNeeded();
    @BFieldNotProcess
    java.lang.Boolean noReleaseNeeded();
    @BFieldSetNotProcess
    void _set_provider(final Object value);
    @BFieldCheckNotProcess
    Field _check_provider();
    @BFieldNotProcess
    android.os.IInterface provider();
}

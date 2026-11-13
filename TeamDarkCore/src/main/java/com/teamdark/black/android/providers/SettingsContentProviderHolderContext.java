package com.teamdark.black.android.providers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.provider.Settings$ContentProviderHolder")
public interface SettingsContentProviderHolderContext {
    @BFieldSetNotProcess
    void _set_mContentProvider(final Object value);
    @BFieldCheckNotProcess
    Field _check_mContentProvider();
    @BFieldNotProcess
    android.os.IInterface mContentProvider();
}

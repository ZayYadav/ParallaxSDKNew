package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk$ServiceDispatcher")
public interface LoadedApkServiceDispatcherContext {
    @BFieldSetNotProcess
    void _set_mConnection(final Object value);
    @BFieldCheckNotProcess
    Field _check_mConnection();
    @BFieldNotProcess
    android.content.ServiceConnection mConnection();
    @BFieldSetNotProcess
    void _set_mContext(final Object value);
    @BFieldCheckNotProcess
    Field _check_mContext();
    @BFieldNotProcess
    android.content.Context mContext();
}

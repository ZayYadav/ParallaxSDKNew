package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk$ServiceDispatcher$InnerConnection")
public interface LoadedApkServiceDispatcherInnerConnectionContext {
    @BFieldSetNotProcess
    void _set_mDispatcher(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDispatcher();
    @BFieldNotProcess
    java.lang.ref.WeakReference<?> mDispatcher();
}

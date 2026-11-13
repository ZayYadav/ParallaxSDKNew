package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk$ReceiverDispatcher$InnerReceiver")
public interface LoadedApkReceiverDispatcherInnerReceiverContext {
    @BFieldSetNotProcess
    void _set_mDispatcher(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDispatcher();
    @BFieldNotProcess
    java.lang.ref.WeakReference<?> mDispatcher();
}

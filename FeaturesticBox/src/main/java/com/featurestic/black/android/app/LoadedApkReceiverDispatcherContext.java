package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.LoadedApk$ReceiverDispatcher")
public interface LoadedApkReceiverDispatcherContext {
    @BMethodCheckNotProcess
    Method _check_getIIntentReceiver();
    android.os.IInterface getIIntentReceiver();
    @BFieldSetNotProcess
    void _set_mIIntentReceiver(final Object value);
    @BFieldCheckNotProcess
    Field _check_mIIntentReceiver();
    @BFieldNotProcess
    android.content.IIntentReceiver mIIntentReceiver();
    @BFieldSetNotProcess
    void _set_mReceiver(final Object value);
    @BFieldCheckNotProcess
    Field _check_mReceiver();
    @BFieldNotProcess
    android.content.BroadcastReceiver mReceiver();
}

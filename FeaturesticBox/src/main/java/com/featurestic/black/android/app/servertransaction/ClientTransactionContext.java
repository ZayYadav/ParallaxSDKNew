package com.featurestic.black.android.app.servertransaction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.servertransaction.ClientTransaction")
public interface ClientTransactionContext {
    @BFieldSetNotProcess
    void _set_mActivityCallbacks(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActivityCallbacks();
    @BFieldNotProcess
    java.util.List<java.lang.Object> mActivityCallbacks();
    @BFieldSetNotProcess
    void _set_mActivityToken(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActivityToken();
    @BFieldNotProcess
    android.os.IBinder mActivityToken();
    @BFieldSetNotProcess
    void _set_mLifecycleStateRequest(final Object value);
    @BFieldCheckNotProcess
    Field _check_mLifecycleStateRequest();
    @BFieldNotProcess
    java.lang.Object mLifecycleStateRequest();
}

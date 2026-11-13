package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ClientTransactionHandler")
public interface ClientTransactionHandlerContext {
    @BMethodCheckNotProcess
    Method _check_getActivityClient(android.os.IBinder IBinder0);
    java.lang.Object getActivityClient(android.os.IBinder IBinder0);
}

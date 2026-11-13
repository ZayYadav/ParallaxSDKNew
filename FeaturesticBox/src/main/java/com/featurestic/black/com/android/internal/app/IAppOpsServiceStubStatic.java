package com.featurestic.black.com.android.internal.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.app.IAppOpsService$Stub")
public interface IAppOpsServiceStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

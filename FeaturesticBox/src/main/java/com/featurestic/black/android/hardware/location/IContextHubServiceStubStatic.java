package com.featurestic.black.android.hardware.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.hardware.location.IContextHubService$Stub")
public interface IContextHubServiceStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder iBinder);
    android.os.IInterface asInterface(android.os.IBinder iBinder);
}

package com.teamdark.black.com.android.internal.telecom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.telecom.ITelecomService$Stub")
public interface ITelecomServiceStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

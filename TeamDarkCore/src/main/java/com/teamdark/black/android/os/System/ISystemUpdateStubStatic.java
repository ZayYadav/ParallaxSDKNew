package com.teamdark.black.android.os.System;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.ISystemUpdate$Stub")
public interface ISystemUpdateStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder binder);
    android.os.IInterface asInterface(android.os.IBinder binder);
}

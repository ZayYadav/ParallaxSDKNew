package com.teamdark.black.android.app.admin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.admin.IDevicePolicyManager$Stub")
public interface IDevicePolicyManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

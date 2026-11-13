package com.teamdark.black.android.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.IConnectivityManager$Stub")
public interface IConnectivityManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder binder);
    android.os.IInterface asInterface(android.os.IBinder binder);
}

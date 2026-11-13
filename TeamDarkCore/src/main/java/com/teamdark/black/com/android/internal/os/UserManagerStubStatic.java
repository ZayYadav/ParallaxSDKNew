package com.teamdark.black.com.android.internal.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.IUserManager$Stub")
public interface UserManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

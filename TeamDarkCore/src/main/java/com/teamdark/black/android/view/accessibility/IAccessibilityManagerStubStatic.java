package com.teamdark.black.android.view.accessibility;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.accessibility.IAccessibilityManager$Stub")
public interface IAccessibilityManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

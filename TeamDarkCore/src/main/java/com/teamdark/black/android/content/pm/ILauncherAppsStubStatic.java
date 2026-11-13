package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.ILauncherApps$Stub")
public interface ILauncherAppsStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder binder);
    android.os.IInterface asInterface(android.os.IBinder binder);
}

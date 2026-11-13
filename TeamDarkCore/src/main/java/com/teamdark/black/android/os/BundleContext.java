package com.teamdark.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.Bundle")
public interface BundleContext {
    @BMethodCheckNotProcess
    Method _check_getIBinder(java.lang.String String0);
    android.os.IBinder getIBinder(java.lang.String String0);
    @BMethodCheckNotProcess
    Method _check_putIBinder(java.lang.String String0, android.os.IBinder IBinder1);
    java.lang.Void putIBinder(java.lang.String String0, android.os.IBinder IBinder1);
}

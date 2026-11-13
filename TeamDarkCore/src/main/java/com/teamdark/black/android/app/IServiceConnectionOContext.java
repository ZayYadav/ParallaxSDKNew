package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IServiceConnection")
public interface IServiceConnectionOContext {
    @BMethodCheckNotProcess
    Method _check_connected(android.content.ComponentName ComponentName0, android.os.IBinder IBinder1, boolean boolean2);
    java.lang.Void connected(android.content.ComponentName ComponentName0, android.os.IBinder IBinder1, boolean boolean2);
}

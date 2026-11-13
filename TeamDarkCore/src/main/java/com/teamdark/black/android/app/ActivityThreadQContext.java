package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadQContext {
    @BMethodCheckNotProcess
    Method _check_handleNewIntent(android.os.IBinder IBinder0, java.util.List List1);
    java.lang.Void handleNewIntent(android.os.IBinder IBinder0, java.util.List List1);
}

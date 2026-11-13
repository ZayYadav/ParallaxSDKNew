package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread")
public interface ActivityThreadNMR1Context {
    @BMethodCheckNotProcess
    Method _check_performNewIntents(android.os.IBinder IBinder0, java.util.List List1, boolean boolean2);
    java.lang.Void performNewIntents(android.os.IBinder IBinder0, java.util.List List1, boolean boolean2);
}

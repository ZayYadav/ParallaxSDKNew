package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IApplicationThread")
public interface IApplicationThreadContext {
    @BMethodCheckNotProcess
    Method _check_scheduleBindService(android.os.IBinder IBinder0, android.content.Intent Intent1, boolean boolean2);
    java.lang.Void scheduleBindService(android.os.IBinder IBinder0, android.content.Intent Intent1, boolean boolean2);
    @BMethodCheckNotProcess
    Method _check_scheduleCreateService(android.os.IBinder IBinder0, android.content.pm.ServiceInfo ServiceInfo1);
    java.lang.Void scheduleCreateService(android.os.IBinder IBinder0, android.content.pm.ServiceInfo ServiceInfo1);
    @BMethodCheckNotProcess
    Method _check_scheduleNewIntent(java.util.List List0, android.os.IBinder IBinder1);
    java.lang.Void scheduleNewIntent(java.util.List List0, android.os.IBinder IBinder1);
    @BMethodCheckNotProcess
    Method _check_scheduleServiceArgs(android.os.IBinder IBinder0, int int1, int int2, android.content.Intent Intent3);
    java.lang.Void scheduleServiceArgs(android.os.IBinder IBinder0, int int1, int int2, android.content.Intent Intent3);
    @BMethodCheckNotProcess
    Method _check_scheduleStopService(android.os.IBinder IBinder0);
    java.lang.Void scheduleStopService(android.os.IBinder IBinder0);
    @BMethodCheckNotProcess
    Method _check_scheduleUnbindService(android.os.IBinder IBinder0, android.content.Intent Intent1);
    java.lang.Void scheduleUnbindService(android.os.IBinder IBinder0, android.content.Intent Intent1);
}

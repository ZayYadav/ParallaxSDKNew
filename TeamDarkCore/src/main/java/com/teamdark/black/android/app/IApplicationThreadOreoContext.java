package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IApplicationThread")
public interface IApplicationThreadOreoContext {
    @BMethodCheckNotProcess
    Method _check_scheduleServiceArgs();
    java.lang.Void scheduleServiceArgs();
}

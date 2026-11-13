package com.featurestic.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.IApplicationThread")
public interface IApplicationThreadOreoContext {
    @BMethodCheckNotProcess
    Method _check_scheduleServiceArgs();
    java.lang.Void scheduleServiceArgs();
}

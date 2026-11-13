package com.featurestic.black.android.os.health;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.health.SystemHealthManager")
public interface SystemHealthManagerContext {
    @BFieldSetNotProcess
    void _set_mBatteryStats(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBatteryStats();
    @BFieldNotProcess
    android.os.IInterface mBatteryStats();
}

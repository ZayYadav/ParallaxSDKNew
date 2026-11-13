package com.teamdark.black.com.cosmos.apm.framework.page;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.cosmos.apm.framework.page.ActivityLifeCycleHelper$ApplicationInstrumentation")
public interface ActivityLifeCycleHelper$ApplicationInstrumentationContext {
    @BFieldSetNotProcess
    void _set_mBase(final Object value);
    @BFieldCheckNotProcess
    Field _check_mBase();
    @BFieldNotProcess
    android.app.Instrumentation mBase();
}

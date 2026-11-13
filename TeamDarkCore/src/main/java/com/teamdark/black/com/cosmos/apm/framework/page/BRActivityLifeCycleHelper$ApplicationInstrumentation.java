package com.teamdark.black.com.cosmos.apm.framework.page;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityLifeCycleHelper$ApplicationInstrumentation {
    public static ActivityLifeCycleHelper$ApplicationInstrumentationStatic getWithException() {
        return BlackReflection.create(ActivityLifeCycleHelper$ApplicationInstrumentationStatic.class, null, true);
    }
    public static ActivityLifeCycleHelper$ApplicationInstrumentationStatic get() {
        return BlackReflection.create(ActivityLifeCycleHelper$ApplicationInstrumentationStatic.class, null, false);
    }
    public static ActivityLifeCycleHelper$ApplicationInstrumentationContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityLifeCycleHelper$ApplicationInstrumentationContext.class, caller, true);
    }
    public static ActivityLifeCycleHelper$ApplicationInstrumentationContext get(final Object caller) {
        return BlackReflection.create(ActivityLifeCycleHelper$ApplicationInstrumentationContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityLifeCycleHelper$ApplicationInstrumentationContext.class);
    }
}

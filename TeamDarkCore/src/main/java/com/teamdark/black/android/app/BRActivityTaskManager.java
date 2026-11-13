package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityTaskManager {
    public static ActivityTaskManagerStatic getWithException() {
        return BlackReflection.create(ActivityTaskManagerStatic.class, null, true);
    }
    public static ActivityTaskManagerStatic get() {
        return BlackReflection.create(ActivityTaskManagerStatic.class, null, false);
    }
    public static ActivityTaskManagerContext getWithException(final Object caller) {
        return BlackReflection.create(ActivityTaskManagerContext.class, caller, true);
    }
    public static ActivityTaskManagerContext get(final Object caller) {
        return BlackReflection.create(ActivityTaskManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityTaskManagerContext.class);
    }
}

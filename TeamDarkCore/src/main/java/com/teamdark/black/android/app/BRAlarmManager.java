package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRAlarmManager {
    public static AlarmManagerStatic getWithException() {
        return BlackReflection.create(AlarmManagerStatic.class, null, true);
    }
    public static AlarmManagerStatic get() {
        return BlackReflection.create(AlarmManagerStatic.class, null, false);
    }
    public static AlarmManagerContext getWithException(final Object caller) {
        return BlackReflection.create(AlarmManagerContext.class, caller, true);
    }
    public static AlarmManagerContext get(final Object caller) {
        return BlackReflection.create(AlarmManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(AlarmManagerContext.class);
    }
}

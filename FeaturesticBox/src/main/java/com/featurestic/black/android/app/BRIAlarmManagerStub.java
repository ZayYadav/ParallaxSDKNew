package com.featurestic.black.android.app;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAlarmManagerStub {
    public static IAlarmManagerStubStatic getWithException() {
        return BlackReflection.create(IAlarmManagerStubStatic.class, null, true);
    }
    public static IAlarmManagerStubStatic get() {
        return BlackReflection.create(IAlarmManagerStubStatic.class, null, false);
    }
    public static IAlarmManagerStubContext getWithException(final Object caller) {
        return BlackReflection.create(IAlarmManagerStubContext.class, caller, true);
    }
    public static IAlarmManagerStubContext get(final Object caller) {
        return BlackReflection.create(IAlarmManagerStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAlarmManagerStubContext.class);
    }
}

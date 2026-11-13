package com.teamdark.black.android.media;

import com.teamdark.blackreflection.BlackReflection;

public class BRAudioManager {
    public static AudioManagerStatic getWithException() {
        return BlackReflection.create(AudioManagerStatic.class, null, true);
    }
    public static AudioManagerStatic get() {
        return BlackReflection.create(AudioManagerStatic.class, null, false);
    }
    public static AudioManagerContext getWithException(final Object caller) {
        return BlackReflection.create(AudioManagerContext.class, caller, true);
    }
    public static AudioManagerContext get(final Object caller) {
        return BlackReflection.create(AudioManagerContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(AudioManagerContext.class);
    }
}

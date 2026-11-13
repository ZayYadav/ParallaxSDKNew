package com.featurestic.black.android.media;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAudioService {
    public static IAudioServiceStatic getWithException() {
        return BlackReflection.create(IAudioServiceStatic.class, null, true);
    }
    public static IAudioServiceStatic get() {
        return BlackReflection.create(IAudioServiceStatic.class, null, false);
    }
    public static IAudioServiceContext getWithException(final Object caller) {
        return BlackReflection.create(IAudioServiceContext.class, caller, true);
    }
    public static IAudioServiceContext get(final Object caller) {
        return BlackReflection.create(IAudioServiceContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAudioServiceContext.class);
    }
}

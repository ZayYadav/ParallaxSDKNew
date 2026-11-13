package com.featurestic.black.android.media;

import com.featurestic.blackreflection.BlackReflection;

public class BRIAudioServiceStub {
    public static IAudioServiceStubStatic getWithException() {
        return BlackReflection.create(IAudioServiceStubStatic.class, null, true);
    }
    public static IAudioServiceStubStatic get() {
        return BlackReflection.create(IAudioServiceStubStatic.class, null, false);
    }
    public static IAudioServiceStubContext getWithException(final Object caller) {
        return BlackReflection.create(IAudioServiceStubContext.class, caller, true);
    }
    public static IAudioServiceStubContext get(final Object caller) {
        return BlackReflection.create(IAudioServiceStubContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(IAudioServiceStubContext.class);
    }
}

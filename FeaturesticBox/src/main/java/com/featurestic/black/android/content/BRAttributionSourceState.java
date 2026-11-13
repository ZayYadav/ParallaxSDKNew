package com.featurestic.black.android.content;

import com.featurestic.blackreflection.BlackReflection;

public class BRAttributionSourceState {
    public static AttributionSourceStateStatic getWithException() {
        return BlackReflection.create(AttributionSourceStateStatic.class, null, true);
    }
    public static AttributionSourceStateStatic get() {
        return BlackReflection.create(AttributionSourceStateStatic.class, null, false);
    }
    public static AttributionSourceStateContext getWithException(final Object caller) {
        return BlackReflection.create(AttributionSourceStateContext.class, caller, true);
    }
    public static AttributionSourceStateContext get(final Object caller) {
        return BlackReflection.create(AttributionSourceStateContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(AttributionSourceStateContext.class);
    }
}

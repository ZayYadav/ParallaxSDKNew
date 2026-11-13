package com.featurestic.black.android.view;

import com.featurestic.blackreflection.BlackReflection;

public class BRSurfaceControl {
    public static SurfaceControlStatic getWithException() {
        return BlackReflection.create(SurfaceControlStatic.class, null, true);
    }
    public static SurfaceControlStatic get() {
        return BlackReflection.create(SurfaceControlStatic.class, null, false);
    }
    public static SurfaceControlContext getWithException(final Object caller) {
        return BlackReflection.create(SurfaceControlContext.class, caller, true);
    }
    public static SurfaceControlContext get(final Object caller) {
        return BlackReflection.create(SurfaceControlContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.featurestic.blackreflection.utils.ClassUtil.classReady(SurfaceControlContext.class);
    }
}

package com.teamdark.black.android.app;

import com.teamdark.blackreflection.BlackReflection;

public class BRActivityThreadProviderKeyJBMR1 {
    public static ActivityThreadProviderKeyJBMR1Static getWithException() {
        return BlackReflection.create(ActivityThreadProviderKeyJBMR1Static.class, null, true);
    }
    public static ActivityThreadProviderKeyJBMR1Static get() {
        return BlackReflection.create(ActivityThreadProviderKeyJBMR1Static.class, null, false);
    }
    public static ActivityThreadProviderKeyJBMR1Context getWithException(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderKeyJBMR1Context.class, caller, true);
    }
    public static ActivityThreadProviderKeyJBMR1Context get(final Object caller) {
        return BlackReflection.create(ActivityThreadProviderKeyJBMR1Context.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(ActivityThreadProviderKeyJBMR1Context.class);
    }
}

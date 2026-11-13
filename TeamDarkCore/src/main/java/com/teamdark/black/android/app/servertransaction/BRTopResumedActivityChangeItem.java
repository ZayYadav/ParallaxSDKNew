package com.teamdark.black.android.app.servertransaction;

import com.teamdark.blackreflection.BlackReflection;

public class BRTopResumedActivityChangeItem {
    public static TopResumedActivityChangeItemStatic getWithException() {
        return BlackReflection.create(TopResumedActivityChangeItemStatic.class, null, true);
    }
    public static TopResumedActivityChangeItemStatic get() {
        return BlackReflection.create(TopResumedActivityChangeItemStatic.class, null, false);
    }
    public static TopResumedActivityChangeItemContext getWithException(final Object caller) {
        return BlackReflection.create(TopResumedActivityChangeItemContext.class, caller, true);
    }
    public static TopResumedActivityChangeItemContext get(final Object caller) {
        return BlackReflection.create(TopResumedActivityChangeItemContext.class, caller, false);
    }
    public static Class<?> getRealClass() {
        return com.teamdark.blackreflection.utils.ClassUtil.classReady(TopResumedActivityChangeItemContext.class);
    }
}

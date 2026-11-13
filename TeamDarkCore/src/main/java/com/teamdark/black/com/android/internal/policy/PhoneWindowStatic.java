package com.teamdark.black.com.android.internal.policy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.policy.PhoneWindow$WindowManagerHolder")
public interface PhoneWindowStatic {
    @BFieldSetNotProcess
    void _set_sWindowManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_sWindowManager();
    @BFieldNotProcess
    android.os.IInterface sWindowManager();
}

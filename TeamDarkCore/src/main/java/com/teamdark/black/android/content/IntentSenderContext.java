package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.IntentSender")
public interface IntentSenderContext {
    @BFieldSetNotProcess
    void _set_mTarget(final Object value);
    @BFieldCheckNotProcess
    Field _check_mTarget();
    @BFieldNotProcess
    android.os.IInterface mTarget();
}

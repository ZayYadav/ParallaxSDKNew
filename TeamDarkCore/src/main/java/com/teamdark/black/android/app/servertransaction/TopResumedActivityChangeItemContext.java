package com.teamdark.black.android.app.servertransaction;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.servertransaction.TopResumedActivityChangeItem")
public interface TopResumedActivityChangeItemContext {
    @BFieldSetNotProcess
    void _set_mOnTop(final Object value);
    @BFieldCheckNotProcess
    Field _check_mOnTop();
    @BFieldNotProcess
    java.lang.Boolean mOnTop();
}

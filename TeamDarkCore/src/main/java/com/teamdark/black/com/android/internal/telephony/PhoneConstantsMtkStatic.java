package com.teamdark.black.com.android.internal.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.telephony.PhoneConstants")
public interface PhoneConstantsMtkStatic {
    @BFieldSetNotProcess
    void _set_GEMINI_SIM_NUM(final Object value);
    @BFieldCheckNotProcess
    Field _check_GEMINI_SIM_NUM();
    @BFieldNotProcess
    java.lang.Integer GEMINI_SIM_NUM();
}

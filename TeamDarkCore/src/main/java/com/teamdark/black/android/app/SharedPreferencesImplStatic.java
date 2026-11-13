package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.SharedPreferencesImpl")
public interface SharedPreferencesImplStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.SharedPreferencesImpl _new(java.io.File File0, int int1);
}

package com.teamdark.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.DisplayAdjustments")
public interface DisplayAdjustmentsContext {
    @BMethodCheckNotProcess
    Method _check_setCompatibilityInfo();
    java.lang.Void setCompatibilityInfo();
}

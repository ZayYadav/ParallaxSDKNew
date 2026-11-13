package com.teamdark.black.android.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.view.CompatibilityInfoHolder")
public interface CompatibilityInfoHolderContext {
    @BMethodCheckNotProcess
    Method _check_set();
    java.lang.Void set();
}

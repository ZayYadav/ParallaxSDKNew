package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser$Activity")
public interface PackageParserActivityContext {
    @BFieldSetNotProcess
    void _set_info(final Object value);
    @BFieldCheckNotProcess
    Field _check_info();
    @BFieldNotProcess
    android.content.pm.ActivityInfo info();
}

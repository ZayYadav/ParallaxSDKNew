package com.teamdark.black.android.app.usage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.usage.StorageStats")
public interface StorageStatsStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.usage.StorageStats _new();
}

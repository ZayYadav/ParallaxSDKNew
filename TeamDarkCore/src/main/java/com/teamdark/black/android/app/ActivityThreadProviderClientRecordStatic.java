package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.ActivityThread$ProviderClientRecord")
public interface ActivityThreadProviderClientRecordStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.ActivityThread.ProviderClientRecord _new();
}

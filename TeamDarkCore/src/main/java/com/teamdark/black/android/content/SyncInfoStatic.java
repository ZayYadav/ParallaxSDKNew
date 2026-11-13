package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.SyncInfo")
public interface SyncInfoStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.content.SyncInfo _new(int int0, android.accounts.Account Account1, java.lang.String String2, long long3);
}

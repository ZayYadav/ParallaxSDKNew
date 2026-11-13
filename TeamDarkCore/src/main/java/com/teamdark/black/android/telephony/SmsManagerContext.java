package com.teamdark.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.SmsManager")
public interface SmsManagerContext {
    @BMethodCheckNotProcess
    Method _check_getAutoPersisting();
    java.lang.Boolean getAutoPersisting();
    @BMethodCheckNotProcess
    Method _check_setAutoPersisting(boolean boolean0);
    java.lang.Void setAutoPersisting(boolean boolean0);
}

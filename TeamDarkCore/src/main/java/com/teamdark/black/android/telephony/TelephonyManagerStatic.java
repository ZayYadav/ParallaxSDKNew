package com.teamdark.black.android.telephony;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.telephony.TelephonyManager")
public interface TelephonyManagerStatic {
    @BMethodCheckNotProcess
    Method _check_getSubscriberInfoService();
    java.lang.Object getSubscriberInfoService();
    @BFieldSetNotProcess
    void _set_sServiceHandleCacheEnabled(final Object value);
    @BFieldCheckNotProcess
    Field _check_sServiceHandleCacheEnabled();
    @BFieldNotProcess
    java.lang.Boolean sServiceHandleCacheEnabled();
    @BFieldSetNotProcess
    void _set_sIPhoneSubInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_sIPhoneSubInfo();
    @BFieldNotProcess
    android.os.IInterface sIPhoneSubInfo();
}

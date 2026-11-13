package com.teamdark.black.android.net.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.wifi.WifiManager")
public interface WifiManagerStatic {
    @BFieldSetNotProcess
    void _set_sService(final Object value);
    @BFieldCheckNotProcess
    Field _check_sService();
    @BFieldNotProcess
    android.os.IInterface sService();
}

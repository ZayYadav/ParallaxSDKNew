package com.teamdark.black.android.net.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.wifi.WifiManager")
public interface WifiManagerContext {
    @BFieldSetNotProcess
    void _set_mService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mService();
    @BFieldNotProcess
    android.os.IInterface mService();
}

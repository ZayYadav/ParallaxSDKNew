package com.teamdark.black.android.net.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.wifi.WifiScanner")
public interface WifiScannerStatic {
    @BFieldSetNotProcess
    void _set_GET_AVAILABLE_CHANNELS_EXTRA(final Object value);
    @BFieldCheckNotProcess
    Field _check_GET_AVAILABLE_CHANNELS_EXTRA();
    @BFieldNotProcess
    java.lang.String GET_AVAILABLE_CHANNELS_EXTRA();
}

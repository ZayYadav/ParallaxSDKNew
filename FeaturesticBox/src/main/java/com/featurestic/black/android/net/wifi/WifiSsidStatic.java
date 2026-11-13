package com.featurestic.black.android.net.wifi;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.net.wifi.WifiSsid")
public interface WifiSsidStatic {
    @BMethodCheckNotProcess
    Method _check_createFromAsciiEncoded(java.lang.String asciiEncoded);
    java.lang.Object createFromAsciiEncoded(java.lang.String asciiEncoded);
}

package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.ILocationListener")
public interface ILocationListenerContext {
    @BMethodCheckNotProcess
    Method _check_onLocationChanged(android.location.Location Location0);
    java.lang.Void onLocationChanged(android.location.Location Location0);
}

package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationManager$GnssStatusListenerTransport")
public interface LocationManagerListenerTransportContext {
    @BMethodCheckNotProcess
    Method _check_onLocationChanged(android.location.Location Location0);
    java.lang.Void onLocationChanged(android.location.Location Location0);
    @BMethodCheckNotProcess
    Method _check_onProviderDisabled(java.lang.String String0);
    java.lang.Void onProviderDisabled(java.lang.String String0);
    @BMethodCheckNotProcess
    Method _check_onProviderEnabled(java.lang.String String0);
    java.lang.Void onProviderEnabled(java.lang.String String0);
    @BMethodCheckNotProcess
    Method _check_onStatusChanged(java.lang.String String0, int int1, android.os.Bundle Bundle2);
    java.lang.Void onStatusChanged(java.lang.String String0, int int1, android.os.Bundle Bundle2);
    @BFieldSetNotProcess
    void _set_mListener(final Object value);
    @BFieldCheckNotProcess
    Field _check_mListener();
    @BFieldNotProcess
    android.location.LocationListener mListener();
    @BFieldSetNotProcess
    void _set_this$0(final Object value);
    @BFieldCheckNotProcess
    Field _check_this$0();
    @BFieldNotProcess
    java.lang.Object this$0();
}

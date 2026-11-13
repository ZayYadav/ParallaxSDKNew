package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationManager$GnssStatusListenerTransport")
public interface LocationManagerGnssStatusListenerTransportContext {
    @BMethodCheckNotProcess
    Method _check_onFirstFix(int int0);
    java.lang.Void onFirstFix(int int0);
    @BMethodCheckNotProcess
    Method _check_onGnssStarted();
    java.lang.Void onGnssStarted();
    @BMethodCheckNotProcess
    Method _check_onNmeaReceived(long long0, java.lang.String String1);
    java.lang.Void onNmeaReceived(long long0, java.lang.String String1);
    @BFieldSetNotProcess
    void _set_mGpsListener(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsListener();
    @BFieldNotProcess
    java.lang.Object mGpsListener();
    @BFieldSetNotProcess
    void _set_mGpsNmeaListener(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsNmeaListener();
    @BFieldNotProcess
    java.lang.Object mGpsNmeaListener();
    @BFieldSetNotProcess
    void _set_this$0(final Object value);
    @BFieldCheckNotProcess
    Field _check_this$0();
    @BFieldNotProcess
    java.lang.Object this$0();
}

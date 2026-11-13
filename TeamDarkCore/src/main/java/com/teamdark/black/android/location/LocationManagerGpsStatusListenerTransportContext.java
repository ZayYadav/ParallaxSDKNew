package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationManager$GpsStatusListenerTransport")
public interface LocationManagerGpsStatusListenerTransportContext {
    @BMethodCheckNotProcess
    Method _check_onFirstFix(int int0);
    java.lang.Void onFirstFix(int int0);
    @BMethodCheckNotProcess
    Method _check_onGpsStarted();
    java.lang.Void onGpsStarted();
    @BMethodCheckNotProcess
    Method _check_onNmeaReceived(long long0, java.lang.String String1);
    java.lang.Void onNmeaReceived(long long0, java.lang.String String1);
    @BFieldSetNotProcess
    void _set_mListener(final Object value);
    @BFieldCheckNotProcess
    Field _check_mListener();
    @BFieldNotProcess
    java.lang.Object mListener();
    @BFieldSetNotProcess
    void _set_mNmeaListener(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNmeaListener();
    @BFieldNotProcess
    java.lang.Object mNmeaListener();
    @BFieldSetNotProcess
    void _set_this$0(final Object value);
    @BFieldCheckNotProcess
    Field _check_this$0();
    @BFieldNotProcess
    java.lang.Object this$0();
}

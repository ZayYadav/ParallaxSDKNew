package com.featurestic.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationManager")
public interface LocationManagerContext {
    @BFieldSetNotProcess
    void _set_mGnssNmeaListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGnssNmeaListeners();
    @BFieldNotProcess
    java.util.HashMap mGnssNmeaListeners();
    @BFieldSetNotProcess
    void _set_mGnssStatusListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGnssStatusListeners();
    @BFieldNotProcess
    java.util.HashMap mGnssStatusListeners();
    @BFieldSetNotProcess
    void _set_mGpsNmeaListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsNmeaListeners();
    @BFieldNotProcess
    java.util.HashMap mGpsNmeaListeners();
    @BFieldSetNotProcess
    void _set_mGpsStatusListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsStatusListeners();
    @BFieldNotProcess
    java.util.HashMap mGpsStatusListeners();
    @BFieldSetNotProcess
    void _set_mListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mListeners();
    @BFieldNotProcess
    java.util.HashMap mListeners();
    @BFieldSetNotProcess
    void _set_mNmeaListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mNmeaListeners();
    @BFieldNotProcess
    java.util.HashMap mNmeaListeners();
    @BFieldSetNotProcess
    void _set_mService(final Object value);
    @BFieldCheckNotProcess
    Field _check_mService();
    @BFieldNotProcess
    android.os.IInterface mService();
}

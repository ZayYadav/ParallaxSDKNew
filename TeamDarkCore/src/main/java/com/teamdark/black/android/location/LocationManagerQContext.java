package com.teamdark.black.android.location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.LocationManager")
public interface LocationManagerQContext {
    @BFieldSetNotProcess
    void _set_mGnssNmeaListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGnssNmeaListeners();
    @BFieldNotProcess
    android.util.ArrayMap mGnssNmeaListeners();
    @BFieldSetNotProcess
    void _set_mGnssStatusListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGnssStatusListeners();
    @BFieldNotProcess
    android.util.ArrayMap mGnssStatusListeners();
    @BFieldSetNotProcess
    void _set_mGpsNmeaListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsNmeaListeners();
    @BFieldNotProcess
    android.util.ArrayMap mGpsNmeaListeners();
    @BFieldSetNotProcess
    void _set_mGpsStatusListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGpsStatusListeners();
    @BFieldNotProcess
    android.util.ArrayMap mGpsStatusListeners();
    @BFieldSetNotProcess
    void _set_mListeners(final Object value);
    @BFieldCheckNotProcess
    Field _check_mListeners();
    @BFieldNotProcess
    android.util.ArrayMap mListeners();
}

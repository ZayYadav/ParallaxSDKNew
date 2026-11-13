package com.featurestic.black.android.location.provider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.location.provider.ProviderProperties")
public interface ProviderPropertiesContext {
    @BFieldSetNotProcess
    void _set_mHasNetworkRequirement(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasNetworkRequirement();
    @BFieldNotProcess
    java.lang.Boolean mHasNetworkRequirement();
    @BFieldSetNotProcess
    void _set_mHasSatelliteRequirement(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasSatelliteRequirement();
    @BFieldNotProcess
    java.lang.Boolean mHasSatelliteRequirement();
    @BFieldSetNotProcess
    void _set_mHasCellRequirement(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasCellRequirement();
    @BFieldNotProcess
    java.lang.Boolean mHasCellRequirement();
    @BFieldSetNotProcess
    void _set_mHasMonetaryCost(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasMonetaryCost();
    @BFieldNotProcess
    java.lang.Boolean mHasMonetaryCost();
    @BFieldSetNotProcess
    void _set_mHasAltitudeSupport(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasAltitudeSupport();
    @BFieldNotProcess
    java.lang.Boolean mHasAltitudeSupport();
    @BFieldSetNotProcess
    void _set_mHasSpeedSupport(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasSpeedSupport();
    @BFieldNotProcess
    java.lang.Boolean mHasSpeedSupport();
    @BFieldSetNotProcess
    void _set_mHasBearingSupport(final Object value);
    @BFieldCheckNotProcess
    Field _check_mHasBearingSupport();
    @BFieldNotProcess
    java.lang.Boolean mHasBearingSupport();
}

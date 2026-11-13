package com.featurestic.black.com.android.internal.net;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("com.android.internal.net.VpnConfig")
public interface VpnConfigContext {
    @BFieldSetNotProcess
    void _set_user(final Object value);
    @BFieldCheckNotProcess
    Field _check_user();
    @BFieldNotProcess
    java.lang.String user();
    @BFieldSetNotProcess
    void _set_disallowedApplications(final Object value);
    @BFieldCheckNotProcess
    Field _check_disallowedApplications();
    @BFieldNotProcess
    java.util.List<java.lang.String> disallowedApplications();
    @BFieldSetNotProcess
    void _set_allowedApplications(final Object value);
    @BFieldCheckNotProcess
    Field _check_allowedApplications();
    @BFieldNotProcess
    java.util.List<java.lang.String> allowedApplications();
}

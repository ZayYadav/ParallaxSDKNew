package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.PackageParser$Package")
public interface PackageParserPackageContext {
    @BFieldSetNotProcess
    void _set_activities(final Object value);
    @BFieldCheckNotProcess
    Field _check_activities();
    @BFieldNotProcess
    java.util.List activities();
    @BFieldSetNotProcess
    void _set_mAppMetaData(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAppMetaData();
    @BFieldNotProcess
    android.os.Bundle mAppMetaData();
    @BFieldSetNotProcess
    void _set_mSharedUserId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSharedUserId();
    @BFieldNotProcess
    java.lang.String mSharedUserId();
    @BFieldSetNotProcess
    void _set_mSignatures(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSignatures();
    @BFieldNotProcess
    android.content.pm.Signature[] mSignatures();
    @BFieldSetNotProcess
    void _set_mSigningDetails(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSigningDetails();
    @BFieldNotProcess
    java.lang.Object mSigningDetails();
    @BFieldSetNotProcess
    void _set_mVersionCode(final Object value);
    @BFieldCheckNotProcess
    Field _check_mVersionCode();
    @BFieldNotProcess
    java.lang.Integer mVersionCode();
    @BFieldSetNotProcess
    void _set_packageName(final Object value);
    @BFieldCheckNotProcess
    Field _check_packageName();
    @BFieldNotProcess
    java.lang.String packageName();
    @BFieldSetNotProcess
    void _set_permissionGroups(final Object value);
    @BFieldCheckNotProcess
    Field _check_permissionGroups();
    @BFieldNotProcess
    java.util.List permissionGroups();
    @BFieldSetNotProcess
    void _set_permissions(final Object value);
    @BFieldCheckNotProcess
    Field _check_permissions();
    @BFieldNotProcess
    java.util.List permissions();
    @BFieldSetNotProcess
    void _set_protectedBroadcasts(final Object value);
    @BFieldCheckNotProcess
    Field _check_protectedBroadcasts();
    @BFieldNotProcess
    java.util.List<java.lang.String> protectedBroadcasts();
    @BFieldSetNotProcess
    void _set_providers(final Object value);
    @BFieldCheckNotProcess
    Field _check_providers();
    @BFieldNotProcess
    java.util.List providers();
    @BFieldSetNotProcess
    void _set_receivers(final Object value);
    @BFieldCheckNotProcess
    Field _check_receivers();
    @BFieldNotProcess
    java.util.List receivers();
    @BFieldSetNotProcess
    void _set_requestedPermissions(final Object value);
    @BFieldCheckNotProcess
    Field _check_requestedPermissions();
    @BFieldNotProcess
    java.util.List<java.lang.String> requestedPermissions();
    @BFieldSetNotProcess
    void _set_services(final Object value);
    @BFieldCheckNotProcess
    Field _check_services();
    @BFieldNotProcess
    java.util.List services();
}

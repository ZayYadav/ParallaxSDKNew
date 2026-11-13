package com.featurestic.black.android.credentials;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.credentials.ICredentialManager")
public interface ICredentialManagerContext {
    @BMethodCheckNotProcess
    Method _check_getCredential(java.lang.String requestType);
    android.credentials.GetCredentialResponse getCredential(java.lang.String requestType);
    @BMethodCheckNotProcess
    Method _check_createCredential(java.lang.String requestType);
    android.credentials.CreateCredentialResponse createCredential(java.lang.String requestType);
    @BMethodCheckNotProcess
    Method _check_prepareGetCredential(java.lang.String requestType);
    android.credentials.PrepareGetCredentialResponse prepareGetCredential(java.lang.String requestType);
    @BMethodCheckNotProcess
    Method _check_setAllowedProviders(java.lang.String[] packageNames);
    java.lang.Void setAllowedProviders(java.lang.String[] packageNames);
    @BMethodCheckNotProcess
    Method _check_getAllowedProviders();
    java.lang.String[] getAllowedProviders();
}

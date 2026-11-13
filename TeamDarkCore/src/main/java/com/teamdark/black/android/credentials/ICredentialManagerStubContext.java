package com.teamdark.black.android.credentials;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.credentials.ICredentialManager$Stub")
public interface ICredentialManagerStubContext {
    @BFieldSetNotProcess
    void _set_TRANSACTION_getCredential(final Object value);
    @BFieldCheckNotProcess
    Field _check_TRANSACTION_getCredential();
    @BFieldNotProcess
    java.lang.Integer TRANSACTION_getCredential();
    @BFieldSetNotProcess
    void _set_TRANSACTION_createCredential(final Object value);
    @BFieldCheckNotProcess
    Field _check_TRANSACTION_createCredential();
    @BFieldNotProcess
    java.lang.Integer TRANSACTION_createCredential();
    @BFieldSetNotProcess
    void _set_TRANSACTION_prepareGetCredential(final Object value);
    @BFieldCheckNotProcess
    Field _check_TRANSACTION_prepareGetCredential();
    @BFieldNotProcess
    java.lang.Integer TRANSACTION_prepareGetCredential();
    @BFieldSetNotProcess
    void _set_TRANSACTION_setAllowedProviders(final Object value);
    @BFieldCheckNotProcess
    Field _check_TRANSACTION_setAllowedProviders();
    @BFieldNotProcess
    java.lang.Integer TRANSACTION_setAllowedProviders();
    @BFieldSetNotProcess
    void _set_TRANSACTION_getAllowedProviders(final Object value);
    @BFieldCheckNotProcess
    Field _check_TRANSACTION_getAllowedProviders();
    @BFieldNotProcess
    java.lang.Integer TRANSACTION_getAllowedProviders();
}

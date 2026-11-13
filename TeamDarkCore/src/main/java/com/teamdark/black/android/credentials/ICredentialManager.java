package com.teamdark.black.android.credentials;

import android.credentials.CreateCredentialResponse;
import android.credentials.GetCredentialResponse;
import android.credentials.PrepareGetCredentialResponse;
import android.os.IBinder;
import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BStaticMethod;
import com.teamdark.blackreflection.annotation.BField;
import com.teamdark.blackreflection.annotation.BMethod;

@BClassName("android.credentials.ICredentialManager")
public interface ICredentialManager {

    @BClassName("android.credentials.ICredentialManager$Stub")
    interface Stub {
        @BStaticMethod
        IInterface asInterface(IBinder binder);
        
        @BField
        int TRANSACTION_getCredential();
        
        @BField
        int TRANSACTION_createCredential();
        
        @BField
        int TRANSACTION_prepareGetCredential();
        
        @BField
        int TRANSACTION_setAllowedProviders();
        
        @BField
        int TRANSACTION_getAllowedProviders();
    }

    // Main interface methods
    @BMethod
    GetCredentialResponse getCredential(String requestType);

    @BMethod
    CreateCredentialResponse createCredential(String requestType);

    @BMethod
    PrepareGetCredentialResponse prepareGetCredential(String requestType);

    @BMethod
    void setAllowedProviders(String[] packageNames);

    @BMethod
    String[] getAllowedProviders();
}

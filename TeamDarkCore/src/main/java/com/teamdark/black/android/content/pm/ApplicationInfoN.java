package com.teamdark.black.android.content.pm;


import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.content.pm.ApplicationInfo")
public interface ApplicationInfoN {
    @BField
    String credentialEncryptedDataDir();

    @BField
    String credentialProtectedDataDir();

    @BField
    String deviceEncryptedDataDir();

    @BField
    String deviceProtectedDataDir();
}

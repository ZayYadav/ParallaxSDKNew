package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.SyncRequest")
public interface SyncRequestContext {
    @BFieldSetNotProcess
    void _set_mAccountToSync(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAccountToSync();
    @BFieldNotProcess
    android.accounts.Account mAccountToSync();
    @BFieldSetNotProcess
    void _set_mAuthority(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAuthority();
    @BFieldNotProcess
    java.lang.String mAuthority();
    @BFieldSetNotProcess
    void _set_mExtras(final Object value);
    @BFieldCheckNotProcess
    Field _check_mExtras();
    @BFieldNotProcess
    android.os.Bundle mExtras();
    @BFieldSetNotProcess
    void _set_mIsPeriodic(final Object value);
    @BFieldCheckNotProcess
    Field _check_mIsPeriodic();
    @BFieldNotProcess
    java.lang.Boolean mIsPeriodic();
    @BFieldSetNotProcess
    void _set_mSyncFlexTimeSecs(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSyncFlexTimeSecs();
    @BFieldNotProcess
    java.lang.Long mSyncFlexTimeSecs();
    @BFieldSetNotProcess
    void _set_mSyncRunTimeSecs(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSyncRunTimeSecs();
    @BFieldNotProcess
    java.lang.Long mSyncRunTimeSecs();
}

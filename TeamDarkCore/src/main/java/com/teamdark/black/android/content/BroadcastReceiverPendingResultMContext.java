package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.BroadcastReceiver$PendingResult")
public interface BroadcastReceiverPendingResultMContext {
    @BFieldSetNotProcess
    void _set_mAbortBroadcast(final Object value);
    @BFieldCheckNotProcess
    Field _check_mAbortBroadcast();
    @BFieldNotProcess
    java.lang.Boolean mAbortBroadcast();
    @BFieldSetNotProcess
    void _set_mFinished(final Object value);
    @BFieldCheckNotProcess
    Field _check_mFinished();
    @BFieldNotProcess
    java.lang.Boolean mFinished();
    @BFieldSetNotProcess
    void _set_mFlags(final Object value);
    @BFieldCheckNotProcess
    Field _check_mFlags();
    @BFieldNotProcess
    java.lang.Integer mFlags();
    @BFieldSetNotProcess
    void _set_mInitialStickyHint(final Object value);
    @BFieldCheckNotProcess
    Field _check_mInitialStickyHint();
    @BFieldNotProcess
    java.lang.Boolean mInitialStickyHint();
    @BFieldSetNotProcess
    void _set_mOrderedHint(final Object value);
    @BFieldCheckNotProcess
    Field _check_mOrderedHint();
    @BFieldNotProcess
    java.lang.Boolean mOrderedHint();
    @BFieldSetNotProcess
    void _set_mResultCode(final Object value);
    @BFieldCheckNotProcess
    Field _check_mResultCode();
    @BFieldNotProcess
    java.lang.Integer mResultCode();
    @BFieldSetNotProcess
    void _set_mResultData(final Object value);
    @BFieldCheckNotProcess
    Field _check_mResultData();
    @BFieldNotProcess
    java.lang.String mResultData();
    @BFieldSetNotProcess
    void _set_mResultExtras(final Object value);
    @BFieldCheckNotProcess
    Field _check_mResultExtras();
    @BFieldNotProcess
    android.os.Bundle mResultExtras();
    @BFieldSetNotProcess
    void _set_mSendingUser(final Object value);
    @BFieldCheckNotProcess
    Field _check_mSendingUser();
    @BFieldNotProcess
    java.lang.Integer mSendingUser();
    @BFieldSetNotProcess
    void _set_mToken(final Object value);
    @BFieldCheckNotProcess
    Field _check_mToken();
    @BFieldNotProcess
    android.os.IBinder mToken();
    @BFieldSetNotProcess
    void _set_mType(final Object value);
    @BFieldCheckNotProcess
    Field _check_mType();
    @BFieldNotProcess
    java.lang.Integer mType();
}

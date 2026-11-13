package com.teamdark.black.android.app;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.Activity")
public interface ActivityContext {
    @BMethodCheckNotProcess
    Method _check_onActivityResult(int int0, int int1, android.content.Intent Intent2);
    java.lang.Void onActivityResult(int int0, int int1, android.content.Intent Intent2);
    @BFieldSetNotProcess
    void _set_mActivityInfo(final Object value);
    @BFieldCheckNotProcess
    Field _check_mActivityInfo();
    @BFieldNotProcess
    android.content.pm.ActivityInfo mActivityInfo();
    @BFieldSetNotProcess
    void _set_mEmbeddedID(final Object value);
    @BFieldCheckNotProcess
    Field _check_mEmbeddedID();
    @BFieldNotProcess
    java.lang.String mEmbeddedID();
    @BFieldSetNotProcess
    void _set_mFinished(final Object value);
    @BFieldCheckNotProcess
    Field _check_mFinished();
    @BFieldNotProcess
    java.lang.Boolean mFinished();
    @BFieldSetNotProcess
    void _set_mParent(final Object value);
    @BFieldCheckNotProcess
    Field _check_mParent();
    @BFieldNotProcess
    android.app.Activity mParent();
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
    android.content.Intent mResultData();
    @BFieldSetNotProcess
    void _set_mToken(final Object value);
    @BFieldCheckNotProcess
    Field _check_mToken();
    @BFieldNotProcess
    android.os.IBinder mToken();
}

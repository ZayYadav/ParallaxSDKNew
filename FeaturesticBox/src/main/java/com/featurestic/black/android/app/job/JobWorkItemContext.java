package com.featurestic.black.android.app.job;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.job.JobWorkItem")
public interface JobWorkItemContext {
    @BMethodCheckNotProcess
    Method _check_getIntent();
    android.content.Intent getIntent();
    @BFieldSetNotProcess
    void _set_mDeliveryCount(final Object value);
    @BFieldCheckNotProcess
    Field _check_mDeliveryCount();
    @BFieldNotProcess
    java.lang.Integer mDeliveryCount();
    @BFieldSetNotProcess
    void _set_mGrants(final Object value);
    @BFieldCheckNotProcess
    Field _check_mGrants();
    @BFieldNotProcess
    java.lang.Object mGrants();
    @BFieldSetNotProcess
    void _set_mWorkId(final Object value);
    @BFieldCheckNotProcess
    Field _check_mWorkId();
    @BFieldNotProcess
    java.lang.Integer mWorkId();
}

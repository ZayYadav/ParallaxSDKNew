package com.featurestic.black.android.app.job;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.job.JobParameters")
public interface JobParametersContext {
    @BFieldSetNotProcess
    void _set_callback(final Object value);
    @BFieldCheckNotProcess
    Field _check_callback();
    @BFieldNotProcess
    android.os.IBinder callback();
    @BFieldSetNotProcess
    void _set_extras(final Object value);
    @BFieldCheckNotProcess
    Field _check_extras();
    @BFieldNotProcess
    android.os.PersistableBundle extras();
    @BFieldSetNotProcess
    void _set_jobId(final Object value);
    @BFieldCheckNotProcess
    Field _check_jobId();
    @BFieldNotProcess
    java.lang.Integer jobId();
}

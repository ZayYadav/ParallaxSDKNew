package com.teamdark.black.android.app.job;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.job.JobInfo")
public interface JobInfoContext {
    @BFieldSetNotProcess
    void _set_flexMillis(final Object value);
    @BFieldCheckNotProcess
    Field _check_flexMillis();
    @BFieldNotProcess
    java.lang.Long flexMillis();
    @BFieldSetNotProcess
    void _set_intervalMillis(final Object value);
    @BFieldCheckNotProcess
    Field _check_intervalMillis();
    @BFieldNotProcess
    java.lang.Long intervalMillis();
    @BFieldSetNotProcess
    void _set_jobId(final Object value);
    @BFieldCheckNotProcess
    Field _check_jobId();
    @BFieldNotProcess
    java.lang.Integer jobId();
    @BFieldSetNotProcess
    void _set_service(final Object value);
    @BFieldCheckNotProcess
    Field _check_service();
    @BFieldNotProcess
    android.content.ComponentName service();
}

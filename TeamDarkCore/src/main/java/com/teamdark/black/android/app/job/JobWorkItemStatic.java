package com.teamdark.black.android.app.job;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.job.JobWorkItem")
public interface JobWorkItemStatic {
    @BConstructorNotProcess
    com.teamdark.black.android.app.job.JobWorkItem _new(android.content.Intent Intent0);
}

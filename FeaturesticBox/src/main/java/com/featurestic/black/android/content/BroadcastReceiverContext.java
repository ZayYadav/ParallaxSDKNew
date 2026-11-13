package com.featurestic.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.featurestic.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.BroadcastReceiver")
public interface BroadcastReceiverContext {
    @BMethodCheckNotProcess
    Method _check_getPendingResult();
    android.content.BroadcastReceiver.PendingResult getPendingResult();
    @BMethodCheckNotProcess
    Method _check_setPendingResult(@BParamClassName("android.content.BroadcastReceiver$PendingResult") java.lang.Object pendingResult);
    java.lang.Void setPendingResult(@BParamClassName("android.content.BroadcastReceiver$PendingResult") java.lang.Object pendingResult);
}

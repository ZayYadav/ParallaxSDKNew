package com.teamdark.black.android.content;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.BroadcastReceiver$PendingResult")
public interface BroadcastReceiverPendingResultStatic {
    @BConstructorNotProcess
    android.content.BroadcastReceiver.PendingResult _new(int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, int type, boolean ordered, boolean sticky, android.os.IBinder token, int userId);
}

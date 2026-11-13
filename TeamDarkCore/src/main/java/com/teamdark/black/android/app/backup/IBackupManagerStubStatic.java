package com.teamdark.black.android.app.backup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.app.backup.IBackupManager$Stub")
public interface IBackupManagerStubStatic {
    @BMethodCheckNotProcess
    Method _check_asInterface(android.os.IBinder IBinder0);
    android.os.IInterface asInterface(android.os.IBinder IBinder0);
}

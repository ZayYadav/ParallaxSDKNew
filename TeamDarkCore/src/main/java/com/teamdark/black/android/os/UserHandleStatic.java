package com.teamdark.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.UserHandle")
public interface UserHandleStatic {
    @BMethodCheckNotProcess
    Method _check_myUserId();
    java.lang.Integer myUserId();
}

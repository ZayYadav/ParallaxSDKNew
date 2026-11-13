package com.teamdark.black.android.content.pm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.content.pm.UserInfo")
public interface UserInfoStatic {
    @BFieldSetNotProcess
    void _set_FLAG_PRIMARY(final Object value);
    @BFieldCheckNotProcess
    Field _check_FLAG_PRIMARY();
    @BFieldNotProcess
    java.lang.Integer FLAG_PRIMARY();
    @BConstructorNotProcess
    java.lang.Object _new(int id, java.lang.String name, int flags);
}

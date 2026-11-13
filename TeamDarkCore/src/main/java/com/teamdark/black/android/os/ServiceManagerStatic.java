package com.teamdark.black.android.os;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.teamdark.blackreflection.annotation.*;

@BClassNameNotProcess("android.os.ServiceManager")
public interface ServiceManagerStatic {
    @BMethodCheckNotProcess
    Method _check_addService(java.lang.String String0, android.os.IBinder IBinder1);
    java.lang.Void addService(java.lang.String String0, android.os.IBinder IBinder1);
    @BMethodCheckNotProcess
    Method _check_checkService();
    android.os.IBinder checkService();
    @BMethodCheckNotProcess
    Method _check_getIServiceManager();
    android.os.IInterface getIServiceManager();
    @BMethodCheckNotProcess
    Method _check_getService(java.lang.String name);
    android.os.IBinder getService(java.lang.String name);
    @BMethodCheckNotProcess
    Method _check_listServices();
    java.lang.String[] listServices();
    @BFieldSetNotProcess
    void _set_sCache(final Object value);
    @BFieldCheckNotProcess
    Field _check_sCache();
    @BFieldNotProcess
    java.util.Map<java.lang.String,android.os.IBinder> sCache();
    @BFieldSetNotProcess
    void _set_sServiceManager(final Object value);
    @BFieldCheckNotProcess
    Field _check_sServiceManager();
    @BFieldNotProcess
    android.os.IInterface sServiceManager();
}

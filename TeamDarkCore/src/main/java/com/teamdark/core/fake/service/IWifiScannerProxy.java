package com.teamdark.core.fake.service;

import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.Reflector;

public class IWifiScannerProxy extends BinderInvocationStub {

    public IWifiScannerProxy() {
        super(BRServiceManager.get().getService("wifiscanner"));
    }

    @Override
    protected Object getWho() {
        IBinder binder = BRServiceManager.get().getService("wifiscanner");
        if (binder == null) return null;
        try {
            return Reflector.on("android.net.wifi.IWifiScanner$Stub")
                    .method("asInterface", IBinder.class)
                    .call(binder);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        if (getWho() != null) {
            replaceSystemService("wifiscanner");
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException ignored) {
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException) {
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}

package com.teamdark.core.fake.service;


import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRIDeviceIdentifiersPolicyServiceStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.Md5Utils;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by @TeamDarkCore on 4/3/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class IDeviceIdentifiersPolicyProxy extends BinderInvocationStub {

    public IDeviceIdentifiersPolicyProxy() {
        super(BRServiceManager.get().getService("device_identifiers"));
    }

    @Override
    protected Object getWho() {
        return BRIDeviceIdentifiersPolicyServiceStub.get().asInterface(BRServiceManager.get().getService("device_identifiers"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("device_identifiers");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getSerialForPackage")
    public static class x extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return Md5Utils.fakeSerial();
        }
    }
}

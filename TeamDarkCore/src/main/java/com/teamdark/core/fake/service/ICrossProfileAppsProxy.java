package com.teamdark.core.fake.service;

import java.lang.reflect.Method;

import com.teamdark.black.android.content.pm.BRICrossProfileAppsStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;

public class ICrossProfileAppsProxy extends BinderInvocationStub {
    public ICrossProfileAppsProxy() {
        super(BRServiceManager.get().getService("crossprofileapps"));
    }

    @Override
    protected Object getWho() {
        return BRICrossProfileAppsStub.get().asInterface(BRServiceManager.get().getService("crossprofileapps"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("crossprofileapps");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getTargetUserProfiles")
    public static class getTargetUserProfiles extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("startActivityAsUser")
    public static class startActivityAsUser extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}

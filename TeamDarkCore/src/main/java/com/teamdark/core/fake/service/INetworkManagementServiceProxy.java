package com.teamdark.core.fake.service;

import static com.teamdark.core.app.BActivityThread.getUid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRINetworkManagementServiceStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.fake.service.base.UidMethodProxy;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by TeamDarkCore on 2022/3/5.
 */
public class INetworkManagementServiceProxy extends BinderInvocationStub {
    public static final String NAME = "network_management";

    public INetworkManagementServiceProxy() {
        super(BRServiceManager.get().getService(NAME));
    }

    @Override
    protected Object getWho() {
        return BRINetworkManagementServiceStub.get().asInterface(BRServiceManager.get().getService(NAME));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(NAME);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
        MethodParameterUtils.replaceFirstUid(args);
        MethodParameterUtils.replaceLastUid(args);
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException ignored) {
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException) return null;
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
        addMethodHook(new UidMethodProxy("setUidCleartextNetworkPolicy", 0));
        addMethodHook(new UidMethodProxy("setUidMeteredNetworkBlacklist", 0));
        addMethodHook(new UidMethodProxy("setUidMeteredNetworkWhitelist", 0));
    }

    @ProxyMethod("getNetworkStatsUidDetail")
    public static class GetNetworkStatsUidDetail extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstUid(args);
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }

}

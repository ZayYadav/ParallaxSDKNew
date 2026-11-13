package com.teamdark.core.fake.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.android.view.BRIGraphicsStatsStub;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by @TeamDarkCore on 4/13/21.
 */
public class IGraphicsStatsProxy extends BinderInvocationStub {

    public IGraphicsStatsProxy() {
        super(BRServiceManager.get().getService("graphicsstats"));
    }

    @Override
    protected Object getWho() {
        return BRIGraphicsStatsStub.get().asInterface(BRServiceManager.get().getService("graphicsstats"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("graphicsstats");
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
            if (e.getTargetException() instanceof SecurityException) {
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @ProxyMethod("requestBufferForProcess")
    public static class RequestBufferForProcess extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceFirstAppPkg(args);
            return method.invoke(who, args);
        }
    }
}

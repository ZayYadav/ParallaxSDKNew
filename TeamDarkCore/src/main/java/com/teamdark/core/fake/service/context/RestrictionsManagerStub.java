package com.teamdark.core.fake.service.context;

import android.content.Context;
import android.os.Bundle;
import com.teamdark.black.android.content.BRIRestrictionsManagerStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RestrictionsManagerStub extends BinderInvocationStub {

    public RestrictionsManagerStub() {
        super(BRServiceManager.get().getService(Context.RESTRICTIONS_SERVICE));
    }

    @Override
    protected Object getWho() {
        return BRIRestrictionsManagerStub.get().asInterface(BRServiceManager.get().getService(Context.RESTRICTIONS_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.RESTRICTIONS_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
        try {
            return super.invoke(proxy, method, args);
        } catch (SecurityException ignored) {
            if (method.getReturnType() == Bundle.class) return new Bundle();
            if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException) {
                if (method.getReturnType() == Bundle.class) return new Bundle();
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @ProxyMethod("getApplicationRestrictions")
    public static class GetApplicationRestrictions extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            if (args != null && args.length > 0 && args[0] instanceof String) {
                args[0] = TeamDarkCoreEngine.getHostPkg();
            }
            return method.invoke(who, args);
        }
    }
}

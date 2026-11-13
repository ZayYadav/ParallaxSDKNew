package com.teamdark.core.fake.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.com.android.internal.telephony.BRITelephonyRegistryStub;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Created by @TeamDarkCore on 2021/5/17.
 * Fixed Android 12-16 listenWithEventList & AttributionSource SecurityException.
 */
public class ITelephonyRegistryProxy extends BinderInvocationStub {
    public ITelephonyRegistryProxy() {
        super(BRServiceManager.get().getService("telephony.registry"));
    }

    @Override
    protected Object getWho() {
        return BRITelephonyRegistryStub.get().asInterface(BRServiceManager.get().getService("telephony.registry"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("telephony.registry");
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
        if (args != null && BRAttributionSource.getRealClass() != null) {
            String attrClassName = BRAttributionSource.getRealClass().getName();
            for (Object arg : args) {
                if (arg != null && attrClassName.equals(arg.getClass().getName())) {
                    ContextCompat.fixAttributionSourceState(arg, TeamDarkCoreEngine.getHostUid());
                }
            }
        }
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

    @ProxyMethod("listenWithEventList")
    public static class ListenWithEventList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceAllAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("listenForSubscriber")
    public static class ListenForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceAllAppPkg(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("listen")
    public static class Listen extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceAllAppPkg(args);
            return method.invoke(who, args);
        }
    }
}

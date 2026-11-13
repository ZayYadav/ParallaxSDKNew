package com.teamdark.core.fake.service;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.app.BRIAlarmManagerStub;
import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.fake.hook.ProxyMethods;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Created by @TeamDarkCore on 4/3/21.
 */
public class IAlarmManagerProxy extends BinderInvocationStub {

    public IAlarmManagerProxy() {
        super(BRServiceManager.get().getService(Context.ALARM_SERVICE));
    }

    @Override
    protected Object getWho() {
        return BRIAlarmManagerStub.get().asInterface(BRServiceManager.get().getService(Context.ALARM_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.ALARM_SERVICE);
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
            if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return true;
            if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException) {
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return true;
                if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

    @ProxyMethod("set")
    public static class Set extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return 0;
        }
    }

    @ProxyMethods({"canScheduleExactAlarms", "hasScheduleExactAlarm"})
    public static class CanScheduleExactAlarms extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return true;
        }
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }
}

package com.teamdark.core.fake.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.black.android.telephony.BRTelephonyManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.ClassInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.fake.hook.ProxyMethods;
import com.teamdark.core.utils.Md5Utils;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Created by @TeamDarkCore on 2022/2/26.
 */
public class IPhoneSubInfoProxy extends ClassInvocationStub {
    public static final String TAG = "IPhoneSubInfoProxy";

    public IPhoneSubInfoProxy() {
        if (BRTelephonyManager.get()._check_sServiceHandleCacheEnabled() != null) {
            BRTelephonyManager.get()._set_sServiceHandleCacheEnabled(true);
        }
        if (BRTelephonyManager.get()._check_getSubscriberInfoService() != null) {
            BRTelephonyManager.get().getSubscriberInfoService();
        }
    }

    @Override
    protected Object getWho() {
        return BRTelephonyManager.get().sIPhoneSubInfo();
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        BRTelephonyManager.get()._set_sIPhoneSubInfo(proxyInvocation);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceAllAppPkg(args);
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

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethods({"getLine1Number", "getLine1NumberForSubscriber", "getMsisdn", "getMsisdnForSubscriber"})
    public static class getLine1NumberForSubscriber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethods({"getSubscriberId", "getSubscriberIdForSubscriber"})
    public static class GetSubscriberId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return Md5Utils.fakeImsi();
        }
    }

    @ProxyMethods({"getDeviceId", "getDeviceIdForPhone", "getImeiForSubscriber", "getNaiForSubscriber"})
    public static class GetDeviceId extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return Md5Utils.fakeImei();
        }
    }

    @ProxyMethods({"getIccSerialNumber", "getIccSerialNumberForSubscriber"})
    public static class GetIccSerialNumber extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return Md5Utils.fakeSerial();
        }
    }
}

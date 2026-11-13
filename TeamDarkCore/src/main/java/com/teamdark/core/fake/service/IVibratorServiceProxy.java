package com.teamdark.core.fake.service;

import android.content.Context;
import android.os.IBinder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.black.android.os.BRIVibratorManagerServiceStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.com.android.internal.os.BRIVibratorServiceStub;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.BuildCompat;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Created by @TeamDarkCore on 2022/3/7.
 * Fixed Android 12 (API 31) vs 12L+ (API 32+) service binder mismatch and AttributionSource.
 */
public class IVibratorServiceProxy extends BinderInvocationStub {
    private static final String NAME = BuildCompat.isS() ? "vibrator_manager" : Context.VIBRATOR_SERVICE;

    private static IBinder resolveVibratorBinder() {
        IBinder b = BRServiceManager.get().getService(NAME);
        if (b == null && BuildCompat.isS()) {
            b = BRServiceManager.get().getService(Context.VIBRATOR_SERVICE);
        }
        return b;
    }

    public IVibratorServiceProxy() {
        super(resolveVibratorBinder());
    }

    @Override
    protected Object getWho() {
        IBinder service = resolveVibratorBinder();
        if (BuildCompat.isS() && BRServiceManager.get().getService("vibrator_manager") != null) {
            return BRIVibratorManagerServiceStub.get().asInterface(service);
        }
        return BRIVibratorServiceStub.get().asInterface(service);
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        if (BuildCompat.isS() && BRServiceManager.get().getService("vibrator_manager") != null) {
            replaceSystemService("vibrator_manager");
        } else {
            replaceSystemService(Context.VIBRATOR_SERVICE);
        }
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodParameterUtils.replaceFirstUid(args);
        MethodParameterUtils.replaceLastUid(args);
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
        } catch (SecurityException | IllegalArgumentException ignored) {
            if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
            if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
            return null;
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof SecurityException || e.getTargetException() instanceof IllegalArgumentException) {
                if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
                if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
                return null;
            }
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }
}

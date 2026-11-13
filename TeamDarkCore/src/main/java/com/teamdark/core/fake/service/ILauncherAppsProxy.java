package com.teamdark.core.fake.service;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.utils.compat.ContextCompat;

import com.teamdark.black.android.content.pm.BRILauncherAppsStub;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by @TeamDarkCore on 4/13/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ILauncherAppsProxy extends BinderInvocationStub {

    public ILauncherAppsProxy() {
        super(BRServiceManager.get().getService(Context.LAUNCHER_APPS_SERVICE));
    }

    @Override
    protected Object getWho() {
        return BRILauncherAppsStub.get().asInterface(BRServiceManager.get().getService(Context.LAUNCHER_APPS_SERVICE));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService(Context.LAUNCHER_APPS_SERVICE);
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    protected void onBindMethod() {
        super.onBindMethod();
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
            if (e.getTargetException() instanceof SecurityException) return null;
            throw e.getTargetException() != null ? e.getTargetException() : e;
        }
    }

}

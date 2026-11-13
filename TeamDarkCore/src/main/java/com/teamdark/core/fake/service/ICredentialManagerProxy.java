package com.teamdark.core.fake.service;

import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.utils.MethodParameterUtils;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Android 14+ (API 34+) CredentialManager Service proxy.
 * Normalizes virtual callingPackage / AttributionSource to the host identity
 * so modern Google Sign-In, One Tap, and Passkeys work inside virtual apps.
 */
public class ICredentialManagerProxy extends BinderInvocationStub {
    private static final String SERVICE_NAME = "credential";

    public ICredentialManagerProxy() {
        super(BRServiceManager.get().getService(SERVICE_NAME));
    }

    @Override
    protected Object getWho() {
        IBinder binder = BRServiceManager.get().getService(SERVICE_NAME);
        if (binder == null) {
            return null;
        }
        try {
            Class<?> stubClass = Class.forName("android.credentials.ICredentialManager$Stub");
            Method asInterface = stubClass.getMethod("asInterface", IBinder.class);
            return asInterface.invoke(null, binder);
        } catch (Throwable ignored) {
            return null;
        }
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        if (baseInvocation != null) {
            replaceSystemService(SERVICE_NAME);
        }
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (args != null) {
            MethodParameterUtils.replaceAllAppPkg(args);
            for (Object arg : args) {
                if (arg != null && "android.content.AttributionSource".equals(arg.getClass().getName())) {
                    ContextCompat.fixAttributionSourceState(arg, TeamDarkCoreEngine.getHostUid());
                }
            }
        }
        return super.invoke(proxy, method, args);
    }
}

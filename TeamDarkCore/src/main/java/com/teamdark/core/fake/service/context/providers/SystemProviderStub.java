package com.teamdark.core.fake.service.context.providers;

import android.os.Bundle;
import android.os.IInterface;
import java.lang.reflect.Method;
import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.ClassInvocationStub;
import com.teamdark.core.utils.Md5Utils;
import com.teamdark.core.utils.compat.ContextCompat;

/**
 * Created by @TeamDarkCore on 4/8/21.
 */
public class SystemProviderStub extends ClassInvocationStub implements BContentProvider {
    private IInterface mBase;

    @Override
    public IInterface wrapper(IInterface contentProviderProxy, String appPkg) {
        mBase = contentProviderProxy;
        injectHook();
        return (IInterface) getProxyInvocation();
    }

    @Override
    protected Object getWho() {
        return mBase;
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {}

    @Override
    protected void onBindMethod() {}

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("asBinder".equals(method.getName())) {
            return method.invoke(mBase, args);
        }
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof String) {
                args[0] = TeamDarkCoreEngine.getHostPkg();
            } else if (arg != null && BRAttributionSource.getRealClass() != null && arg.getClass().getName().equals(BRAttributionSource.getRealClass().getName())) {
                ContextCompat.fixAttributionSourceState(arg, TeamDarkCoreEngine.getHostUid());
            }
        }
        if ("call".equals(method.getName()) && args != null && args.length >= 3 && TeamDarkCoreEngine.get().isTeamDarkProcess()) {
            Object mObj = args[args.length - 3];
            Object kObj = args[args.length - 2];
            if (mObj instanceof String && kObj instanceof String) {
                String callMethod = (String) mObj;
                String key = (String) kObj;
                if (("GET_global".equals(callMethod) || "GET_secure".equals(callMethod))
                        && ("adb_enabled".equals(key)
                        || "development_settings_enabled".equals(key)
                        || "mock_location".equals(key))) {
                    Bundle bundle = new Bundle();
                    bundle.putString("value", "0");
                    return bundle;
                }
                if ("GET_secure".equals(callMethod) && "android_id".equals(key)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("value", Md5Utils.fakeAndroidId());
                    return bundle;
                }
            }
        }
        try {
            return method.invoke(mBase, args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            throw cause != null ? cause : e;
        }
    }
}

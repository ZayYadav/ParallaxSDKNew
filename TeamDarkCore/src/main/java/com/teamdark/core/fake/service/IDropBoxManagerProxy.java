package com.teamdark.core.fake.service;

import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.com.android.internal.os.BRIDropBoxManagerServiceStub;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;

public class IDropBoxManagerProxy extends BinderInvocationStub {
    public IDropBoxManagerProxy() {
        super(BRServiceManager.get().getService("dropbox"));
    }

    @Override
    protected Object getWho() {
        return BRIDropBoxManagerServiceStub.get().asInterface(BRServiceManager.get().getService("dropbox"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("dropbox");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("getNextEntryWithAttribution")
    public static class getNextEntryWithAttribution extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }

    @ProxyMethod("unregisterAudioFocusClient")
    public static class unregisterAudioFocusClient extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}

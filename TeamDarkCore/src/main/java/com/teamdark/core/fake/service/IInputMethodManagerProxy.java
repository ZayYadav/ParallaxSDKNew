package com.teamdark.core.fake.service;

import android.util.Log;
import android.view.inputmethod.EditorInfo;

import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.com.android.internal.view.BRIInputMethodManagerStub;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.ArrayUtils;
import com.teamdark.core.utils.MethodParameterUtils;

public class IInputMethodManagerProxy extends BinderInvocationStub {
    public IInputMethodManagerProxy() {
        super(BRServiceManager.get().getService("input_method"));
    }

    @Override
    protected Object getWho() {
        return BRIInputMethodManagerStub.get().asInterface(BRServiceManager.get().getService("input_method"));
    }

    @Override
    protected void inject(Object baseInvocation, Object proxyInvocation) {
        replaceSystemService("input_method");
    }

    @Override
    public boolean isBadEnv() {
        return false;
    }

    @ProxyMethod("startInputOrWindowGainedFocus")
    public static class startInputOrWindowGainedFocus extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            Log.d(TAG, "startInputOrWindowGainedFocus called");
            int index = ArrayUtils.indexOfFirst(args, EditorInfo.class);
            if (index != -1) {
                EditorInfo attribute = (EditorInfo) args[index];
                attribute.packageName = TeamDarkCoreEngine.getHostPkg();
                args[index] = attribute;
            }
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("startInput")
    public static class startInput extends startInputOrWindowGainedFocus {
    }

    @ProxyMethod("windowGainedFocus")
    public static class windowGainedFocus extends startInputOrWindowGainedFocus {
    }

    @ProxyMethod("getInputMethodList")
    public static class getInputMethodList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }

    @ProxyMethod("getEnabledInputMethodList")
    public static class getEnabledInputMethodList extends MethodHook {
        @Override
        protected Object hook(Object who, Method method, Object[] args) throws Throwable {
            MethodParameterUtils.replaceLastUserId(args);
            return method.invoke(who, args);
        }
    }
}

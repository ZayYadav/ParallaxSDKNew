package com.teamdark.core.fake.service;

import android.content.ComponentName;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.black.android.view.BRIAutoFillManagerStub;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.proxy.ProxyManifest;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by @TeamDarkCore on 4/8/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class IAutofillManagerProxy extends BinderInvocationStub {
	public static final String TAG = "AutofillManagerStub";

	public IAutofillManagerProxy() {
		super(BRServiceManager.get().getService("autofill"));
	}

	@Override
	protected Object getWho() {
		return BRIAutoFillManagerStub.get().asInterface(BRServiceManager.get().getService("autofill"));
	}

	@Override
	protected void inject(Object baseInvocation, Object proxyInvocation) {
		replaceSystemService("autofill");
	}

	@Override
	public boolean isBadEnv() {
		return false;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodParameterUtils.replaceAllAppPkg(args);
		MethodParameterUtils.replaceLastUserId(args);
		try {
			return super.invoke(proxy, method, args);
		} catch (SecurityException ignored) {
			return null;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof SecurityException) return null;
			throw e.getTargetException() != null ? e.getTargetException() : e;
		}
	}

	@ProxyMethod("startSession")
	public static class StartSession extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					if (args[i] == null) continue;
					if (args[i] instanceof ComponentName) {
						args[i] = new ComponentName(TeamDarkCoreEngine.getHostPkg(),ProxyManifest.getProxyActivity(BActivityThread.getAppPid()));
					}
				}
			}
			return method.invoke(who, args);
		}
	}
}

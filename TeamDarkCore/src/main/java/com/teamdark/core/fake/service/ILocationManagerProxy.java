package com.teamdark.core.fake.service;

import android.content.Context;
import android.location.LocationManager;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.teamdark.black.android.content.BRAttributionSource;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.utils.compat.ContextCompat;
import java.util.Arrays;
import java.util.Objects;

import com.teamdark.black.android.location.BRILocationListener;
import com.teamdark.black.android.location.BRILocationManagerStub;
import com.teamdark.black.android.location.provider.BRProviderProperties;
import com.teamdark.black.android.location.provider.ProviderProperties;
import com.teamdark.black.android.os.BRServiceManager;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.entity.location.BLocation;
import com.teamdark.core.fake.frameworks.BLocationManager;
import com.teamdark.core.fake.hook.BinderInvocationStub;
import com.teamdark.core.fake.hook.MethodHook;
import com.teamdark.core.fake.hook.ProxyMethod;
import com.teamdark.core.utils.MethodParameterUtils;

/**
 * Created by @TeamDarkCore on 4/8/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ILocationManagerProxy extends BinderInvocationStub {
	public static final String TAG = "ILocationManagerProxy";

	public ILocationManagerProxy() {
		super(BRServiceManager.get().getService(Context.LOCATION_SERVICE));
	}

	@Override
	protected Object getWho() {
		return BRILocationManagerStub.get()
			.asInterface(BRServiceManager.get().getService(Context.LOCATION_SERVICE));
	}

	@Override
	protected void inject(Object baseInvocation, Object proxyInvocation) {
		replaceSystemService(Context.LOCATION_SERVICE);
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
			if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
			if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
			return null;
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof SecurityException) {
				if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class) return false;
				if (method.getReturnType() == int.class || method.getReturnType() == Integer.class) return 0;
				return null;
			}
			throw e.getTargetException() != null ? e.getTargetException() : e;
		}
	}

	@ProxyMethod("registerGnssStatusCallback")
	public static class RegisterGnssStatusCallback extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			// todo
			return true;
		}
	}

	@ProxyMethod("getLastLocation")
	public static class GetLastLocation extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (BLocationManager.isFakeLocationEnable()) {
				return BLocationManager.get().getLocation(BActivityThread.getUserId(), BActivityThread.getAppPackageName()).convert2SystemLocation();
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("getLastKnownLocation")
	public static class GetLastKnownLocation extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (BLocationManager.isFakeLocationEnable()) {
				return BLocationManager.get().getLocation(BActivityThread.getUserId(), BActivityThread.getAppPackageName()).convert2SystemLocation();
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("requestLocationUpdates")
	public static class RequestLocationUpdates extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (BLocationManager.isFakeLocationEnable()) {
				if (args[1] instanceof IInterface) {
					IInterface listener = (IInterface) args[1];
					BLocationManager.get().requestLocationUpdates(listener.asBinder());
					return 0;
				}
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("removeUpdates")
	public static class RemoveUpdates extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (args[0] instanceof IInterface) {
				IInterface listener = (IInterface) args[0];
				BLocationManager.get().removeUpdates(listener.asBinder());
				return 0;
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("getProviderProperties")
	public static class GetProviderProperties extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			Object providerProperties = method.invoke(who, args);
			if (BLocationManager.isFakeLocationEnable()) {
				BRProviderProperties.get(providerProperties)._set_mHasNetworkRequirement(false);
				if (BLocationManager.get().getCell(BActivityThread.getUserId(), BActivityThread.getAppPackageName()) == null) {
					BRProviderProperties.get(providerProperties)._set_mHasCellRequirement(false);
				}
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("removeGpsStatusListener")
	public static class RemoveGpsStatusListener extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			// todo
			return 0;
		}
	}

	@ProxyMethod("getBestProvider")
	public static class GetBestProvider extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			if (BLocationManager.isFakeLocationEnable()) {
				return LocationManager.GPS_PROVIDER;
			}
			return method.invoke(who, args);
		}
	}

	@ProxyMethod("getAllProviders")
	public static class GetAllProviders extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			return Arrays.asList(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER);
		}
	}

	@ProxyMethod("isProviderEnabledForUser")
	public static class isProviderEnabledForUser extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			String provider = (String) args[0];
			return Objects.equals(provider, LocationManager.GPS_PROVIDER);
		}
	}

	@ProxyMethod("setExtraLocationControllerPackageEnabled")
	public static class setExtraLocationControllerPackageEnabled extends MethodHook {

		@Override
		protected Object hook(Object who, Method method, Object[] args) throws Throwable {
			return 0;
		}
	}
}

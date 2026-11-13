package com.teamdark.core.fake.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.teamdark.black.android.app.ActivityThreadActivityClientRecordContext;
import com.teamdark.black.android.app.BRActivityClient;
import com.teamdark.black.android.app.BRActivityClientActivityClientControllerSingleton;
import com.teamdark.black.android.app.BRActivityManagerNative;
import com.teamdark.black.android.app.BRActivityThread;
import com.teamdark.black.android.app.BRActivityThreadActivityClientRecord;
import com.teamdark.black.android.app.BRActivityThreadCreateServiceData;
import com.teamdark.black.android.app.BRActivityThreadH;
import com.teamdark.black.android.app.BRIActivityManager;
import com.teamdark.black.android.app.servertransaction.BRClientTransaction;
import com.teamdark.black.android.app.servertransaction.BRLaunchActivityItem;
import com.teamdark.black.android.app.servertransaction.LaunchActivityItemContext;
import com.teamdark.black.android.os.BRHandler;
import com.teamdark.core.TeamDarkCoreEngine;
import com.teamdark.core.app.BActivityThread;
import com.teamdark.core.compat.auth.GoogleSignInTokenHelper;
import com.teamdark.core.fake.hook.IInjectHook;
import com.teamdark.core.jnihook.jni.JniHook;
import com.teamdark.core.proxy.ProxyManifest;
import com.teamdark.core.proxy.record.ProxyActivityRecord;
import com.teamdark.core.utils.Slog;
import com.teamdark.core.utils.compat.BuildCompat;

/**
 * ActivityThread.mH callback that replaces ProxyActivity$P0..P24 with the target
 * virtual game Activity before Android executes the launch transaction.
 */
public class HCallbackStub implements IInjectHook, Handler.Callback {
    public static final String TAG = "HCallbackStub";

    private Handler.Callback mOtherCallback;
    private final AtomicBoolean mBeing = new AtomicBoolean(false);

    private Handler.Callback getHCallback() {
        return BRHandler.get(getH()).mCallback();
    }

    private Handler getH() {
        Object currentActivityThread = TeamDarkCoreEngine.mainThread();
        return BRActivityThread.get(currentActivityThread).mH();
    }

    @Override
    public void injectHook() {
        mOtherCallback = getHCallback();
        if (mOtherCallback != null
                && (mOtherCallback == this
                || mOtherCallback.getClass().getName().equals(getClass().getName()))) {
            mOtherCallback = null;
        }
        BRHandler.get(getH())._set_mCallback(this);
    }

    @Override
    public boolean isBadEnv() {
        Handler.Callback hCallback = getHCallback();
        return hCallback != null && hCallback != this;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (!mBeing.getAndSet(true)) {
            try {
                if (BuildCompat.isPie()) {
                    if (msg.what == BRActivityThreadH.get().EXECUTE_TRANSACTION()) {
                        try {
                            GoogleSignInTokenHelper.inspectClientTransaction(msg.obj);
                        } catch (Throwable ignored) {
                        }
                        if (handleLaunchActivity(msg.obj)) {
                            getH().sendMessageAtFrontOfQueue(Message.obtain(msg));
                            return true;
                        }
                    }
                } else {
                    if (msg.what == BRActivityThreadH.get().LAUNCH_ACTIVITY()) {
                        if (handleLaunchActivity(msg.obj)) {
                            getH().sendMessageAtFrontOfQueue(Message.obtain(msg));
                            return true;
                        }
                    }
                }
                if (msg.what == BRActivityThreadH.get().CREATE_SERVICE()) {
                    return handleCreateService(msg.obj);
                }
                if (mOtherCallback != null) {
                    return mOtherCallback.handleMessage(msg);
                }
                return false;
            } finally {
                mBeing.set(false);
            }
        }
        return false;
    }

    private Object getLaunchActivityItem(Object clientTransaction) {
        if (clientTransaction == null) {
            return null;
        }
        try {
            List<Object> callbacks = GoogleSignInTokenHelper.extractClientTransactionItems(clientTransaction);
            if (callbacks == null) {
                callbacks = BRClientTransaction.get(clientTransaction).mActivityCallbacks();
            }
            if (callbacks == null) {
                return null;
            }
            Class<?> launchClass = BRLaunchActivityItem.getRealClass();
            for (Object item : callbacks) {
                if (item == null) {
                    continue;
                }
                String clsName = item.getClass().getName();
                if ((launchClass != null && (launchClass.isInstance(item) || launchClass.getName().equals(clsName)))
                        || clsName.endsWith(".LaunchActivityItem")) {
                    return item;
                }
            }
        } catch (Throwable error) {
            Slog.e(TAG, "Unable to inspect ClientTransaction launch item: " + error);
        }
        return null;
    }

    private Intent extractStubIntent(Object launchItem) {
        if (launchItem == null) return null;
        try {
            Intent i = BRLaunchActivityItem.get(launchItem).mIntent();
            if (i != null) return i;
        } catch (Throwable ignored) {
        }
        Class<?> cls = launchItem.getClass();
        while (cls != null && cls != Object.class) {
            try {
                Field f = cls.getDeclaredField("mIntent");
                f.setAccessible(true);
                Object v = f.get(launchItem);
                if (v instanceof Intent) return (Intent) v;
            } catch (Throwable ignored) {
            }
            for (Field f : cls.getDeclaredFields()) {
                if (Intent.class.isAssignableFrom(f.getType())) {
                    try {
                        f.setAccessible(true);
                        Object v = f.get(launchItem);
                        if (v instanceof Intent) return (Intent) v;
                    } catch (Throwable ignored) {
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private Boolean handleLaunchActivity(Object client) {
        if (client == null) {
            return false;
        }

        Object launchItem;
        if (BuildCompat.isPie()) {
            launchItem = getLaunchActivityItem(client);
            if (launchItem == null) {
                return false;
            }
        } else {
            launchItem = client;
        }

        Intent stubIntent;
        IBinder token;
        if (BuildCompat.isPie()) {
            stubIntent = extractStubIntent(launchItem);
            token = extractActivityToken(client, launchItem);
        } else {
            ActivityThreadActivityClientRecordContext recordContext =
                    BRActivityThreadActivityClientRecord.get(launchItem);
            stubIntent = recordContext.intent();
            token = recordContext.token();
        }

        if (stubIntent == null) {
            return false;
        }

        ProxyActivityRecord stubRecord = ProxyActivityRecord.create(stubIntent);
        ActivityInfo activityInfo = stubRecord.mActivityInfo;
        if (activityInfo == null) {
            return false;
        }

        Intent targetIntent = stubRecord.mTarget;
        BActivityThread activityThread = BActivityThread.currentActivityThread();

        if (BActivityThread.getAppConfig() == null) {
            TeamDarkCoreEngine.getBActivityManager().restartProcess(
                    activityInfo.packageName,
                    activityInfo.processName,
                    stubRecord.mUserId);

            Intent packageLaunchIntent = TeamDarkCoreEngine.getBPackageManager()
                    .getLaunchIntentForPackage(activityInfo.packageName, stubRecord.mUserId);

            Intent restartTarget = packageLaunchIntent != null
                    ? packageLaunchIntent : targetIntent;
            if (restartTarget == null) {
                return false;
            }

            stubIntent.setExtrasClassLoader(getClass().getClassLoader());
            ProxyActivityRecord.saveStub(
                    stubIntent,
                    restartTarget,
                    stubRecord.mActivityInfo,
                    stubRecord.mActivityRecord,
                    stubRecord.mUserId);
            writeLaunchRecord(launchItem, stubIntent, activityInfo, null);
            return true;
        }

        if (!activityThread.isInit()) {
            activityThread.bindApplication(
                    activityInfo.packageName, activityInfo.processName);
            return true;
        }

        // Best-effort framework Application sync (never aborts launch!)
        try {
            activityThread.ensureFrameworkInitialApplication();
        } catch (Throwable ignored) {
        }

        if (targetIntent == null) {
            return false;
        }

        if (BActivityThread.getApplication() != null
                && BActivityThread.getApplication().getClassLoader() != null) {
            ClassLoader guestCl = BActivityThread.getApplication().getClassLoader();
            targetIntent.setExtrasClassLoader(guestCl);
            try {
                android.os.Bundle extras = targetIntent.getExtras();
                if (extras != null) {
                    extras.setClassLoader(guestCl);
                    for (String key : extras.keySet()) {
                        Object val = extras.get(key);
                        if (val instanceof android.os.Bundle) {
                            ((android.os.Bundle) val).setClassLoader(guestCl);
                        }
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        int taskId = resolveTaskIdSafely(token);
        TeamDarkCoreEngine.getBActivityManager().onActivityCreated(
                taskId, token, stubRecord.mActivityRecord);

        if (BuildCompat.isTiramisu()) {
            writeLaunchRecord(launchItem, targetIntent, activityInfo, null);
            Object launchingRecord = null;
            try {
                launchingRecord = BRActivityThread.get(TeamDarkCoreEngine.mainThread())
                        .getLaunchingActivity(token);
            } catch (Throwable ignored) {
            }
            if (launchingRecord != null) {
                writeLaunchRecord(launchItem, targetIntent, activityInfo, launchingRecord);
            }
        } else if (BuildCompat.isS()) {
            Object launchingRecord = null;
            try {
                launchingRecord = BRActivityThread.get(TeamDarkCoreEngine.mainThread())
                        .getLaunchingActivity(token);
            } catch (Throwable ignored) {
            }
            writeLaunchRecord(launchItem, targetIntent, activityInfo, launchingRecord);
            checkActivityClient();
        } else if (BuildCompat.isPie()) {
            writeLaunchRecord(launchItem, targetIntent, activityInfo, null);
        } else {
            ActivityThreadActivityClientRecordContext recordContext =
                    BRActivityThreadActivityClientRecord.get(launchItem);
            recordContext._set_intent(targetIntent);
            recordContext._set_activityInfo(activityInfo);
        }
        return false;
    }

    private void writeLaunchRecord(
            Object launchItem,
            Intent targetIntent,
            ActivityInfo activityInfo,
            Object launchingRecord) {
        if (launchItem == null || targetIntent == null || activityInfo == null) {
            return;
        }

        if (BuildCompat.isPie()) {
            try {
                LaunchActivityItemContext launchContext = BRLaunchActivityItem.get(launchItem);
                launchContext._set_mIntent(targetIntent);
                launchContext._set_mInfo(activityInfo);
            } catch (Throwable ignored) {
            }
            // Direct reflection fallback for Android 14–16 where mIntent / mInfo may be final
            setFieldDirect(launchItem, "mIntent", Intent.class, targetIntent);
            setFieldDirect(launchItem, "mInfo", ActivityInfo.class, activityInfo);
        } else {
            ActivityThreadActivityClientRecordContext recordContext =
                    BRActivityThreadActivityClientRecord.get(launchItem);
            recordContext._set_intent(targetIntent);
            recordContext._set_activityInfo(activityInfo);
        }

        if (launchingRecord != null) {
            try {
                ActivityThreadActivityClientRecordContext recordContext =
                        BRActivityThreadActivityClientRecord.get(launchingRecord);
                recordContext._set_intent(targetIntent);
                recordContext._set_activityInfo(activityInfo);
                recordContext._set_packageInfo(
                        BActivityThread.currentActivityThread().getPackageInfo());
            } catch (Throwable ignored) {
            }
        }
    }

    private static void setFieldDirect(Object target, String fieldName, Class<?> expectedType, Object value) {
        if (target == null || value == null) return;
        Class<?> cls = target.getClass();
        while (cls != null && cls != Object.class) {
            try {
                Field f = cls.getDeclaredField(fieldName);
                JniHook.setAccessible(cls, f);
                f.setAccessible(true);
                f.set(target, value);
                return;
            } catch (Throwable ignored) {
            }
            cls = cls.getSuperclass();
        }
        cls = target.getClass();
        while (cls != null && cls != Object.class) {
            for (Field f : cls.getDeclaredFields()) {
                if (expectedType.isAssignableFrom(f.getType())) {
                    try {
                        JniHook.setAccessible(cls, f);
                        f.setAccessible(true);
                        f.set(target, value);
                        return;
                    } catch (Throwable ignored) {
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }

    private boolean handleCreateService(Object data) {
        if (BActivityThread.getAppConfig() != null) {
            String appPackageName = BActivityThread.getAppPackageName();
            if (appPackageName == null || data == null) {
                return false;
            }

            ServiceInfo serviceInfo = BRActivityThreadCreateServiceData.get(data).info();
            if (serviceInfo == null || serviceInfo.name == null) {
                return false;
            }

            if (!serviceInfo.name.equals(ProxyManifest.getProxyService(BActivityThread.getAppPid()))
                    && !serviceInfo.name.equals(
                    ProxyManifest.getProxyJobService(BActivityThread.getAppPid()))) {
                Slog.d(TAG, "handleCreateService: " + data);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(appPackageName, serviceInfo.name));
                TeamDarkCoreEngine.getBActivityManager().startService(
                        intent, null, false, BActivityThread.getUserId());
                return true;
            }
        }
        return false;
    }

    private IBinder extractActivityToken(Object client, Object launchItem) {
        try {
            IBinder t = BRClientTransaction.get(client).mActivityToken();
            if (t != null) return t;
        } catch (Throwable ignored) {
        }
        try {
            java.lang.reflect.Method m = client.getClass().getMethod("getActivityToken");
            Object res = m.invoke(client);
            if (res instanceof IBinder) return (IBinder) res;
        } catch (Throwable ignored) {
        }
        if (launchItem != null) {
            try {
                java.lang.reflect.Method m = launchItem.getClass().getMethod("getActivityToken");
                Object res = m.invoke(launchItem);
                if (res instanceof IBinder) return (IBinder) res;
            } catch (Throwable ignored) {
            }
            Class<?> cls = launchItem.getClass();
            while (cls != null && cls != Object.class) {
                try {
                    java.lang.reflect.Field f = cls.getDeclaredField("mActivityToken");
                    f.setAccessible(true);
                    Object res = f.get(launchItem);
                    if (res instanceof IBinder) return (IBinder) res;
                } catch (Throwable ignored) {
                }
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    private int resolveTaskIdSafely(IBinder token) {
        if (token == null) return 0;
        try {
            Integer id = BRIActivityManager.get(BRActivityManagerNative.get().getDefault())
                    .getTaskForActivity(token, false);
            if (id != null && id != -1) {
                return id;
            }
        } catch (Throwable ignored) {
        }
        try {
            Object client = BRActivityClient.get().getActivityClientController();
            if (client != null) {
                java.lang.reflect.Method m = client.getClass().getMethod("getTaskForActivity", IBinder.class, boolean.class);
                Object res = m.invoke(client, token, false);
                if (res instanceof Integer) {
                    return (Integer) res;
                }
            }
        } catch (Throwable ignored) {
        }
        return 0;
    }

    private void checkActivityClient() {
        try {
            Object activityClientController =
                    BRActivityClient.get().getActivityClientController();
            if (!(activityClientController instanceof Proxy)) {
                IActivityClientProxy proxy = new IActivityClientProxy(activityClientController);
                proxy.onlyProxy(true);
                proxy.injectHook();
                Object instance = BRActivityClient.get().getInstance();
                Object singleton = BRActivityClient.get(instance).INTERFACE_SINGLETON();
                BRActivityClientActivityClientControllerSingleton.get(singleton)
                        ._set_mKnownInstance(proxy.getProxyInvocation());
            }
        } catch (Throwable error) {
            Slog.e(TAG, "Unable to refresh ActivityClient hook: " + error);
        }
    }
}

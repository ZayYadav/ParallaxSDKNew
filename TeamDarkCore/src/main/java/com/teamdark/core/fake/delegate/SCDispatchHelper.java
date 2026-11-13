package com.teamdark.core.fake.delegate;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.RemoteException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Reflective dispatcher for android.app.IServiceConnection.connected(...) across
 * all Android versions and OEM ROMs (2-arg, 3-arg, 4-arg IBinder, and 4-arg IBinderSession).
 * Never emits a direct invokeinterface against IServiceConnection.connected so it never
 * triggers NoSuchMethodError when framework AIDL signatures change.
 */
public final class SCDispatchHelper {
    private SCDispatchHelper() {
    }

    public static void dispatch(Object conn, ComponentName name, IBinder service) throws RemoteException {
        dispatch(conn, name, service, null, false);
    }

    public static void dispatch(Object conn, ComponentName name, IBinder service, boolean dead) throws RemoteException {
        dispatch(conn, name, service, null, dead);
    }

    public static void dispatch(Object conn, ComponentName name, IBinder service, Object session, boolean dead) throws RemoteException {
        if (conn == null) {
            return;
        }
        Method best4Session = null;
        Method best4Binder = null;
        Method best3 = null;
        Method best2 = null;

        Class<?> cls = conn.getClass();
        while (cls != null && cls != Object.class) {
            for (Method m : cls.getDeclaredMethods()) {
                if (!"connected".equals(m.getName())) {
                    continue;
                }
                Class<?>[] pt = m.getParameterTypes();
                if (pt.length == 4 && pt[0] == ComponentName.class && pt[1] == IBinder.class && pt[3] == boolean.class) {
                    if ("android.app.IBinderSession".equals(pt[2].getName())) {
                        if (best4Session == null) best4Session = m;
                    } else if (best4Binder == null) {
                        best4Binder = m;
                    }
                } else if (pt.length == 3 && pt[0] == ComponentName.class && pt[1] == IBinder.class && pt[2] == boolean.class) {
                    if (best3 == null) best3 = m;
                } else if (pt.length == 2 && pt[0] == ComponentName.class && pt[1] == IBinder.class) {
                    if (best2 == null) best2 = m;
                }
            }
            cls = cls.getSuperclass();
        }

        // Also scan public interface/class methods if not found on declared hierarchy
        if (best4Session == null && best4Binder == null && best3 == null && best2 == null) {
            for (Method m : conn.getClass().getMethods()) {
                if (!"connected".equals(m.getName())) {
                    continue;
                }
                Class<?>[] pt = m.getParameterTypes();
                if (pt.length == 4 && pt[0] == ComponentName.class && pt[1] == IBinder.class && pt[3] == boolean.class) {
                    if ("android.app.IBinderSession".equals(pt[2].getName())) {
                        if (best4Session == null) best4Session = m;
                    } else if (best4Binder == null) {
                        best4Binder = m;
                    }
                } else if (pt.length == 3 && pt[0] == ComponentName.class && pt[1] == IBinder.class && pt[2] == boolean.class) {
                    if (best3 == null) best3 = m;
                } else if (pt.length == 2 && pt[0] == ComponentName.class && pt[1] == IBinder.class) {
                    if (best2 == null) best2 = m;
                }
            }
        }

        Method[] candidates = new Method[]{best4Session, best4Binder, best3, best2};
        for (Method m : candidates) {
            if (m == null) {
                continue;
            }
            try {
                m.setAccessible(true);
                Class<?>[] pt = m.getParameterTypes();
                if (pt.length == 4) {
                    Object sessionArg = (session != null && pt[2].isInstance(session)) ? session : null;
                    m.invoke(conn, name, service, sessionArg, dead);
                } else if (pt.length == 3) {
                    m.invoke(conn, name, service, dead);
                } else if (pt.length == 2) {
                    m.invoke(conn, name, service);
                }
                return;
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getTargetException();
                if (cause instanceof RemoteException) {
                    throw (RemoteException) cause;
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                if (cause instanceof Error) {
                    throw (Error) cause;
                }
                return;
            } catch (Throwable ignored) {
            }
        }
    }
}

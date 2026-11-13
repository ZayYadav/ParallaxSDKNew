package com.teamdark.core.utils.compat;

import android.os.Build;

/**
 * Captures and preserves REAL physical device hardware attributes
 * before virtual engine (DeviceProfileCompat) applies gaming spoof presets (e.g. ROG Phone).
 * Used for accurate Crash Telemetry & Panel Hardware Logging.
 */
public final class RealDeviceHelper {

    public static final String REAL_MANUFACTURER;
    public static final String REAL_BRAND;
    public static final String REAL_MODEL;
    public static final String REAL_DEVICE;
    public static final String REAL_PRODUCT;
    public static final String REAL_BOARD;
    public static final String REAL_HARDWARE;
    public static final String REAL_ANDROID_VERSION;
    public static final int REAL_SDK_INT;

    static {
        String m = Build.MANUFACTURER;
        String b = Build.BRAND;
        String mo = Build.MODEL;
        String d = Build.DEVICE;
        String p = Build.PRODUCT;
        String bo = Build.BOARD;
        String h = Build.HARDWARE;
        String v = Build.VERSION.RELEASE;
        int sdk = Build.VERSION.SDK_INT;

        // Direct kernel / SystemProperties lookup to guarantee real host values
        try {
            Class<?> sp = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method get = sp.getMethod("get", String.class, String.class);
            String sysM = (String) get.invoke(null, "ro.product.manufacturer", "");
            if (sysM != null && !sysM.trim().isEmpty() && !"asus".equalsIgnoreCase(sysM)) {
                m = sysM.trim();
            }
            String sysBrand = (String) get.invoke(null, "ro.product.brand", "");
            if (sysBrand != null && !sysBrand.trim().isEmpty() && !"asus".equalsIgnoreCase(sysBrand)) {
                b = sysBrand.trim();
            }
            String sysModel = (String) get.invoke(null, "ro.product.model", "");
            if (sysModel != null && !sysModel.trim().isEmpty() && !"ASUS_AI2401_A".equalsIgnoreCase(sysModel)) {
                mo = sysModel.trim();
            }
            String sysDevice = (String) get.invoke(null, "ro.product.device", "");
            if (sysDevice != null && !sysDevice.trim().isEmpty() && !"AI2401".equalsIgnoreCase(sysDevice)) {
                d = sysDevice.trim();
            }
        } catch (Throwable ignored) {
        }

        REAL_MANUFACTURER = m;
        REAL_BRAND = b;
        REAL_MODEL = mo;
        REAL_DEVICE = d;
        REAL_PRODUCT = p;
        REAL_BOARD = bo;
        REAL_HARDWARE = h;
        REAL_ANDROID_VERSION = v;
        REAL_SDK_INT = sdk;
    }

    private RealDeviceHelper() {}
}

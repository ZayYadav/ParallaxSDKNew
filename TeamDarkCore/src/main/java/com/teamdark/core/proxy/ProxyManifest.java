package com.teamdark.core.proxy;

import java.util.Locale;
import com.teamdark.core.TeamDarkCoreEngine;

public class ProxyManifest {
    public static final int FREE_COUNT = 25;
    public static final String PROXY_PACKAGE_NAME = "com.teamdark.core";

    public static boolean isProxy(String msg) {
        return getBindProvider().equals(msg)
                || msg.contains("teamdark_proxy_provider_")
                || msg.contains("proxy_content_provider_");
    }

    public static String getBindProvider() {
        return TeamDarkCoreEngine.getHostPkg() + ".teamdark.SystemCallProvider";
    }

    public static String getProxyAuthorities(int index) {
        return String.format(Locale.CHINA, "%s.teamdark_proxy_provider_%d", TeamDarkCoreEngine.getHostPkg(), index);
    }

    public static String getProxyPendingActivity(int index) {
        return String.format(Locale.CHINA, PROXY_PACKAGE_NAME + ".proxy.ProxyPendingActivity$P%d", index);
    }

    public static String getProxyActivity(int index) {
        return String.format(Locale.CHINA, PROXY_PACKAGE_NAME + ".proxy.ProxyActivity$P%d", index);
    }

    public static String TransparentProxyActivity(int index) {
        return String.format(Locale.CHINA, PROXY_PACKAGE_NAME + ".proxy.TransparentProxyActivity$P%d", index);
    }

    public static String getProxyService(int index) {
        return String.format(Locale.CHINA, PROXY_PACKAGE_NAME + ".proxy.ProxyService$P%d", index);
    }

    public static String getProxyJobService(int index) {
        return String.format(Locale.CHINA, PROXY_PACKAGE_NAME + ".proxy.ProxyJobService$P%d", index);
    }

    public static String getProxyFileProvider() {
        return TeamDarkCoreEngine.getHostPkg() + ".teamdark.FileProvider";
    }

    public static String getProxyReceiver() {
        return TeamDarkCoreEngine.getHostPkg() + ".teamdark_stub_receiver";
    }

    public static String getProcessName(int bPid) {
        return TeamDarkCoreEngine.getHostPkg() + ":p" + bPid;
    }
}

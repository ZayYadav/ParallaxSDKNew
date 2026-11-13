package com.sbox.entity.pm;

import androidx.annotation.Keep;

@Keep
public class InstallResult extends com.featurestic.box.entity.pm.InstallResult {
    public InstallResult() {
        super();
    }
    public static InstallResult from(com.featurestic.box.entity.pm.InstallResult src) {
        InstallResult r = new InstallResult();
        if (src != null) {
            r.success = src.success;
            r.packageName = src.packageName;
            r.msg = src.msg;
        }
        return r;
    }
}

package com.featurestic.box.utils.auth;

import java.util.HashSet;
import com.featurestic.lsparanoid.Obfuscate;

@Obfuscate
public final class Auth {

    public static final HashSet<String> AUTH_PKG_SET = new HashSet<String>();

    static {
        AUTH_PKG_SET.add("com.twitter.android");
        AUTH_PKG_SET.add("com.x.android");
    }

}
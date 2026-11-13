package com.featurestic.black.android.app;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;

import com.featurestic.blackreflection.annotation.BClassName;
import com.featurestic.blackreflection.annotation.BField;
import com.featurestic.blackreflection.annotation.BMethod;
import com.featurestic.blackreflection.annotation.BParamClassName;
import com.featurestic.blackreflection.annotation.BStaticMethod;

@BClassName("android.app.ContextImpl")
public interface ContextImpl {
    @BField
    String mBasePackageName();

    @BField
    ContentResolver mContentResolver();

    @BField
    Object mPackageInfo();

    @BField
    PackageManager mPackageManager();

    @BStaticMethod
    Object createAppContext(
            @BParamClassName("android.app.ActivityThread") Object mainThread,
            @BParamClassName("android.app.LoadedApk") Object packageInfo);

    @BMethod
    Context getReceiverRestrictedContext();

    @BMethod
    void setOuterContext(Context Context0);

    @BMethod
    Object getAttributionSource();

    @BMethod
    PackageManager getPackageManager();
}

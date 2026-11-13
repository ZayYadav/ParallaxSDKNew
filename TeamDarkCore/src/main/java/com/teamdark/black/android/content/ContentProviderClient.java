package com.teamdark.black.android.content;

import android.os.IInterface;

import com.teamdark.blackreflection.annotation.BClassName;
import com.teamdark.blackreflection.annotation.BField;

@BClassName("android.content.ContentProviderClient")
public interface ContentProviderClient {
    @BField
    IInterface mContentProvider();
}

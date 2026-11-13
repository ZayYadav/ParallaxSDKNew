package com.featurestic.box;

import android.content.Context;
import androidx.annotation.Keep;
import com.featurestic.sdk.FeaturesticBox;

@Keep
public final class FeaturesticSdk {
    private FeaturesticSdk() {}

    public static void init(Context context) {
        FeaturesticBox.init(context);
    }

    public static boolean startGame(Context context, String packageName) {
        return FeaturesticBox.startGame(context, packageName, 0, null);
    }
}

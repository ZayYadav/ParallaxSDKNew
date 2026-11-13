package com.teamdark.core;

import android.content.Context;
import androidx.annotation.Keep;
import com.teamdark.sdk.TeamDarkCore;

@Keep
public final class TeamDarkSdk {
    private TeamDarkSdk() {}

    public static void init(Context context) {
        TeamDarkCore.init(context);
    }

    public static boolean startGame(Context context, String packageName) {
        return TeamDarkCore.startGame(context, packageName, 0, null);
    }
}

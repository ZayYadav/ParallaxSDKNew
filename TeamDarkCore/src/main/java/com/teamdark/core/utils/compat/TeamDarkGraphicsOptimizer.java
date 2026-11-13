package com.teamdark.core.utils.compat;

import android.app.Activity;
import androidx.annotation.Keep;

/**
 * Clean Native In-Game Graphics Unlocker.
 *
 * Does NOT overwrite or tamper with EnjoyCJZC.ini / UserCustom.ini (which triggers
 * libanogs.so config integrity/offset crashes in BGMI 3.x/4.x). Instead, 90/120 FPS
 * and Ultra HD / Extreme+ options are unlocked cleanly through DeviceProfileCompat
 * and IDisplayManagerProxy so the user can freely select their preferred FPS and
 * graphics settings inside the game!
 */
@Keep
public final class TeamDarkGraphicsOptimizer {
    public static final int PRESET_DEFAULT = 0;
    public static final int PRESET_ESPORTS_NO_SHADOW = 1;
    public static final int PRESET_ULTRA_SHARP_HDR = 2;

    private TeamDarkGraphicsOptimizer() {
    }

    public static int getCurrentPreset() {
        return PRESET_DEFAULT;
    }

    public static void setPreset(int preset, Activity activeActivity) {
    }

    public static int cycleNextPreset(Activity activeActivity) {
        return PRESET_DEFAULT;
    }

    public static String getPresetLabel(int preset) {
        return "Auto 120Hz Native Unlock";
    }

    public static void applyGraphicsPresetAsync(
            final String packageName, final int userId, final int preset) {
        // Intentionally no-op on EnjoyCJZC.ini so libanogs.so config checks pass 100% cleanly!
    }
}

package com.teamdark.core.fake;

import com.teamdark.core.jnihook.ReflectCore;

/**
 * Created by @TeamDarkCore on 3/7/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class FakeCore {
    public static void init() {
        ReflectCore.set(android.app.ActivityThread.class);
    }
}

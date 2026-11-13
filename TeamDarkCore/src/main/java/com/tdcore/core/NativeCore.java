package com.tdcore.core;

import androidx.annotation.Keep;

/**
 * TDCore entry point for existing native operations.
 *
 * The bundled precompiled native library still declares the legacy JNI
 * binding on com.fbox.core.NativeCore. Inheritance keeps those native method
 * declarations on their original JVM class without altering their behavior.
 * Rebuilding the native binary is required to remove that compatibility ABI.
 */
@Keep
public final class NativeCore extends com.fbox.core.NativeCore {
    private NativeCore() {
        // Static native methods are inherited from the legacy JNI bridge.
    }
}

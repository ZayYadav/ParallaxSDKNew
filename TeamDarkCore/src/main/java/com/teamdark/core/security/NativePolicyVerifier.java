package com.teamdark.core.security;

import androidx.annotation.Keep;

/** Verifies the server's ECDSA P-256 authorization in the SDK's native library. */
@Keep
public final class NativePolicyVerifier {
    private static final boolean AVAILABLE;
    static {
        boolean ok = false;
        try {
            System.loadLibrary("TeamDarkCore");
            ok = true;
        } catch (Throwable ignored) {
            // Missing native library means authorization fails closed.
        }
        AVAILABLE = ok;
    }

    @Keep
    private static native int getNativeFeatures(long currentEpochSeconds);

    /** Signed feature bits expire with the verified server lease. */
    public static int authorizedFeatures() {
        if (!AVAILABLE) return 0;
        try { return getNativeFeatures(System.currentTimeMillis() / 1000L); }
        catch (Throwable ignored) { return 0; }
    }

    private NativePolicyVerifier() {}

    @Keep
    private static native boolean verifySignedPolicy(String payloadBase64,
            String signatureBase64, String x509EcPublicKeyBase64, String deviceHash,
            String packageName, String signingSha256, String nonce, long clientEpochSeconds);

    public static boolean verify(String policy, String sig, String publicKey, String device,
            String packageName, String cert, String nonce, long now) {
        if (!AVAILABLE || policy == null || sig == null || publicKey == null ||
                publicKey.isEmpty() || device == null || packageName == null ||
                cert == null || nonce == null) return false;
        try {
            return verifySignedPolicy(policy, sig, publicKey, device, packageName, cert, nonce, now);
        } catch (Throwable ignored) {
            return false;
        }
    }
}

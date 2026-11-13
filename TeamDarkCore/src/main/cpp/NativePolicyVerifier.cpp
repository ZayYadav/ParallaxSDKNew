#include <jni.h>
#include <atomic>
#include <algorithm>
#include <cstdint>
#include <cstdlib>
#include <limits>
#include <string>
#include <vector>

/*
 * TDCore server-authenticated access policy. Verification happens in native
 * code; Android's vetted java.security Signature provider performs P-256
 * ECDSA operations through JNI. No private signing material is shipped to SDK.
 */
namespace {
std::atomic<long long> gLeaseUntil{0};
std::atomic<int> gFeatureMask{0};

bool ok(JNIEnv* env) {
    if (env == nullptr || env->ExceptionCheck()) {
        if (env != nullptr && env->ExceptionCheck()) env->ExceptionClear();
        return false;
    }
    return true;
}
std::string str(JNIEnv* env, jstring value) {
    if (value == nullptr) return {};
    const char* chars = env->GetStringUTFChars(value, nullptr);
    if (!chars || !ok(env)) return {};
    std::string result(chars);
    env->ReleaseStringUTFChars(value, chars);
    return result;
}
jbyteArray decodeBase64(JNIEnv* env, jstring value) {
    jclass cls = env->FindClass("android/util/Base64");
    if (!cls || !ok(env)) return nullptr;
    jmethodID method = env->GetStaticMethodID(
        cls, "decode", "(Ljava/lang/String;I)[B");
    if (!method || !ok(env)) return nullptr;
    auto out = static_cast<jbyteArray>(
        env->CallStaticObjectMethod(cls, method, value, static_cast<jint>(0)));
    if (!ok(env) || !out) return nullptr;
    if (env->GetArrayLength(out) > 4096) return nullptr;
    return out;
}
bool number(const std::string& s, long long& v) {
    if (s.empty() || s.size() > 18) return false;
    for (char c : s) if (c < '0' || c > '9') return false;
    char* end = nullptr;
    v = std::strtoll(s.c_str(), &end, 10);
    return end != nullptr && *end == '\0' && v > 0;
}
std::vector<std::string> lines(const std::string& s) {
    std::vector<std::string> out;
    size_t pos = 0;
    while (pos <= s.size()) {
        auto end = s.find('\n', pos);
        if (end == std::string::npos) {
            out.emplace_back(s.substr(pos));
            break;
        }
        out.emplace_back(s.substr(pos, end - pos));
        pos = end + 1;
    }
    return out;
}
bool verifyEcdsa(JNIEnv* env, jbyteArray message, jbyteArray signature,
                 jbyteArray publicKeyDer) {
    jclass specClass = env->FindClass("java/security/spec/X509EncodedKeySpec");
    if (!specClass || !ok(env)) return false;
    jmethodID specCtor = env->GetMethodID(specClass, "<init>", "([B)V");
    if (!specCtor || !ok(env)) return false;
    jobject spec = env->NewObject(specClass, specCtor, publicKeyDer);
    if (!spec || !ok(env)) return false;

    jclass factoryClass = env->FindClass("java/security/KeyFactory");
    if (!factoryClass || !ok(env)) return false;
    jmethodID factoryGet = env->GetStaticMethodID(factoryClass, "getInstance",
        "(Ljava/lang/String;)Ljava/security/KeyFactory;");
    if (!factoryGet || !ok(env)) return false;
    jstring ec = env->NewStringUTF("EC");
    jobject factory = env->CallStaticObjectMethod(factoryClass, factoryGet, ec);
    if (!factory || !ok(env)) return false;
    jmethodID generate = env->GetMethodID(factoryClass, "generatePublic",
        "(Ljava/security/spec/KeySpec;)Ljava/security/PublicKey;");
    if (!generate || !ok(env)) return false;
    jobject key = env->CallObjectMethod(factory, generate, spec);
    if (!key || !ok(env)) return false;

    jclass signClass = env->FindClass("java/security/Signature");
    if (!signClass || !ok(env)) return false;
    jmethodID getSign = env->GetStaticMethodID(signClass, "getInstance",
        "(Ljava/lang/String;)Ljava/security/Signature;");
    if (!getSign || !ok(env)) return false;
    jstring algorithm = env->NewStringUTF("SHA256withECDSA");
    jobject signer = env->CallStaticObjectMethod(signClass, getSign, algorithm);
    if (!signer || !ok(env)) return false;
    jmethodID init = env->GetMethodID(signClass, "initVerify",
        "(Ljava/security/PublicKey;)V");
    jmethodID update = env->GetMethodID(signClass, "update", "([B)V");
    jmethodID verify = env->GetMethodID(signClass, "verify", "([B)Z");
    if (!init || !update || !verify || !ok(env)) return false;
    env->CallVoidMethod(signer, init, key);
    if (!ok(env)) return false;
    env->CallVoidMethod(signer, update, message);
    if (!ok(env)) return false;
    jboolean valid = env->CallBooleanMethod(signer, verify, signature);
    return ok(env) && valid == JNI_TRUE;
}
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_teamdark_core_security_NativePolicyVerifier_verifySignedPolicy(
    JNIEnv* env, jclass, jstring payloadB64, jstring signatureB64,
    jstring publicKeyB64, jstring deviceValue, jstring packageValue,
    jstring certificateValue, jstring nonceValue, jlong now) {

    gLeaseUntil.store(0);
    gFeatureMask.store(0);
    if (!env || now <= 0 || !payloadB64 || !signatureB64 ||
        !publicKeyB64 || !deviceValue || !packageValue ||
        !certificateValue || !nonceValue) return JNI_FALSE;

    jbyteArray bytes = decodeBase64(env, payloadB64);
    jbyteArray sig = decodeBase64(env, signatureB64);
    jbyteArray pub = decodeBase64(env, publicKeyB64);
    if (!bytes || !sig || !pub) return JNI_FALSE;
    jint n = env->GetArrayLength(bytes);
    if (n < 32 || n > 2048 || env->GetArrayLength(sig) < 64 ||
        env->GetArrayLength(pub) < 64) return JNI_FALSE;

    std::string raw(static_cast<size_t>(n), '\0');
    env->GetByteArrayRegion(bytes, 0, n, reinterpret_cast<jbyte*>(&raw[0]));
    if (!ok(env)) return JNI_FALSE;
    auto f = lines(raw);
    if (f.size() != 10 || f[0] != "tdcore-policy-v1" ||
        f[2] != str(env, deviceValue) ||
        f[3] != str(env, packageValue) ||
        f[4] != str(env, certificateValue) ||
        f[5] != str(env, nonceValue)) return JNI_FALSE;

    long long issued = 0, expires = 0, license = 0, features = 0, version = 0;
    if (!number(f[1], license) || !number(f[6], issued) ||
        !number(f[7], expires) || !number(f[9], version)) return JNI_FALSE;
    if (f[8] == "0") features = 0;
    else if (!number(f[8], features)) return JNI_FALSE;
    if (features < 0 || features > 2147483647LL ||
        issued > static_cast<long long>(now) + 300LL ||
        issued < static_cast<long long>(now) - 300LL ||
        expires <= static_cast<long long>(now) ||
        expires > issued + 60LL) return JNI_FALSE;

    if (!verifyEcdsa(env, bytes, sig, pub)) return JNI_FALSE;
    gFeatureMask.store(static_cast<int>(features));
    gLeaseUntil.store(expires);
    return JNI_TRUE;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_teamdark_core_security_NativePolicyVerifier_getNativeFeatures(
    JNIEnv*, jclass, jlong now) {
    if (now <= 0 || gLeaseUntil.load() <= static_cast<long long>(now)) return 0;
    return static_cast<jint>(gFeatureMask.load());
}

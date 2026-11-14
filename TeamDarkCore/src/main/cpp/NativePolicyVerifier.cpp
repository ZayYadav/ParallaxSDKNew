#include <jni.h>
#include <atomic>
#include <algorithm>
#include <cstdint>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <fcntl.h>
#include <limits>
#include <string>
#include <sys/stat.h>
#include <unistd.h>
#include <vector>

#include "TDCoreGuard.h"

/*
 * TDCore server-authenticated access policy. Verification happens in native
 * code; Android's vetted java.security Signature provider performs P-256
 * ECDSA operations through JNI. No private signing material is shipped to SDK.
 *
 * Every policy operation is gated by the native integrity guard. A debugger,
 * instrumentation agent or inline hook poisons the process permanently: the
 * lease and feature bits are zeroed and all later checks fail closed. There is
 * no offline path that can restore access.
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
}  // namespace

/* ==================== Native APK identity enforcement ====================
 * The signed policy carries the package name and signing-certificate SHA-256
 * that the panel authorized. Java-supplied copies of those values can be
 * forged by hooks, so they are re-measured here with no Java input at all:
 *   1. process identity  -> /proc/self/cmdline (main process = package)
 *   2. running APK path  -> /proc/self/maps (the mmap'd base/split APK)
 *   3. APK Signing Block (v2/v3) parsed from the APK bytes on disk
 *   4. SHA-256 of the single signer certificate, computed in pure C++
 * Any mismatch (or an APK with no v2/v3 signing block) fails closed. A
 * definitive identity mismatch permanently poisons the integrity guard, so a
 * repackaged or re-signed APK can never authorize, not even after a restart
 * of the license check.
 */
namespace tdcore { namespace apkid {

constexpr uint32_t K256[64] = {
    0x428a2f98,0x71374491,0xb5c0fbcf,0xe9b5dba5,0x3956c25b,0x59f111f1,0x923f82a4,0xab1c5ed5,
    0xd807aa98,0x12835b01,0x243185be,0x550c7dc3,0x72be5d74,0x80deb1fe,0x9bdc06a7,0xc19bf174,
    0xe49b69c1,0xefbe4786,0x0fc19dc6,0x240ca1cc,0x2de92c6f,0x4a7484aa,0x5cb0a9dc,0x76f988da,
    0x983e5152,0xa831c66d,0xb00327c8,0xbf597fc7,0xc6e00bf3,0xd5a79147,0x06ca6351,0x14292967,
    0x27b70a85,0x2e1b2138,0x4d2c6dfc,0x53380d13,0x650a7354,0x766a0abb,0x81c2c92e,0x92722c85,
    0xa2bfe8a1,0xa81a664b,0xc24b8b70,0xc76c51a3,0xd192e819,0xd6990624,0xf40e3585,0x106aa070,
    0x19a4c116,0x1e376c08,0x2748774c,0x34b0bcb5,0x391c0cb3,0x4ed8aa4a,0x5b9cca4f,0x682e6ff3,
    0x748f82ee,0x78a5636f,0x84c87814,0x8cc70208,0x90befffa,0xa4506ceb,0xbef9a3f7,0xc67178f2};

inline uint32_t rotr(uint32_t x, int n) { return (x >> n) | (x << (32 - n)); }

struct Sha256 {
    uint32_t h[8];
    uint64_t bits;
    uint8_t buf[64];
    size_t used;
    void init() {
        h[0]=0x6a09e667; h[1]=0xbb67ae85; h[2]=0x3c6ef372; h[3]=0xa54ff53a;
        h[4]=0x510e527f; h[5]=0x9b05688c; h[6]=0x1f83d9ab; h[7]=0x5be0cd19;
        bits=0; used=0;
    }
    void block(const uint8_t* p) {
        uint32_t w[64];
        for (int i=0;i<16;++i)
            w[i]=((uint32_t)p[i*4]<<24)|((uint32_t)p[i*4+1]<<16)|((uint32_t)p[i*4+2]<<8)|p[i*4+3];
        for (int i=16;i<64;++i) {
            uint32_t s0=rotr(w[i-15],7)^rotr(w[i-15],18)^(w[i-15]>>3);
            uint32_t s1=rotr(w[i-2],17)^rotr(w[i-2],19)^(w[i-2]>>10);
            w[i]=w[i-16]+s0+w[i-7]+s1;
        }
        uint32_t a=h[0],b=h[1],c=h[2],d=h[3],e=h[4],f=h[5],g=h[6],hh=h[7];
        for (int i=0;i<64;++i) {
            uint32_t S1=rotr(e,6)^rotr(e,11)^rotr(e,25);
            uint32_t ch=(e&f)^((~e)&g);
            uint32_t t1=hh+S1+ch+K256[i]+w[i];
            uint32_t S0=rotr(a,2)^rotr(a,13)^rotr(a,22);
            uint32_t maj=(a&b)^(a&c)^(b&c);
            uint32_t t2=S0+maj;
            hh=g; g=f; f=e; e=d+t1; d=c; c=b; b=a; a=t1+t2;
        }
        h[0]+=a; h[1]+=b; h[2]+=c; h[3]+=d; h[4]+=e; h[5]+=f; h[6]+=g; h[7]+=hh;
    }
    void update(const uint8_t* data, size_t len) {
        bits += (uint64_t)len * 8ULL;
        while (len > 0) {
            size_t take = 64 - used;
            if (take > len) take = len;
            memcpy(buf + used, data, take);
            used += take; data += take; len -= take;
            if (used == 64) { block(buf); used = 0; }
        }
    }
    void final(uint8_t out[32]) {
        uint64_t total = bits;
        uint8_t pad = 0x80;
        update(&pad, 1);
        uint8_t zero = 0;
        while (used != 56) update(&zero, 1);
        uint8_t lenb[8];
        for (int i=0;i<8;++i) lenb[i]=(uint8_t)(total >> (56 - i*8));
        update(lenb, 8);
        for (int i=0;i<8;++i) {
            out[i*4]  =(uint8_t)(h[i]>>24); out[i*4+1]=(uint8_t)(h[i]>>16);
            out[i*4+2]=(uint8_t)(h[i]>>8);  out[i*4+3]=(uint8_t)h[i];
        }
    }
};

inline uint32_t rd32(const uint8_t* p) {
    return (uint32_t)p[0] | ((uint32_t)p[1]<<8) | ((uint32_t)p[2]<<16) | ((uint32_t)p[3]<<24);
}
inline uint64_t rd64(const uint8_t* p) {
    uint64_t v=0;
    for (int i=7;i>=0;--i) v=(v<<8)|p[i];
    return v;
}

bool readTail(const std::string& path, std::vector<uint8_t>& out, size_t maxBytes,
              off_t* fileSize) {
    int fd = ::open(path.c_str(), O_RDONLY);
    if (fd < 0) return false;
    struct stat st {};
    if (fstat(fd, &st) != 0 || st.st_size <= 0) { ::close(fd); return false; }
    off_t size = st.st_size;
    size_t want = size < (off_t)maxBytes ? (size_t)size : maxBytes;
    off_t from = size - (off_t)want;
    out.assign(want, 0);
    size_t got = 0;
    while (got < want) {
        ssize_t r = ::pread(fd, out.data() + got, want - got, from + (off_t)got);
        if (r <= 0) { ::close(fd); return false; }
        got += (size_t)r;
    }
    ::close(fd);
    if (fileSize) *fileSize = size;
    return true;
}

/* procfs files report st_size == 0, so read() until EOF into a capped buffer. */
bool readFileCap(const std::string& path, std::vector<uint8_t>& out, size_t cap) {
    int fd = ::open(path.c_str(), O_RDONLY);
    if (fd < 0) return false;
    out.assign(cap, 0);
    size_t total = 0;
    while (total < cap) {
        ssize_t r = ::read(fd, out.data() + total, cap - total);
        if (r < 0) { ::close(fd); return false; }
        if (r == 0) break;
        total += (size_t)r;
    }
    ::close(fd);
    out.resize(total);
    return total > 0;
}

std::string processName() {
    std::vector<uint8_t> raw;
    if (!readFileCap("/proc/self/cmdline", raw, 512)) return {};
    size_t z = 0;
    while (z < raw.size() && raw[z] != 0) ++z;
    std::string name(reinterpret_cast<const char*>(raw.data()), z);
    size_t colon = name.find(':');          /* com.app:service -> com.app */
    if (colon != std::string::npos) name.resize(colon);
    while (!name.empty() && (name.back()=='\n' || name.back()=='\r')) name.pop_back();
    return name;
}

void apkCandidatesFromMaps(std::vector<std::string>* out) {
    std::vector<uint8_t> raw;
    if (!readFileCap("/proc/self/maps", raw, 4u<<20)) return;
    const char* p = reinterpret_cast<const char*>(raw.data());
    size_t n = raw.size(), pos = 0;
    while (pos < n && out->size() < 4) {
        size_t eol = pos;
        while (eol < n && p[eol] != '\n') ++eol;
        std::string line(p + pos, eol - pos);
        pos = eol + 1;
        size_t apk = line.find(".apk");
        if (apk == std::string::npos) continue;
        size_t pathStart = line.find_last_of(' ');
        if (pathStart == std::string::npos || pathStart + 1 > apk) continue;
        std::string path = line.substr(pathStart + 1);
        if (path.find("(deleted)") != std::string::npos) continue;  /* replaced on disk */
        if (std::find(out->begin(), out->end(), path) == out->end()) out->push_back(path);
    }
}

/* Extract the single signer's single certificate from an APK Signing Block
 * v2 (0x7109871a) or v3 (0xf05368c0) ID-value pair. */
bool extractCertificate(const uint8_t* v, size_t n, std::vector<uint8_t>* cert) {
    size_t p = 0;
    auto u32 = [&](size_t off, uint32_t* out) {
        if (off + 4 > n) return false;
        *out = rd32(v + off);
        return true;
    };
    uint32_t seqLen = 0, signerLen = 0;
    if (!u32(p, &seqLen)) return false; p += 4;
    if (!u32(p, &signerLen)) return false;
    if (signerLen + 4 != seqLen) return false;              /* exactly one signer */
    size_t signerEnd = p + 4 + signerLen;
    if (signerEnd > n) return false;
    p += 4;
    uint32_t sdLen = 0;
    if (!u32(p, &sdLen)) return false;
    size_t sdStart = p + 4, sdEnd = sdStart + sdLen;
    if (sdEnd > signerEnd) return false;
    /* signed data: digests, certificates, additional attributes */
    size_t q = sdStart;
    uint32_t digestsLen = 0, certsLen = 0;
    if (!u32(q, &digestsLen)) return false;
    q += 4 + digestsLen;
    if (q + 4 > sdEnd) return false;
    if (!u32(q, &certsLen)) return false;
    size_t certsStart = q + 4, certsEnd = certsStart + certsLen;
    if (certsEnd > sdEnd) return false;
    uint32_t certLen = 0;
    if (!u32(certsStart, &certLen)) return false;
    if (certLen + 4 != certsLen) return false;              /* exactly one certificate */
    size_t certStart = certsStart + 4;
    if (certLen == 0 || certStart + certLen > certsEnd) return false;
    cert->assign(v + certStart, v + certStart + certLen);
    return true;
}

/* Locate the APK Signing Block immediately before the central directory and
 * hand the first v2/v3 pair value to extractCertificate(). */
bool certificateOf(const std::string& path, std::vector<uint8_t>* cert) {
    std::vector<uint8_t> tail;
    off_t fileSize = 0;
    if (!readTail(path, tail, 1u<<20, &fileSize)) return false;
    const uint8_t* d = tail.data();
    const size_t n = tail.size();
    const off_t bufStart = (off_t)fileSize - (off_t)n;   /* absolute offset of d[0] */
    /* End of Central Directory record */
    size_t eocd = std::string::npos;
    for (size_t i = n; i-- >= 22; ) {
        if (d[i]==0x50 && d[i+1]==0x4b && d[i+2]==0x05 && d[i+3]==0x06) { eocd = i; break; }
    }
    if (eocd == std::string::npos || eocd + 22 > n) return false;
    uint64_t cdOffset = rd32(d + eocd + 16);
    uint64_t cdSize = rd32(d + eocd + 12);
    if (cdOffset == 0xFFFFFFFFULL || cdOffset == 0 || cdSize == 0) return false;
    /* CD is followed by the 22-byte EOCD record (+ optional comment). */
    if (cdOffset + cdSize + 22 > (uint64_t)fileSize) return false;
    if ((off_t)cdOffset < bufStart || (off_t)cdOffset >= (off_t)fileSize) return false;
    /* Signing block: [size][pairs][size][magic "APK Sig Block 42"] then CD. */
    const size_t cdPos = (size_t)((off_t)cdOffset - bufStart);   /* index of CD in tail */
    if (cdPos < 40) return false;
    static const char MAGIC[] = "APK Sig Block 42";
    if (memcmp(d + cdPos - 16, MAGIC, 16) != 0) return false;
    uint64_t blockSize = rd64(d + cdPos - 24);
    if (blockSize < 24 || blockSize > (uint64_t)cdPos - 8) return false;
    const size_t blockStart = (size_t)cdPos - 8 - (size_t)blockSize;
    if (blockStart + 8 > cdPos - 24) return false;
    if (rd64(d + blockStart) != blockSize) return false;
    /* Iterate ID-value pairs. */
    const size_t pairsEnd = cdPos - 24;
    size_t p = blockStart + 8;
    while (p + 12 <= pairsEnd) {
        uint64_t pairLen = rd64(d + p);
        p += 8;
        if (pairLen < 4 || p + pairLen > pairsEnd) return false;
        uint32_t id = rd32(d + p);
        if (id == 0x7109871a /* v2 */ || id == 0xf05368c0 /* v3 */) {
            return extractCertificate(d + p + 4, (size_t)pairLen - 4, cert);
        }
        p += (size_t)pairLen;
    }
    return false;
}

/* Full native identity measurement: package from the process, signer from the
 * mapped APK. *tampered is set when a definitive mismatch was measured (as
 * opposed to a transient read failure), so the caller can poison the guard. */
bool installedApkMatches(const std::string& pkg, const std::string& certHex,
                         bool* tampered) {
    if (tampered) *tampered = false;
    if (pkg.empty() || certHex.size() != 64) return false;
    if (processName() != pkg) {
        if (tampered) *tampered = true;
        return false;
    }
    std::vector<std::string> paths;
    apkCandidatesFromMaps(&paths);
    for (const std::string& path : paths) {
        std::vector<uint8_t> cert;
        if (!certificateOf(path, &cert)) continue;   /* split APK or unreadable: try next */
        Sha256 hash;
        hash.init();
        hash.update(cert.data(), cert.size());
        uint8_t digest[32];
        hash.final(digest);
        static const char HEX[] = "0123456789ABCDEF";
        std::string hex(64, '0');
        for (size_t i = 0; i < 32; ++i) {
            hex[i*2] = HEX[(digest[i] >> 4) & 0xF];
            hex[i*2+1] = HEX[digest[i] & 0xF];
        }
        if (hex != certHex) {
            if (tampered) *tampered = true;          /* re-signed APK on disk */
            return false;
        }
        return true;                                 /* one verified APK is enough */
    }
    return false;                                    /* no APK signing block found */
}

}}  // namespace tdcore::apkid

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
    if (!tdcore::guard::selfCheck()) return JNI_FALSE;

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

    /* Native identity enforcement: the package and signer inside the signed
     * policy must describe the APK that is actually running. Java only
     * supplies strings that the server signed; the ground truth below is
     * measured from the process and the APK file with no Java involvement. */
    bool apkTampered = false;
    if (!tdcore::apkid::installedApkMatches(f[3], f[4], &apkTampered)) {
        if (apkTampered) tdcore::guard::wipe();   /* re-signed/repackaged APK */
        return JNI_FALSE;
    }

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
    if (!tdcore::guard::selfCheck()) {
        gLeaseUntil.store(0);
        gFeatureMask.store(0);
        return 0;
    }
    if (now <= 0 || gLeaseUntil.load() <= static_cast<long long>(now)) return 0;
    return static_cast<jint>(gFeatureMask.load());
}

/* Native integrity probe. False means tampering was detected and the guard is
 * already poisoned; Java must wipe its local session and deny every feature. */
extern "C" JNIEXPORT jboolean JNICALL
Java_com_teamdark_core_security_NativePolicyVerifier_nativeGuardHealthy(
    JNIEnv*, jclass) {
    return tdcore::guard::selfCheck() ? JNI_TRUE : JNI_FALSE;
}

/* Permanent local wipe used when Java-side checks detect tampering. */
extern "C" JNIEXPORT void JNICALL
Java_com_teamdark_core_security_NativePolicyVerifier_nativeGuardWipe(
    JNIEnv*, jclass) {
    tdcore::guard::wipe();
    gLeaseUntil.store(0);
    gFeatureMask.store(0);
}

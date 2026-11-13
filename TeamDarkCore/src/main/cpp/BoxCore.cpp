#include "BoxCore.h"
#include "AnogsCompat.h"
#include "IO.h"
#include "Log.h"

#include <cstring>

struct {
    JavaVM *vm;
    jclass NativeCoreClass;
    jmethodID getCallingUidId;
    jmethodID redirectPathString;
    jmethodID redirectPathFile;
    int api_level;
    bool initialized;
} VMEnv = {nullptr, nullptr, nullptr, nullptr, nullptr, 0, false};

static JNIEnv *getEnv() {
    if (VMEnv.vm == nullptr) return nullptr;
    JNIEnv *env = nullptr;
    if (VMEnv.vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return nullptr;
    }
    return env;
}

static JNIEnv *ensureEnvCreated() {
    JNIEnv *env = getEnv();
    if (env == nullptr && VMEnv.vm != nullptr) {
        if (VMEnv.vm->AttachCurrentThread(&env, nullptr) != JNI_OK) {
            return nullptr;
        }
    }
    return env;
}

int BoxCore::getCallingUid(JNIEnv * /*env*/, int orig) {
    if (!VMEnv.initialized) return orig;
    JNIEnv *e = ensureEnvCreated();
    if (e == nullptr || VMEnv.NativeCoreClass == nullptr || VMEnv.getCallingUidId == nullptr) {
        return orig;
    }
    return e->CallStaticIntMethod(VMEnv.NativeCoreClass, VMEnv.getCallingUidId, orig);
}

jstring BoxCore::redirectPathString(JNIEnv *env, jstring path) {
    if (!VMEnv.initialized || path == nullptr || env == nullptr) return path;
    if (VMEnv.NativeCoreClass == nullptr || VMEnv.redirectPathString == nullptr) return path;
    jstring result = static_cast<jstring>(env->CallStaticObjectMethod(
            VMEnv.NativeCoreClass, VMEnv.redirectPathString, path));
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        return path;
    }
    return result ? result : path;
}

jobject BoxCore::redirectPathFile(JNIEnv *env, jobject path) {
    if (!VMEnv.initialized || path == nullptr || env == nullptr) return path;
    if (VMEnv.NativeCoreClass == nullptr || VMEnv.redirectPathFile == nullptr) return path;
    jobject result = env->CallStaticObjectMethod(
            VMEnv.NativeCoreClass, VMEnv.redirectPathFile, path);
    if (env->ExceptionCheck()) {
        env->ExceptionClear();
        return path;
    }
    return result ? result : path;
}

int BoxCore::getApiLevel() {
    return VMEnv.api_level;
}

JavaVM *BoxCore::getJavaVM() {
    return VMEnv.vm;
}

jclass BoxCore::getNativeCoreClass() {
    return VMEnv.NativeCoreClass;
}

jlongArray BoxCore::loadEmptyDex(JNIEnv *env) {
    return env ? env->NewLongArray(0) : nullptr;
}

static void nativeInitAnogsRuntime(JNIEnv *env, jclass clazz, jint apiLevel) {
    if (env == nullptr) return;
    VMEnv.api_level = apiLevel;

    if (VMEnv.NativeCoreClass == nullptr && clazz != nullptr) {
        VMEnv.NativeCoreClass = static_cast<jclass>(env->NewGlobalRef(clazz));
    }
    if (VMEnv.NativeCoreClass != nullptr) {
        VMEnv.getCallingUidId = env->GetStaticMethodID(
                VMEnv.NativeCoreClass, "getCallingUid", "(I)I");
        if (env->ExceptionCheck()) env->ExceptionClear();
        VMEnv.redirectPathString = env->GetStaticMethodID(
                VMEnv.NativeCoreClass, "redirectPath", "(Ljava/lang/String;)Ljava/lang/String;");
        if (env->ExceptionCheck()) env->ExceptionClear();
        VMEnv.redirectPathFile = env->GetStaticMethodID(
                VMEnv.NativeCoreClass, "redirectPath", "(Ljava/io/File;)Ljava/io/File;");
        if (env->ExceptionCheck()) env->ExceptionClear();
    }
    VMEnv.initialized = true;
    AnogsCompat::initRuntime(env, apiLevel);
}

static void nativeSyncIORule(JNIEnv *env, jclass /*clazz*/, jstring targetPath, jstring relocatePath) {
    if (env == nullptr || targetPath == nullptr || relocatePath == nullptr) return;
    const char *target = env->GetStringUTFChars(targetPath, nullptr);
    const char *relocate = env->GetStringUTFChars(relocatePath, nullptr);
    if (target != nullptr && relocate != nullptr) {
        IO::addRule(target, relocate);
    }
    if (target != nullptr) env->ReleaseStringUTFChars(targetPath, target);
    if (relocate != nullptr) env->ReleaseStringUTFChars(relocatePath, relocate);
}

static void nativeEnableNativeIO(JNIEnv *env, jclass /*clazz*/) {
    IO::init(env);
    AnogsCompat::enableNativeIO();
}

static void nativePrepareLibrarySegments(JNIEnv *env, jclass /*clazz*/, jstring libPathOrName) {
    if (env == nullptr || libPathOrName == nullptr) {
        AnogsCompat::prepareLibrarySegments(nullptr);
        return;
    }
    const char *libC = env->GetStringUTFChars(libPathOrName, nullptr);
    AnogsCompat::prepareLibrarySegments(libC);
    if (libC != nullptr) env->ReleaseStringUTFChars(libPathOrName, libC);
}

static jboolean nativeCanResolveArtField(JNIEnv *env, jclass /*clazz*/) {
    if (env == nullptr) return JNI_FALSE;
    jclass fieldClass = env->FindClass("java/lang/reflect/Field");
    if (fieldClass == nullptr) {
        if (env->ExceptionCheck()) env->ExceptionClear();
        return JNI_FALSE;
    }
    jmethodID getArtField = env->GetMethodID(fieldClass, "getArtField", "()J");
    if (env->ExceptionCheck()) env->ExceptionClear();
    env->DeleteLocalRef(fieldClass);
    if (getArtField == nullptr) {
        return JNI_FALSE;
    }

    jclass execClass = env->FindClass("java/lang/reflect/Executable");
    if (execClass == nullptr) {
        if (env->ExceptionCheck()) env->ExceptionClear();
        return JNI_FALSE;
    }
    jfieldID artMethod = env->GetFieldID(execClass, "artMethod", "J");
    if (env->ExceptionCheck()) env->ExceptionClear();
    env->DeleteLocalRef(execClass);
    return (artMethod != nullptr) ? JNI_TRUE : JNI_FALSE;
}

static JNINativeMethod gMethods[] = {
        {"nativeInitAnogsRuntime", "(I)V", reinterpret_cast<void *>(nativeInitAnogsRuntime)},
        {"nativeSyncIORule", "(Ljava/lang/String;Ljava/lang/String;)V", reinterpret_cast<void *>(nativeSyncIORule)},
        {"nativeEnableNativeIO", "()V", reinterpret_cast<void *>(nativeEnableNativeIO)},
        {"nativePrepareLibrarySegments", "(Ljava/lang/String;)V", reinterpret_cast<void *>(nativePrepareLibrarySegments)},
        {"nativeCanResolveArtField", "()Z", reinterpret_cast<void *>(nativeCanResolveArtField)},
};

static int registerNativeMethods(JNIEnv *env, const char *className,
                                 JNINativeMethod *methods, int numMethods) {
    if (env == nullptr || className == nullptr || methods == nullptr) return JNI_FALSE;
    jclass clazz = env->FindClass(className);
    if (clazz == nullptr) {
        if (env->ExceptionCheck()) env->ExceptionClear();
        ALOGE("registerNativeMethods: class %s not found", className);
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        if (env->ExceptionCheck()) env->ExceptionClear();
        ALOGE("registerNativeMethods: RegisterNatives failed for %s", className);
        env->DeleteLocalRef(clazz);
        return JNI_FALSE;
    }
    env->DeleteLocalRef(clazz);
    return JNI_TRUE;
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void * /*reserved*/) {
    JNIEnv *env = nullptr;
    VMEnv.vm = vm;
    if (vm == nullptr || vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_EVERSION;
    }
    registerNativeMethods(env, VMCORE_CLASS, gMethods, sizeof(gMethods) / sizeof(gMethods[0]));
    return JNI_VERSION_1_6;
}

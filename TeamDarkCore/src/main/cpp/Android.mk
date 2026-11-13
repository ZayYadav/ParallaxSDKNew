LOCAL_PATH := $(call my-dir)
MAIN_LOCAL_PATH := $(LOCAL_PATH)

# ========== TeamDarkCore shared library ==========
include $(CLEAR_VARS)

# --- Module Name ---
LOCAL_MODULE := TeamDarkCore

# -------- C FLAGS (SAFE) --------
LOCAL_CFLAGS := \
	-O3 \
	-Wno-error=format-security \
	-fvisibility=hidden \
	-ffunction-sections \
	-fdata-sections \
	-fstack-protector-strong \
	-D_FORTIFY_SOURCE=2 \
	-fcommon

# -------- CPP FLAGS (NO CODE REMOVED) --------
LOCAL_CPPFLAGS := \
	-std=c++17 \
	-Wno-error=format-security \
	-Wno-error=c++11-narrowing \
	-fvisibility=hidden \
	-fvisibility-inlines-hidden \
	-ffunction-sections \
	-fdata-sections \
	-fstack-protector-strong \
	-D_FORTIFY_SOURCE=2 \
	-fexceptions \
	-fno-rtti \
	-fno-unwind-tables \
	-fno-asynchronous-unwind-tables \
	-fcommon

# -------- LINKER FLAGS --------
LOCAL_LDFLAGS := \
	-Wl,--gc-sections \
	-Wl,--strip-all \
	-Wl,--exclude-libs,ALL \
	-Wl,-z,relro \
	-Wl,-z,now \
	-Wl,-z,noexecstack

LOCAL_LDLIBS := -llog -landroid -lz -ldl
LOCAL_ARM_MODE := arm

# --- Include Paths ---
LOCAL_C_INCLUDES := $(LOCAL_PATH)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Hook
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Hook/And64InlineHook
LOCAL_C_INCLUDES += $(LOCAL_PATH)/Hook/Substrate
LOCAL_C_INCLUDES += $(LOCAL_PATH)/IO
LOCAL_C_INCLUDES += $(LOCAL_PATH)/JniHook
LOCAL_C_INCLUDES += $(LOCAL_PATH)/SandHook
LOCAL_C_INCLUDES += $(LOCAL_PATH)/SandHook/xdl
LOCAL_C_INCLUDES += $(LOCAL_PATH)/SandHook/libzip
LOCAL_C_INCLUDES += $(LOCAL_PATH)/KittyMemory

# --- ADD: libzip headers ---
LOCAL_C_INCLUDES += $(LOCAL_PATH)/libzip

# --- Source Files ---
TDCORE_CORE_SRC := $(LOCAL_PATH)/BoxCore.cpp \
                        $(LOCAL_PATH)/AnogsCompat.cpp \
                        $(LOCAL_PATH)/IO.cpp \
                        $(LOCAL_PATH)/KittyMemory/KittyMemory.cpp \
                        $(LOCAL_PATH)/KittyMemory/KittyUtils.cpp \
                        $(LOCAL_PATH)/KittyMemory/MemoryBackup.cpp \
                        $(LOCAL_PATH)/KittyMemory/MemoryPatch.cpp

# C Files (xdl - SandHook/xdl)
TDCORE_CORE_SRC += $(wildcard $(LOCAL_PATH)/SandHook/xdl/*.c)

ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    TDCORE_CORE_SRC += $(LOCAL_PATH)/Hook/And64InlineHook/And64InlineHook.cpp
endif
ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
    TDCORE_CORE_SRC += $(LOCAL_PATH)/Hook/Substrate/SubstrateHook.cpp \
                            $(LOCAL_PATH)/Hook/Substrate/SubstratePosixMemory.cpp \
                            $(LOCAL_PATH)/Hook/Substrate/SubstrateDebug.cpp
endif

LOCAL_SRC_FILES := $(TDCORE_CORE_SRC:$(LOCAL_PATH)/%=%)

# --- Build Shared Library ---
include $(BUILD_SHARED_LIBRARY)

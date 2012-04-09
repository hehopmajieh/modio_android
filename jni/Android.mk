LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := axonI2C
LOCAL_LDLIBS 	:= -llog
### Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := axonI2C.c

include $(BUILD_SHARED_LIBRARY)

#include <jni.h>
/* Header for class axomI2C */
#include <stdio.h>
#include <android/log.h>
#include <fcntl.h>
#include <linux/i2c.h>
#include <memory.h>
#include <malloc.h>

#ifndef _Included_axon_I2C
#define _Included_axon_I2C
#ifdef __cplusplus
  extern "C" {
#endif
/*
 * Class:     axon_I2C
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_axon_I2C_open
  (JNIEnv *, jobject, jstring);

/*
 * Class:     axon_I2C
 * Method:    read
 * Signature: (II[II)I
 */
JNIEXPORT jint JNICALL Java_axon_I2C_read
  (JNIEnv *, jobject, jint, jint, jintArray, jint);

/*
 * Class:     axon_I2C
 * Method:    write
 * Signature: (III[II)I
 */
JNIEXPORT jint JNICALL Java_axon_I2C_write
  (JNIEnv *, jobject, jint, jint, jint, jintArray, jint);

/*
 * Class:     axon_I2c
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_axon_I2C_close
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif

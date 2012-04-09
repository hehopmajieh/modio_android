#include "axonI2C.h"

#define  LOG_TAG    "axoni2c"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

//***************************************************************************
// Open the I2C device
//***************************************************************************

JNIEXPORT jint JNICALL Java_axon_test_testJNI_axonI2C_open(JNIEnv *env, jobject obj, jstring file)
{
    char fileName[64];
    const jbyte *str;
    
    str = (*env)->GetStringUTFChars(env, file, NULL);
    if (str == NULL) {
        LOGI("Can't get file name!");
        return -1;
    }
    sprintf(fileName, "%s", str);
    LOGI("will open i2c device node %s", fileName);
    (*env)->ReleaseStringUTFChars(env, file, str);
    return open(fileName, O_RDWR);
}

//***************************************************************************
// Read data from the I2C device
//***************************************************************************
  
JNIEXPORT jint JNICALL Java_axon_test_testJNI_axonI2C_read(JNIEnv * env, jobject obj, jint fileHander, jint slaveAddr, jintArray bufArr, jint len)
{
    jint *bufInt;
    char *bufByte;
    int res = 0, i = 0, j = 0;
    
    if (len <= 0) {
    LOGE("I2C: buf len <=0");
        goto err0;
    }
      
    bufInt = (jint *) malloc(len * sizeof(int));
    if (bufInt == 0) {
        LOGE("I2C: nomem");
        goto err0;
    }
    bufByte = (char*) malloc(len);
    if (bufByte == 0) {
        LOGE("I2C: nomem");
        goto err1;
    }
    
    (*env)->GetIntArrayRegion(env, bufArr, 0, len, bufInt);
    
    res = ioctl(fileHander, I2C_SLAVE, slaveAddr);
    if (res != 0) {
        LOGE("I2C: Can't set slave address");
        goto err2;
    }
    
    memset(bufByte, '\0', len);
    if ((j = read(fileHander, bufByte, len)) != len) {
        LOGE("read fail in i2c read jni i = %d buf 4", i);
        goto err2;        
    } 
    else {
    for (i = 0; i < j ; i++)
        bufInt[i] = bufByte[i];
        LOGI("return %d %d %d %d in i2c read jni", bufByte[0], bufByte[1], bufByte[2], bufByte[3]);
        (*env)->SetIntArrayRegion(env, bufArr, 0, len, bufInt);
    }
    free(bufByte);
    free(bufInt);
    
    return j;
    
    err2:
    free(bufByte);
    err1:
    free(bufInt);
    err0:
    return -1;                                          
}

//***************************************************************************
// Write data to the I2C device
//***************************************************************************
  
JNIEXPORT jint JNICALL Java_axon_test_testJNI_axonI2C_write(JNIEnv *env, jobject obj, jint fileHander, 
                                           jint slaveAddr, jint mode, jintArray bufArr, jint len)
{
    jint *bufInt;
    char *bufByte;
    int res = 0, i = 0, j = 0;
    
    if (len <= 0) {
        LOGE("I2C: buf len <=0");
        goto err0;
    }
    
    bufInt = (jint *) malloc(len * sizeof(int));
    if (bufInt == 0) {
        LOGE("I2C: nomem");
        goto err0;
    }
    bufByte = (char*) malloc(len + 1);
    if (bufByte == 0) {
        LOGE("I2C: nomem");
        goto err1;
    }
    
    (*env)->GetIntArrayRegion(env, bufArr, 0, len, bufInt);
    bufByte[0] = mode;
    for (i = 0; i < len; i++)
        bufByte[i] = bufInt[i];      
    
    res = ioctl(fileHander, I2C_SLAVE, slaveAddr);
    if (res != 0) {
        LOGE("I2C: Can't set slave address");
        goto err2;
    }
    
    if ((j = write(fileHander, bufByte, len)) != len) {
        LOGE("write fail in i2c");
        goto err2;        
    }
    
    LOGI("I2C: write %d byte", j);
    free(bufByte);
    free(bufInt);
    
    return j - 1;

err2:
    free(bufByte);
err1:
    free(bufInt);
err0:
    return -1;     
}
  
//***************************************************************************
// Close the I2C device
//***************************************************************************

JNIEXPORT void JNICALL Java_axon_test_testJNI_axonI2C_close(JNIEnv *env, jobject obj, jint fileHander)
{
    close(fileHander);
}

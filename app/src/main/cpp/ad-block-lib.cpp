//
// Created by Xeror on 1/12/2019.
//

#include <jni.h>
#include <android/log.h>
#include <string>

#include "ad_block_client.h"

#define TAG JNI_LOG

#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR,    TAG, __VA_ARGS__)
#define LOG_W(...) __android_log_print(ANDROID_LOG_WARN,     TAG, __VA_ARGS__)
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO,     TAG, __VA_ARGS__)
#define LOG_D(...) __android_log_print(ANDROID_LOG_DEBUG,    TAG, __VA_ARGS__)


// Memory leak?
static AdBlockClient client;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_MainActivity_initAdBlocker(
        JNIEnv* env,
        jobject,
        jbyteArray filterBytes) {
    jboolean isCopyByteArray;

    jbyte* tmp = env->GetByteArrayElements(filterBytes, &isCopyByteArray);

    bool result = client.deserialize(reinterpret_cast<char*>(tmp));

    if (isCopyByteArray == JNI_TRUE) {
        env->ReleaseByteArrayElements(filterBytes, tmp, 0);
    }

    return result;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_MainActivity_shouldBlockUrl(
        JNIEnv* env,
        jobject,
        jstring domain_str,
        jstring url_str) {

    jboolean isCopyDomain;
    jboolean isCopyUrl;

    const char* domain = env->GetStringUTFChars(domain_str, &isCopyDomain);
    const char* url = env->GetStringUTFChars(url_str, &isCopyUrl);

    bool shouldBlock = client.matches(url, FONoFilterOption, domain);

    if (isCopyDomain == JNI_TRUE) {
        env->ReleaseStringUTFChars(domain_str, domain);
    }

    if (isCopyUrl == JNI_TRUE) {
        env->ReleaseStringUTFChars(url_str, url);
    }

    return shouldBlock;
}

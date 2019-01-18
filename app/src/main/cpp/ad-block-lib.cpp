//
// Created by Andy on 1/12/2019.
//

#include <jni.h>
#include <android/log.h>
#include <string>

#include "ad_block_client.h"

extern "C" JNIEXPORT jlong JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_createAdBlockClient(
        JNIEnv *env,
        jobject) {
    return reinterpret_cast<jlong>(new AdBlockClient());
}

extern "C" JNIEXPORT void JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_destroyAdBlockClient(
        JNIEnv *env,
        jobject,
        jlong handle) {
    delete reinterpret_cast<AdBlockClient*>(handle);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_initAdBlockClient(
        JNIEnv *env,
        jobject,
        jlong handle,
        jbyteArray filterBytes) {
    jboolean isCopyByteArray;

    jbyte *tmp = env->GetByteArrayElements(filterBytes, &isCopyByteArray);

    char *buffer = reinterpret_cast<char *>(tmp);

    bool result = reinterpret_cast<AdBlockClient*>(handle)->deserialize(buffer);

    if (isCopyByteArray == JNI_TRUE) {
        env->ReleaseByteArrayElements(filterBytes, tmp, 0);
    }

    return result;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_shouldBlockUrl(
        JNIEnv *env,
        jobject,
        jlong handle,
        jstring _currentPageDomain,
        jstring _urlToCheck) {
    jboolean isCopyDomain;
    jboolean isCopyUrl;

    const char *currentPageDomain = env->GetStringUTFChars(_currentPageDomain, &isCopyDomain);
    const char *urlToCheck = env->GetStringUTFChars(_urlToCheck, &isCopyUrl);

    bool shouldBlock = reinterpret_cast<AdBlockClient*>(handle)->matches(urlToCheck, FONoFilterOption, currentPageDomain);

    if (isCopyDomain == JNI_TRUE) {
        env->ReleaseStringUTFChars(_currentPageDomain, currentPageDomain);
    }

    if (isCopyUrl == JNI_TRUE) {
        env->ReleaseStringUTFChars(_urlToCheck, urlToCheck);
    }

    return shouldBlock;
}
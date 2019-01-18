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
    delete reinterpret_cast<AdBlockClient *>(handle);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_initAdBlockClient(
        JNIEnv *env,
        jobject,
        jlong handle,
        jbyteArray filterData) {

    bool result = false;

    jbyte *data = env->GetByteArrayElements(filterData, NULL);
    if (data != nullptr) {
        char *buffer = reinterpret_cast<char *>(data);
        result = reinterpret_cast<AdBlockClient *>(handle)->deserialize(buffer);
        env->ReleaseByteArrayElements(filterData, data, JNI_ABORT);
    }

    return result;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockProvider_shouldBlockUrl(
        JNIEnv *env,
        jobject,
        jlong handle,
        jstring domain,
        jstring url) {
    const char *currentPageDomain = env->GetStringUTFChars(domain, nullptr);
    const char *urlToCheck = env->GetStringUTFChars(url, nullptr);

    bool shouldBlock = reinterpret_cast<AdBlockClient *>(handle)->matches(urlToCheck,
                                                                          FONoFilterOption,
                                                                          currentPageDomain);

    if (currentPageDomain != nullptr) {
        env->ReleaseStringUTFChars(domain, currentPageDomain);
    }

    if (urlToCheck != nullptr) {
        env->ReleaseStringUTFChars(url, urlToCheck);
    }

    return shouldBlock;
}
//
// Created by Xeror on 1/12/2019.
//

#include <jni.h>
#include <android/log.h>
#include <string>

#include "ad_block_client.h"

// Memory leak?
static AdBlockClient client;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_AdBlockWebViewClient_initAdBlocker(
        JNIEnv* env,
        jclass,
        jbyteArray filterBytes) {
    jboolean isCopyFilterBytes;

    client = AdBlockClient{};

    jbyte* tmp = env->GetByteArrayElements(filterBytes, &isCopyFilterBytes);

    bool result = client.deserialize(reinterpret_cast<char*>(tmp));

    if (isCopyFilterBytes == JNI_TRUE) {
        env->ReleaseByteArrayElements(filterBytes, tmp, 0);
    }

    return result;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_AdBlockWebViewClient_shouldBlockUrl(
        JNIEnv* env,
        jobject,
        jstring currentPageDomain,
        jstring urlToCheck) {

    jboolean isCopyCurrentPageDomain;
    jboolean isCopyUrlToCheck;

    const char* domain = env->GetStringUTFChars(currentPageDomain, &isCopyCurrentPageDomain);
    const char* url = env->GetStringUTFChars(urlToCheck, &isCopyUrlToCheck);

    bool shouldBlock = client.matches(url, FONoFilterOption, domain);

    if (isCopyCurrentPageDomain == JNI_TRUE) {
        env->ReleaseStringUTFChars(currentPageDomain, domain);
    }

    if (isCopyUrlToCheck == JNI_TRUE) {
        env->ReleaseStringUTFChars(urlToCheck, url);
    }

    return shouldBlock;
}

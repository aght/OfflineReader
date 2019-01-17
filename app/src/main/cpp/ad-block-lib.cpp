//
// Created by Xeror on 1/12/2019.
//

#include <jni.h>
#include <android/log.h>
#include <string>

#include "ad_block_client.h"

static AdBlockClient client;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockWebViewClient_initAdBlockClient(
        JNIEnv* env,
        jclass,
        jbyteArray filterBytes) {
    jboolean isCopyByteArray;

    jbyte* tmp = env->GetByteArrayElements(filterBytes, &isCopyByteArray);

    char* buffer = reinterpret_cast<char*>(tmp);

    bool result = client.deserialize(buffer);

    if (isCopyByteArray == JNI_TRUE) {
        env->ReleaseByteArrayElements(filterBytes, tmp, 0);
    }

    return result;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_adblock_webview_AdBlockWebViewClient_shouldBlockUrl(
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

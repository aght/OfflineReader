//
// Created by Xeror on 1/12/2019.
//

#include <jni.h>
#include <string>

#include "ad_block_client.h"

JNIEXPORT jboolean JNICALL
Java_com_aght_offlinereader_AdBlockWebViewClient_shouldBlockUrl(
        JNIEnv* env,
        jobject,
        jstring domain_str,
        jstring url_str,
        jbyteArray bytes) {

/*
    jboolean isCopyDomain;
    jboolean isCopyUrl;
    jboolean isCopyByteArray;

    const char* domain = env->GetStringUTFChars(domain_str, &isCopyDomain);
    const char* url = env->GetStringUTFChars(url_str, &isCopyUrl);

    jbyte* tmp = env->GetByteArrayElements(bytes, &isCopyByteArray);

    char* buffer = (char*) tmp;

    AdBlockClient client;
    client.deserialize(buffer);

    bool shouldBlock = client.matches(url, FONoFilterOption, domain);

    if (isCopyDomain == JNI_TRUE) {
        env->ReleaseStringUTFChars(domain_str, domain);
    }

    if (isCopyUrl == JNI_TRUE) {
        env->ReleaseStringUTFChars(url_str, url);
    }

    if (isCopyByteArray == JNI_TRUE) {
        env->ReleaseByteArrayElements(bytes, tmp, 0);
    }
*/

    return false;
}

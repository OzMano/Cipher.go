#ifndef CIPHERGO_CIPHER_LIB_H
#define CIPHERGO_CIPHER_LIB_H

#include <jni.h>
#include <map>
#include <string>

using namespace std;

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jint
JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved);

JNIEXPORT void JNICALL
Java_dom_team_ciphergo_CipherCore_init(JNIEnv *env, jclass type);

JNIEXPORT jstring
JNICALL
Java_dom_team_ciphergo_CipherCore_getString(JNIEnv *env, jobject
instance, jstring key_);

#ifdef __cplusplus
};
#endif



#endif //CIPHERGO_CIPHER_LIB_H

#ifndef CIPHERGO_ENVIRONMENTCHECKER_H
#define CIPHERGO_ENVIRONMENTCHECKER_H

#include <jni.h>

class Environments {

private:

    JNIEnv *jniEnv;
    jobject context;

    jobject getPackageInfo();

    jstring getPackageName();

    bool checkSignature();

public:
    Environments(JNIEnv *env, jobject context);

    bool check();

    jobject getContext();

    jobject getApplicationContext(jobject context);
};


#endif //CIPHERGO_ENVIRONMENTCHECKER_H

#include <jni.h>
#include "com_lpr_LPR.h"

JNIEXPORT jbyteArray JNICALL Java_com_lpr_LPR_DetectLPR
  (JNIEnv * jnienv, jobject jobj, jshortArray intArray, jint width, jint height,jint maxSize)
{
    jbyteArray  returnLPRArray = jnienv->NewByteArray( 1024 );
    return   returnLPRArray ;
}
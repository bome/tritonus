/*
 *	common.h
 */

#include	<jni.h>

void
throwRuntimeException(JNIEnv *env, char* pStrMessage);
void
checkArrayLength(JNIEnv *env, jarray array, int nRequiredLength);
void
setStringArrayElement(JNIEnv *env, jobjectArray array, int nIndex, const char* string1);


/*** common.h ***/

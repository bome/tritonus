/*
 *	common.c
 */

#include	"common.h"

void
throwRuntimeException(JNIEnv *env, char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
		// printf("RTE: %p\n", runtimeExceptionClass);
		if (runtimeExceptionClass == NULL)
		{
			(*env)->FatalError(env, "cannot get class object for java.lang.RuntimeException");
		}
	}
	(*env)->ThrowNew(env, runtimeExceptionClass, pStrMessage);
}



void
checkArrayLength(JNIEnv *env, jarray array, int nRequiredLength)
{
	int	nLength;

	nLength = (*env)->GetArrayLength(env, array);
	if (nLength < nRequiredLength)
	{
		throwRuntimeException(env, "array does not have enough elements");
	}
}



void
setStringArrayElement(JNIEnv *env, jobjectArray array, int nIndex, const char* string1)
{
	jstring		string2;

	string2 = (*env)->NewStringUTF(env, string1);
	if (string1 == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	(*env)->SetObjectArrayElement(env, array, nIndex, string2);
}



/*** common.c ***/

/*
 *	common.cc
 */

#include	"common.h"

static bool DEBUG = false;



void
throwRuntimeException(JNIEnv *env, char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if (env->ExceptionOccurred() != NULL)
	{
		if (DEBUG) { env->ExceptionDescribe(); }
		env->ExceptionClear();
	}
	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = env->FindClass("java/lang/RuntimeException");
		if (DEBUG) { printf("RTE: %p\n", runtimeExceptionClass); }
		if (runtimeExceptionClass == NULL)
		{
			env->FatalError("cannot get class object for java.lang.RuntimeException");
		}
	}
	env->ThrowNew(runtimeExceptionClass, pStrMessage);
}



void
checkArrayLength(JNIEnv *env, jarray array, int nRequiredLength)
{
	int	nLength;

	nLength = env->GetArrayLength(array);
	if (nLength < nRequiredLength)
	{
		throwRuntimeException(env, "array does not have enough elements");
	}
}



void
setStringArrayElement(JNIEnv *env, jobjectArray array, int nIndex, const char* string1)
{
	jstring		string2;

	string2 = env->NewStringUTF(string1);
	if (string1 == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	env->SetObjectArrayElement(array, nIndex, string2);
}



/*** common.cc ***/

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
		if (runtimeExceptionClass == NULL)
		{
			(*env)->FatalError(env, "cannot get class object for java.lang.RuntimeException");
		}
	}
	(*env)->ThrowNew(env, runtimeExceptionClass, pStrMessage);
}


/*** common.c ***/

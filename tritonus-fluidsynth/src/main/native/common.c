/*
 *	common.c
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */


#include "common.h"
#include "debug.h"



void
throwRuntimeException(JNIEnv *env, const char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if ((*env)->ExceptionOccurred(env) != NULL)
	{
		if (debug_flag) { (*env)->ExceptionDescribe(env); }
		(*env)->ExceptionClear(env);
	}
	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
		if (debug_flag) { fprintf(debug_file, "RTE: %p\n", runtimeExceptionClass); }
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

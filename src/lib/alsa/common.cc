/*
 *	common.cc
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */


#include	"common.h"



void
throwRuntimeException(JNIEnv *env, const char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if (env->ExceptionOccurred() != NULL)
	{
		if (debug_flag) { env->ExceptionDescribe(); }
		env->ExceptionClear();
	}
	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = env->FindClass("java/lang/RuntimeException");
		if (debug_flag) { fprintf(debug_file, "RTE: %p\n", runtimeExceptionClass); }
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

	// printf("+1");
	nLength = env->GetArrayLength(array);
	// printf("+2");
	if (nLength < nRequiredLength)
	{
	// printf("+2a");
		throwRuntimeException(env, "array does not have enough elements");
	// printf("+2b");
	}
	// printf("+3");
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

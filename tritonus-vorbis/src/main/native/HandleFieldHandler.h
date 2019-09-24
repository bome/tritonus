/*
 *	HandleFieldHandler.h
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

#ifndef _HANDLE_FIELD_HANDLER_H
#define _HANDLE_FIELD_HANDLER_H


#include <jni.h>


#define HandleFieldHandler(_type)            \
HandleFieldHandlerDeclaration(_handler, _type)

#define HandleFieldHandlerDeclaration(_variableName, _type)            \
static jfieldID  _variableName ## FieldID = NULL;                      \
                                                                       \
static jfieldID                                                        \
getNativeHandleFieldID(JNIEnv *env, jobject obj)                       \
{                                                                      \
	if (_variableName ## FieldID == NULL)                          \
	{                                                              \
		jclass	cls = (*env)->GetObjectClass(env, obj);                \
		if (cls == NULL)                                       \
		{                                                      \
			throwRuntimeException(env, "cannot get class"); \
		}                                                      \
		_variableName ## FieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J"); \
		if (_variableName ## FieldID == NULL)                  \
		{                                                      \
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle"); \
		}                                                      \
	}                                                              \
	return _variableName ## FieldID;                               \
}                                                                      \
                                                                       \
static void                                                            \
setHandle(JNIEnv *env, jobject obj, _type handle)                      \
{                                                                      \
	jfieldID	fieldID = getNativeHandleFieldID(env, obj);    \
	(*env)->SetLongField(env, obj, fieldID, (jlong) (int) handle);       \
}                                                                      \
                                                                       \
static _type                                                           \
getHandle(JNIEnv *env, jobject obj)                                    \
{                                                                      \
	jfieldID	fieldID = getNativeHandleFieldID(env, obj);    \
	_type	handle = (_type) (int) (*env)->GetLongField(env, obj, fieldID); \
	return handle;                                                 \
}



#endif /* _HANDLE_FIELD_HANDLER_H */


/*** HandleFieldHandler.h ***/

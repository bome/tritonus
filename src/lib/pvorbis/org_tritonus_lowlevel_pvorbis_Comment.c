/*
 *	org_tritonus_lowlevel_pvorbis_Comment.c
 */

/*
 *  Copyright (c) 2003 -2004 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

#include "common.h"
#include "org_tritonus_lowlevel_pvorbis_Comment.h"


HandleFieldHandlerDeclaration(handler, vorbis_comment*)


vorbis_comment*
getCommentNativeHandle(JNIEnv *env, jobject obj)
{
        return getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_malloc
(JNIEnv* env, jobject obj)
{
	vorbis_comment*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_malloc(): begin\n"); }
	handle = malloc(sizeof(vorbis_comment));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_free
(JNIEnv* env, jobject obj)
{
	vorbis_comment*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    init_1native
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_init_1native
(JNIEnv* env, jobject obj)
{
	vorbis_comment*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_init(): begin\n"); }
	handle = getHandle(env, obj);
	vorbis_comment_init(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_init(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    addComment_1native
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_addComment_1native
(JNIEnv* env, jobject obj, jstring strComment)
{
	vorbis_comment*	handle;
	const char*	comment;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_addComment(): begin\n"); }
	handle = getHandle(env, obj);
	comment = (*env)->GetStringUTFChars(env, strComment, NULL);

	vorbis_comment_add(handle, (char*) comment);
	(*env)->ReleaseStringUTFChars(env, strComment, comment);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_addComment(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    addTag_1native
 * Signature: (Ljava/lang/String;Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_addTag_1native
(JNIEnv* env, jobject obj, jstring strTag, jstring strComment)
{
	vorbis_comment*	handle;
	const char*	tag;
	const char*	comment;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_addTag(): begin\n"); }
	handle = getHandle(env, obj);
	tag = (*env)->GetStringUTFChars(env, strTag, NULL);
	comment = (*env)->GetStringUTFChars(env, strComment, NULL);
	vorbis_comment_add_tag(handle, (char*) tag, (char*) comment);
	(*env)->ReleaseStringUTFChars(env, strComment, comment);
	(*env)->ReleaseStringUTFChars(env, strTag, tag);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_addTag(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    queryCount_1native
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_queryCount_1native
(JNIEnv* env, jobject obj, jstring strTag)
{
	vorbis_comment*	handle;
	const char*	tag;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_queryCount(): begin\n"); }
	handle = getHandle(env, obj);
	tag = (*env)->GetStringUTFChars(env, strTag, NULL);
	nReturn = vorbis_comment_query_count(handle, (char*) tag);
	(*env)->ReleaseStringUTFChars(env, strTag, tag);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_queryCount(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    query_1native
 * Signature: (Ljava/lang/String;I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_query_1native
(JNIEnv* env, jobject obj, jstring strTag, jint nIndex)
{
	vorbis_comment*	handle;
	const char*	tag;
	char*		result;
	jstring		strReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_query(): begin\n"); }
	handle = getHandle(env, obj);
	tag = (*env)->GetStringUTFChars(env, strTag, NULL);
	result = vorbis_comment_query(handle, (char*) tag, nIndex);
	(*env)->ReleaseStringUTFChars(env, strTag, tag);
	strReturn = (*env)->NewStringUTF(env, result);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_query(): end\n"); }
	return strReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    getUserComments_1native
 * Signature: ()[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_getUserComments_1native
(JNIEnv* env, jobject obj)
{
	vorbis_comment*	handle;
	jclass		stringClass;
	jobjectArray	stringArray;
	int		i;
	jstring		string;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_getUserComments(): begin\n"); }
	stringClass = (*env)->FindClass(env, "java/lang/String");
	handle = getHandle(env, obj);
	stringArray = (*env)->NewObjectArray(env, handle->comments,
					     stringClass, NULL);
	for (i = 0; i < handle->comments; i++)
	{
		string = (*env)->NewStringUTF(env, handle->user_comments[i]);
		(*env)->SetObjectArrayElement(env, stringArray, i, string);
	}
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_getUserComments(): end\n"); }
	return stringArray;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    getVendor_1native
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_getVendor_1native
(JNIEnv* env, jobject obj)
{
	vorbis_comment*	handle;
	jstring		strReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_getVendor(): begin\n"); }
	handle = getHandle(env, obj);
	strReturn = (*env)->NewStringUTF(env, handle->vendor);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_getVendor(): end\n"); }
	return strReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    clear_1native
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_clear_1native
(JNIEnv* env, jobject obj)
{
	vorbis_comment*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_clear(): begin\n"); }
	handle = getHandle(env, obj);
	vorbis_comment_clear(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Comment_clear(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Comment
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Comment_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_pvorbis_Comment.c ***/

/*
 *	org_tritonus_lowlevel_pogg_Page
 */

/*
 *  Copyright (c) 2003 - 2004 by Matthias Pfisterer
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
#include "org_tritonus_lowlevel_pogg_Page.h"

HandleFieldHandlerDeclaration(handler, ogg_page*)


ogg_page*
getPageNativeHandle(JNIEnv *env, jobject obj)
{
        return getHandle(env, obj);
}
 


/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_Page_malloc
(JNIEnv* env, jobject obj)
{
	ogg_page*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_malloc(): begin\n"); }
	handle = malloc(sizeof(ogg_page));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	handle->header = NULL;
	handle->body = NULL;
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_Page_free
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getVersion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getVersion
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getVersion(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_version(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getVersion(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    isContinued
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_pogg_Page_isContinued
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isContinued(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_continued(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isContinued(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getPackets
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getPackets
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getPackets(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_packets(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getPackets(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    isBos
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_pogg_Page_isBos
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isBos(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_bos(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isBos(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    isEos
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_pogg_Page_isEos
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isEos(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_eos(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_isEos(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getGranulePos
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getGranulePos
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	jlong		lReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getGranulePos(): begin\n"); }
	handle = getHandle(env, obj);
	lReturn = ogg_page_granulepos(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getGranulePos(): end\n"); }
	return lReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getSerialNo
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getSerialNo
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getSerialNo(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_serialno(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getSerialNo(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getPageNo
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getPageNo
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getPageNo(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_page_pageno(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getPageNo(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    setChecksum
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_Page_setChecksum
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_setChecksum(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_page_checksum_set(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_setChecksum(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getHeader
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getHeader
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	jbyteArray	byteArray;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getHeader(): begin\n"); }
	handle = getHandle(env, obj);
	byteArray = (*env)->NewByteArray(env, handle->header_len);
	(*env)->SetByteArrayRegion(env, byteArray, 0, handle->header_len, handle->header);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getHeader(): end\n"); }
	return byteArray;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    getBody
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_tritonus_lowlevel_pogg_Page_getBody
(JNIEnv* env, jobject obj)
{
	ogg_page*	handle;
	jbyteArray	byteArray;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getBody(): begin\n"); }
	handle = getHandle(env, obj);
	byteArray = (*env)->NewByteArray(env, handle->body_len);
	(*env)->SetByteArrayRegion(env, byteArray, 0, handle->body_len, handle->body);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_getBody(): end\n"); }
	return byteArray;
}


/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    setData
 * Signature: ([BII[BII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_Page_setData
(JNIEnv* env, jobject obj, jbyteArray abHeader, jint nHeaderOffset, jint nHeaderLength, jbyteArray abBody, jint nBodyOffset, jint nBodyLength)
{
	ogg_page*	handle;
	//jbyteArray	byteArray;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_setData(): begin\n"); }
	handle = getHandle(env, obj);
	/* ATTENTION! The storage allocated here is never freed! We have a memory hole! */
	handle->header_len = nHeaderLength;
	if (handle->header != NULL)
	{
		free(handle->header);
	}
	handle->header = malloc(nHeaderLength);
	handle->body_len = nBodyLength;
	if (handle->body != NULL)
	{
		free(handle->body);
	}
	handle->body = malloc(nBodyLength);
	(*env)->GetByteArrayRegion(env, abHeader, nHeaderOffset, nHeaderLength, handle->header);
	(*env)->GetByteArrayRegion(env, abBody, nBodyOffset, nBodyLength, handle->body);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_Page_setData(): end\n"); }
}


/*
 * Class:     org_tritonus_lowlevel_pogg_Page
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_Page_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_pogg_Page.c ***/

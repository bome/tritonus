/*
 *	org_tritonus_lowlevel_pogg_StreamState.c
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
#include "org_tritonus_lowlevel_pogg_StreamState.h"

HandleFieldHandlerDeclaration(handler, ogg_stream_state*)

ogg_packet*
getPacketNativeHandle(JNIEnv *env, jobject obj);
ogg_page*
getPageNativeHandle(JNIEnv *env, jobject obj);


/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_malloc
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_malloc(): begin\n"); }
	handle = malloc(sizeof(ogg_stream_state));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_free
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    init
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_init_1native
(JNIEnv* env, jobject obj, jint nSerialNo)
{
	ogg_stream_state*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_init(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_stream_init(handle, nSerialNo);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_init(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    clear
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_clear_1native
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_clear(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_stream_clear(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_clear(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    reset
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_reset_1native
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_reset(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_stream_reset(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_reset(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    destroy
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_destroy_1native
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_destroy(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_stream_destroy(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_destroy(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    isEOSReached
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_isEOSReached_1native
(JNIEnv* env, jobject obj)
{
	ogg_stream_state*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_isEOSReached(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = ogg_stream_eos(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_isEOSReached(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    pageIn
 * Signature: (Lorg/tritonus/lowlevel/ogg/Page;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_pageIn_1native
(JNIEnv* env, jobject obj, jobject page)
{
	ogg_stream_state*	handle;
	ogg_page*		pageHandle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_pageIn(): begin\n"); }
	handle = getHandle(env, obj);
	pageHandle = getPageNativeHandle(env, page);
	nReturn = ogg_stream_pagein(handle, pageHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_pageIn(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    packetOut
 * Signature: (Lorg/tritonus/lowlevel/ogg/Packet;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_packetOut_1native
(JNIEnv* env, jobject obj, jobject packet)
{
	ogg_stream_state*	handle;
	ogg_packet*		packetHandle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_packetOut(): begin\n"); }
	handle = getHandle(env, obj);
	packetHandle = getPacketNativeHandle(env, packet);
	nReturn = ogg_stream_packetout(handle, packetHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_packetOut(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    packetPeek
 * Signature: (Lorg/tritonus/lowlevel/ogg/Packet;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_packetPeek_1native
(JNIEnv* env, jobject obj, jobject packet)
{
	ogg_stream_state*	handle;
	ogg_packet*		packetHandle = NULL;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_packetPeek(): begin\n"); }
	handle = getHandle(env, obj);
	if (packet != NULL)
	{
		packetHandle = getPacketNativeHandle(env, packet);
	}
	nReturn = ogg_stream_packetpeek(handle, packetHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pogg_StreamState_packetPeek(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pogg_StreamState
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pogg_StreamState_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}




/*** org_tritonus_lowlevel_pogg_StreamState.c ***/

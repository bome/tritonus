/*
 *	org_tritonus_lowlevel_pvorbis_Info.c
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
#include "org_tritonus_lowlevel_pvorbis_Info.h"


HandleFieldHandlerDeclaration(handler, vorbis_info*)

vorbis_info*
getInfoNativeHandle(JNIEnv *env, jobject obj)
{
        return getHandle(env, obj);
}

vorbis_comment*
getCommentNativeHandle(JNIEnv *env, jobject obj);
ogg_packet*
getPacketNativeHandle(JNIEnv *env, jobject obj);



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_malloc
(JNIEnv* env, jobject obj)
{
	vorbis_info*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_malloc(): begin\n"); }
	handle = malloc(sizeof(vorbis_info));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_free
(JNIEnv* env, jobject obj)
{
	vorbis_info*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    init_1native
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_init_1native
(JNIEnv* env, jobject obj)
{
	vorbis_info*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_init(): begin\n"); }
	handle = getHandle(env, obj);
	vorbis_info_init(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_init(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    clear_1native
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_clear_1native
(JNIEnv* env, jobject obj)
{
	vorbis_info*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_clear(): begin\n"); }
	handle = getHandle(env, obj);
	vorbis_info_clear(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_clear(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    getChannels_1native
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_getChannels_1native
(JNIEnv* env, jobject obj)
{
	vorbis_info*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_getChannels(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = handle->channels;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_getChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    getRate_1native
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_getRate_1native
(JNIEnv* env, jobject obj)
{
	vorbis_info*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_getRate(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = handle->rate;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_getRate(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    encodeInit_1native
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_encodeInit_1native
(JNIEnv* env, jobject obj, jint nChannels, jint nRate,
 jint nMaxBitrate, jint nNominalBitrate, jint nMinBitrate)
{
	vorbis_info*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_encodeInit(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = vorbis_encode_init(handle, nChannels, nRate,
				     nMaxBitrate, nNominalBitrate, nMinBitrate);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_encodeInit(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    encodeInitVBR_1native
 * Signature: (IIF)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_encodeInitVBR_1native
(JNIEnv* env, jobject obj, jint nChannels, jint nRate, jfloat fQuality)
{
	vorbis_info*	handle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_encodeInitVBR(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = vorbis_encode_init_vbr(handle, nChannels, nRate, fQuality);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_encodeInitVBR(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    headerIn_1native
 * Signature: (Lorg/tritonus/lowlevel/vorbis/Comment;Lorg/tritonus/lowlevel/ogg/Packet;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_headerIn_1native
(JNIEnv* env, jobject obj, jobject comment, jobject packet)
{
	vorbis_info*	handle;
	vorbis_comment*	commentHandle;
	ogg_packet*	packetHandle;
	int		nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_headerIn(): begin\n"); }
	handle = getHandle(env, obj);
	commentHandle = getCommentNativeHandle(env, comment);
	packetHandle = getPacketNativeHandle(env, packet);
	nReturn = vorbis_synthesis_headerin(handle, commentHandle, packetHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_pvorbis_Info_headerIn(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_pvorbis_Info
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_pvorbis_Info_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_pvorbis_Info.c ***/

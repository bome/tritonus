/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo.cc
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
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo.h"


static HandleFieldHandler<snd_seq_queue_info_t*>	handler;


// TODO: used in ...
snd_seq_queue_info_t*
getQueueInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_malloc(): begin\n"); }
	nReturn = snd_seq_queue_info_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_info_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_info_get_queue(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getName
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	const char*		pName;
	jstring			strName;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	pName = snd_seq_queue_info_get_name(handle);
	strName = env->NewStringUTF(pName);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getName(): end\n"); }
	return strName;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    getOwner
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getOwner
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getOwner(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_info_get_owner(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getOwner(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    getLocked
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getLocked
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getLocked(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_info_get_locked(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getLocked(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    getFlags
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getFlags
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getFlags(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_info_get_flags(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_getFlags(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    setName
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setName
(JNIEnv* env, jobject obj, jstring strName)
{
	snd_seq_queue_info_t*	handle;
	const char*		pName;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	pName = env->GetStringUTFChars(strName, NULL);
	snd_seq_queue_info_set_name(handle, pName);
	env->ReleaseStringUTFChars(strName, pName);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setName(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    setOwner
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setOwner
(JNIEnv* env, jobject obj, jint nOwner)
{
	snd_seq_queue_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setOwner(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_info_set_owner(handle, nOwner);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setOwner(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    setLocked
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setLocked
(JNIEnv* env, jobject obj, jboolean bLocked)
{
	snd_seq_queue_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setLocked(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_info_set_locked(handle, bLocked);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setLocked(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo
 * Method:    setFlags
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setFlags
(JNIEnv* env, jobject obj, jint nFlags)
{
	snd_seq_queue_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setFlags(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_info_set_flags(handle, nFlags);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueInfo_setFlags(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_QueueInfo.cc ***/

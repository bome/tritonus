/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus.cc
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


#include	<alsa/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_queue_status_t*>	handler;


snd_seq_queue_status_t*
getQueueStatusNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_malloc(): begin\n"); }
	nReturn = snd_seq_queue_status_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_free
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_status_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_queue(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    getEvents
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getEvents
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getEvents(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_events(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getEvents(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    getTickTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getTickTime
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	long			lReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getTickTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	lReturn = snd_seq_queue_status_get_tick_time(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getTickTime(): end\n"); }
	return lReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    getRealTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getRealTime
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	const snd_seq_real_time_t*	pRealTime;
	jlong			lNanoseconds;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getRealTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	pRealTime = snd_seq_queue_status_get_real_time(handle);
	lNanoseconds = (jlong) pRealTime->tv_sec * 1000000000 + pRealTime->tv_nsec;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getRealTime(): end\n"); }
	return lNanoseconds;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus
 * Method:    getStatus
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getStatus
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getStatus(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_status(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueStatus_getStatus(): end\n"); }
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_QueueStatus.cc ***/

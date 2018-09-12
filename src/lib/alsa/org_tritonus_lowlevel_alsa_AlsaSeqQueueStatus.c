/*
 *	org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus.c
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
#include "org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus.h"


HandleFieldHandler(snd_seq_queue_status_t*)


snd_seq_queue_status_t*
getQueueStatusNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_malloc(): begin\n"); }
	nReturn = snd_seq_queue_status_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_free
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_seq_queue_status_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getQueue(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_queue(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    getEvents
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getEvents
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getEvents(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_events(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getEvents(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    getTickTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getTickTime
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	long			lReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getTickTime(): begin\n"); }
	handle = getHandle(env, obj);
	lReturn = snd_seq_queue_status_get_tick_time(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getTickTime(): end\n"); }
	return lReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    getRealTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getRealTime
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	const snd_seq_real_time_t*	pRealTime;
	jlong			lNanoseconds;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getRealTime(): begin\n"); }
	handle = getHandle(env, obj);
	pRealTime = snd_seq_queue_status_get_real_time(handle);
	lNanoseconds = (jlong) pRealTime->tv_sec * 1000000000 + pRealTime->tv_nsec;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getRealTime(): end\n"); }
	return lNanoseconds;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    getStatus
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getStatus
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_status_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getStatus(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_queue_status_get_status(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_getStatus(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeqQueueStatus.c ***/

/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents.cc
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
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_remove_events_t*>	handler;


snd_seq_remove_events_t*
getRemoveEventsNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_malloc(): begin\n"); }
	nReturn = snd_seq_remove_events_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_free
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getCondition
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getCondition
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getCondition(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_remove_events_get_condition(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getCondition(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_remove_events_get_queue(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getTime
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTime
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nCondition;
	const snd_seq_timestamp_t*	time;
	jlong				lTimestamp;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nCondition = snd_seq_remove_events_get_condition(handle);
	time = snd_seq_remove_events_get_time(handle);
	if ((nCondition & SND_SEQ_REMOVE_TIME_TICK) == SND_SEQ_REMOVE_TIME_TICK)
	{
		lTimestamp = time->tick;
	}
	else	// time
	{
		lTimestamp = (jlong) time->time.tv_sec * (jlong) 1000000000 + (jlong) time->time.tv_nsec;
	}
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTime(): end\n"); }
	return lTimestamp;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getDestClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestClient
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_remove_events_get_dest(handle);
	nReturn = address->client;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getDestPort
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestPort
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestPort(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_remove_events_get_dest(handle);
	nReturn = address->port;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getDestPort(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getChannel
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getChannel
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getChannel(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_remove_events_get_channel(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getChannel(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getEventType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getEventType
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getEventType(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_remove_events_get_event_type(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getEventType(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    getTag
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTag
(JNIEnv* env, jobject obj)
{
	snd_seq_remove_events_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTag(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_remove_events_get_tag(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_getTag(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setCondition
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setCondition
(JNIEnv* env, jobject obj, jint nCondition)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setCondition(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_set_condition(handle, nCondition);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setCondition(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setQueue
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setQueue
(JNIEnv* env, jobject obj, jint nQueue)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_set_queue(handle, nQueue);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setQueue(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setTime
 * Signature: (J)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTime
(JNIEnv* env, jobject obj, jlong lTime)
{
	snd_seq_remove_events_t*	handle;
	int				nCondition;
	snd_seq_timestamp_t		time;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nCondition = snd_seq_remove_events_get_condition(handle);
	if ((nCondition & SND_SEQ_REMOVE_TIME_TICK) == SND_SEQ_REMOVE_TIME_TICK)
	{
		time.tick = lTime;
	}
	else
	{
		time.time.tv_sec = lTime / 1000000000;
		time.time.tv_nsec = lTime % 1000000000;
	}
	snd_seq_remove_events_set_time(handle, &time);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTime(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setDest
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDest
(JNIEnv* env, jobject obj, jint nClient, jint nPort)
{
	snd_seq_remove_events_t*	handle;
	snd_seq_addr_t			newAddress;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDest(): begin\n"); }
	handle = handler.getHandle(env, obj);
	newAddress.client = nClient;
	newAddress.port = nPort;
	snd_seq_remove_events_set_dest(handle, &newAddress);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDest(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setDestPort
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDestPort
(JNIEnv* env, jobject obj, jint nPort)
{
	snd_seq_remove_events_t*	handle;
	const snd_seq_addr_t*		oldAddress;
	snd_seq_addr_t			newAddress;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDestPort(): begin\n"); }
	handle = handler.getHandle(env, obj);
	oldAddress = snd_seq_remove_events_get_dest(handle);
	newAddress.client = oldAddress->client;
	newAddress.port = nPort;
	snd_seq_remove_events_set_dest(handle, &newAddress);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setDestPort(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setChannel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setChannel
(JNIEnv* env, jobject obj, jint nChannel)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setChannel(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_set_channel(handle, nChannel);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setChannel(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setEventType
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setEventType
(JNIEnv* env, jobject obj, jint nType)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setEventType(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_set_event_type(handle, nType);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setEventType(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents
 * Method:    setTag
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTag
(JNIEnv* env, jobject obj, jint nTag)
{
	snd_seq_remove_events_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTag(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_remove_events_set_tag(handle, nTag);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024RemoveEvents_setTag(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_RemoveEvents.cc ***/

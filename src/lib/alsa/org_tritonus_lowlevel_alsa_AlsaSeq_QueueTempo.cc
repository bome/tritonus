/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo.cc
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
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo.h"


static HandleFieldHandler<snd_seq_queue_tempo_t*>	handler;


snd_seq_queue_tempo_t*
getQueueTempoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_tempo_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_malloc(): begin\n"); }
	nReturn = snd_seq_queue_tempo_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_tempo_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_tempo_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_tempo_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_tempo_get_queue(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    getTempo
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getTempo
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_tempo_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getTempo(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_tempo_get_tempo(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getTempo(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    getPpq
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getPpq
(JNIEnv* env, jobject obj)
{
	snd_seq_queue_tempo_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getPpq(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_queue_tempo_get_ppq(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_getPpq(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    setTempo
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setTempo
(JNIEnv* env, jobject obj, jint nTempo)
{
	snd_seq_queue_tempo_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setTempo(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_tempo_set_tempo(handle, nTempo);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setTempo(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo
 * Method:    setPpq
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setPpq
(JNIEnv* env, jobject obj, jint nPpq)
{
	snd_seq_queue_tempo_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setPpq(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_queue_tempo_set_ppq(handle, nPpq);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024QueueTempo_setPpq(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_QueueTempo.cc ***/

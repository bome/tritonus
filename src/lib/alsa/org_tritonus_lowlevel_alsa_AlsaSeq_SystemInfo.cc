/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.cc
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
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.h"


static HandleFieldHandler<snd_seq_system_info_t*>	handler;


snd_seq_system_info_t*
getSystemInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): begin\n"); }
	nReturn = snd_seq_system_info_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_system_info_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_queues(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_clients(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getPorts
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_ports(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_channels(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getCurrentClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients(): begin\n"); }
	handle = handler.getHandle(env, obj);
	// TODO: disabled because not available in ALSA 0.9.0beta6
	nReturn = -1;	// snd_seq_system_info_get_cur_clients(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getCurrentQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues(): begin\n"); }
	handle = handler.getHandle(env, obj);
	// TODO: disabled because not available in ALSA 0.9.0beta6
	nReturn = -1;	// snd_seq_system_info_get_cur_queues(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues(): end\n"); }
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.cc ***/

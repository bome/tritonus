/*
 *	org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo.c
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
#include "org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo.h"


HandleFieldHandler(snd_seq_system_info_t*)


snd_seq_system_info_t*
getSystemInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_malloc(): begin\n"); }
	nReturn = snd_seq_system_info_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_seq_system_info_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getQueues(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_system_info_get_queues(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getQueues(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getClients(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_system_info_get_clients(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getPorts
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getPorts
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getPorts(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_system_info_get_ports(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getPorts(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getChannels
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getChannels(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_system_info_get_channels(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getCurrentClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentClients(): begin\n"); }
	handle = getHandle(env, obj);
	// TODO: disabled because not available in ALSA 0.9.0beta6
	nReturn = -1;	// snd_seq_system_info_get_cur_clients(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    getCurrentQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentQueues(): begin\n"); }
	handle = getHandle(env, obj);
	// TODO: disabled because not available in ALSA 0.9.0beta6
	nReturn = -1;	// snd_seq_system_info_get_cur_queues(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_getCurrentQueues(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeqSystemInfo.c ***/

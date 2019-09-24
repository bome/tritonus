/*
 *	org_tritonus_lowlevel_alsa_AlsaSeqPortInfo.c
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
#include "org_tritonus_lowlevel_alsa_AlsaSeqPortInfo.h"


HandleFieldHandler(snd_seq_port_info_t*)


snd_seq_port_info_t*
getPortInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_malloc(): begin\n"); }
	nReturn = snd_seq_port_info_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_malloc(): end\n"); }
	return nReturn;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_seq_port_info_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getClient
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getClient(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_client(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getPort
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPort
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPort(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_port(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPort(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getName
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	const char*		name;
	jstring			nameObj;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getName(): begin\n"); }
	handle = getHandle(env, obj);
	name = snd_seq_port_info_get_name(handle);
	nameObj = (*env)->NewStringUTF(env, name);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getName(): end\n"); }
	return nameObj;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getCapability
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getCapability
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getCapability(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_capability(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getCapability(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getType
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getType(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_type(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getType(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getMidiChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiChannels
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiChannels(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_midi_channels(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getMidiVoices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiVoices
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiVoices(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_midi_voices(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getMidiVoices(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getSynthVoices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getSynthVoices
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getSynthVoices(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_synth_voices(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getSynthVoices(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getReadUse
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getReadUse
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getReadUse(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_read_use(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getReadUse(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getWriteUse
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getWriteUse
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getWriteUse(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_write_use(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getWriteUse(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    getPortSpecified
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPortSpecified
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPortSpecified(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_seq_port_info_get_port_specified(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_getPortSpecified(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeqPortInfo
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeqPortInfo_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeqPortInfo.c ***/

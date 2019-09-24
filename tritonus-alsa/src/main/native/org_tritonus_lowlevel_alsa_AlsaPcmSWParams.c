/*
 *	org_tritonus_lowlevel_alsa_AlsaPcmSWParams.c
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
#include "org_tritonus_lowlevel_alsa_AlsaPcmSWParams.h"


HandleFieldHandler(snd_pcm_sw_params_t*)



snd_pcm_sw_params_t*
getSWParamsNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_malloc
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_malloc(): begin\n"); }
	nReturn = snd_pcm_sw_params_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_free
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_sw_params_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getStartMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getStartMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_start_mode(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getXrunMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getXrunMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_xrun_mode(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getTStampMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getTStampMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_tstamp_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_tstamp_mode(handle, &nValue);
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getSleepMin
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getSleepMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	unsigned int	nValue;
	int			nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_sleep_min(handle, &nValue);
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getAvailMin
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getAvailMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_avail_min(handle, &nValue);
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getXferAlign
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getXferAlign
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_xfer_align(handle, &nValue);
	return nValue;
}




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getStartThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getStartThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_start_threshold(handle, &nValue);
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getStopThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getStopThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_stop_threshold(handle, &nValue);
	return nValue;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getSilenceThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getSilenceThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_silence_threshold(handle, &nValue);
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmSWParams
 * Method:    getSilenceSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmSWParams_getSilenceSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	snd_pcm_uframes_t	nValue;
	int					nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_silence_size(handle, &nValue);
	return nValue;
}



/*** org_tritonus_lowlevel_alsa_AlsaPcmSWParams.c ***/

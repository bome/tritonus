/*
 *	org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask.c
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
#include "org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask.h"


HandleFieldHandler(snd_pcm_format_mask_t*)



snd_pcm_format_mask_t*
getFormatMaskNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_malloc
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_malloc(): begin\n"); }
	nReturn = snd_pcm_format_mask_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_free
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_format_mask_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_free(): end\n"); }
}

//-------------

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    none
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_none
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_none(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_format_mask_none(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_none(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    any
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_any
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_any(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_format_mask_any(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_any(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    test
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_test
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_test(): begin\n"); }
	handle = getHandle(env, obj);
	nReturn = snd_pcm_format_mask_test(handle, (snd_pcm_format_t) nFormat);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_test(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    set
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_set
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_set(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_format_mask_set(handle, (snd_pcm_format_t) nFormat);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_set(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask
 * Method:    reset
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_reset
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_reset(): begin\n"); }
	handle = getHandle(env, obj);
	snd_pcm_format_mask_reset(handle, (snd_pcm_format_t) nFormat);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask_reset(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaPcmHWParamsFormatMask.c ***/

/*
 *	org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask.cc
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
#include	<errno.h>
#include	"org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask.h"
#include	"common.h"
#include	"HandleFieldHandler.hh"


static int	DEBUG = 0;
static FILE*	debug_file = NULL;


static HandleFieldHandler<snd_pcm_format_mask_t*>	handler;






snd_pcm_format_mask_t*
getFormatMaskNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_malloc
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask_malloc(): begin\n"); }
	nReturn = snd_pcm_format_mask_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_free
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_format_mask_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask_free(): end\n"); }
}

//-------------

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    none
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_none
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_none(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_format_mask_none(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_none(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    any
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_any
(JNIEnv *env, jobject obj)
{
	snd_pcm_format_mask_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_any(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_format_mask_any(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_any(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    test
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_test
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_test(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_format_mask_test(handle, (snd_pcm_format_t) nFormat);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_test(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    set
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_set
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_set(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_format_mask_set(handle, (snd_pcm_format_t) nFormat);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_set(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask
 * Method:    reset
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_reset
(JNIEnv *env, jobject obj, jint nFormat)
{
	snd_pcm_format_mask_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_reset(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_format_mask_reset(handle, (snd_pcm_format_t) nFormat);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_00024FormatMask_reset(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_FormatMask.cc ***/

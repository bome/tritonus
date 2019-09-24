/*
 *	org_tritonus_lowlevel_alsa_AlsaCtlCardInfo.c
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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
#include "org_tritonus_lowlevel_alsa_AlsaCtlCardInfo.h"


HandleFieldHandler(snd_ctl_card_info_t*)



snd_ctl_card_info_t*
getAlsaCtlCardInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return getHandle(env, obj);
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_malloc(): begin\n"); }
	nReturn = snd_ctl_card_info_malloc(&handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_malloc(): end\n"); }
	return nReturn;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_free
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_free(): begin\n"); }
	handle = getHandle(env, obj);
	snd_ctl_card_info_free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getCard
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	int			nReturn;

	handle = getHandle(env, obj);
	nReturn = snd_ctl_card_info_get_card(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getId
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getId
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_id(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_id() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getDriver
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getDriver
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_driver(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_driver() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getName
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_name(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_name() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getLongname
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getLongname
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_longname(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_longname() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getMixername
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getMixername
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_mixername(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_mixername() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtlCardInfo
 * Method:    getComponents
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtlCardInfo_getComponents
(JNIEnv* env, jobject obj)
{
	snd_ctl_card_info_t*	handle;
	jstring			strResult;
	const char*		result;

	handle = getHandle(env, obj);
	result = snd_ctl_card_info_get_components(handle);
	if (result == NULL)
	{
		throwRuntimeException(env, "snd_card_get_components() failed");
	}
	strResult = (*env)->NewStringUTF(env, result);
	if (strResult == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strResult;
}


/*** org_tritonus_lowlevel_alsa_AlsaCtlCardInfo.c ***/

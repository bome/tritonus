/*
 *	org_tritonus_lowlevel_alsa_AlsaCtl.cc
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
#include	"org_tritonus_lowlevel_alsa_AlsaCtl.h"
#include	"HandleFieldHandler.hh"


static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_ctl_t*>	handler;




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_close
(JNIEnv *env, jobject obj)
{
	snd_ctl_t*	handle = NULL;
	int		nResult;
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtl_close(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nResult = snd_ctl_close(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaCtl_close(): end\n"); }
	return nResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getCardIndex
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getCardIndex
(JNIEnv *env, jclass cls, jstring strCardName)
{
	int	nCard;
	const char*	name = NULL;
	name = env->GetStringUTFChars(strCardName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot get characters from string argument");
	}
	nCard = snd_card_get_index(name);
	env->ReleaseStringUTFChars(strCardName, name);
	return nCard;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getCardLongName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getCardLongName
(JNIEnv *env, jclass cls, jint nCard)
{
	int	nReturn;
	jstring	strName;
	char*	name;
	nReturn = snd_card_get_longname(nCard, &name);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_card_get_longname() failed");
	}
	strName = env->NewStringUTF(name);
	if (strName == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strName;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getCardName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getCardName
(JNIEnv *env, jclass cls, jint nCard)
{
	int	nReturn;
	jstring	strName;
	char*	name;
	nReturn = snd_card_get_name(nCard, &name);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_card_get_name() failed");
	}
	strName = env->NewStringUTF(name);
	if (strName == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strName;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getCards
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getCards
(JNIEnv *env, jclass cls)
{
	int		anCards[32];
	int		nCard = -1;
	int		nCardCount = 0;
	int		nError;
	jintArray	cardsArray;

	nError = snd_card_next(&nCard);
	while (nCard >= 0 && nError >= 0)
	{
		anCards[nCardCount] = nCard;
		nCardCount++;
		nError = snd_card_next(&nCard);
	}
	cardsArray = env->NewIntArray(nCardCount);
	if (cardsArray == NULL)
	{
		throwRuntimeException(env, "cannot allocate int array");
	}
	env->SetIntArrayRegion(cardsArray, 0, nCardCount, (jint*) anCards);
	return cardsArray;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultCard
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultCard */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_card(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultMixerCard
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultMixerCard */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_mixer_card(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultPcmCard
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultPcmCard */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_pcm_card(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultPcmDevice
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultPcmDevice */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_pcm_device(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultRawmidiCard
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultRawmidiCard */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_rawmidi_card(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultRawmidiDevice
 * Signature: ()I
 */
// no longer present in the ALSA API
/* JNIEXPORT jint JNICALL */
/* Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultRawmidiDevice */
/* (JNIEnv *env, jclass cls) */
/* { */
/* 	return snd_defaults_rawmidi_device(); */
/* } */



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getCardInfo
 * Signature: ([I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getCardInfo
(JNIEnv *env, jobject obj, jintArray anValues, jobjectArray astrValues)
{
	snd_ctl_t*	handle;
	int		nReturn;
	jint*		pnValues;
	snd_ctl_card_info_t*	card_info;

	handle = handler.getHandle(env, obj);
	snd_ctl_card_info_alloca(&card_info);
	nReturn = snd_ctl_card_info(handle, card_info);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_ctl_card_info failed");
	}
	// printf("4a\n");
	checkArrayLength(env, anValues, 2);
	// printf("4b\n");
	pnValues = env->GetIntArrayElements(anValues, NULL);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	// printf("4c\n");
	pnValues[0] = snd_ctl_card_info_get_card(card_info);
	pnValues[1] = -1;	// snd_ctl_card_info_get_type(card_info);
	// printf("4d\n");
	env->ReleaseIntArrayElements(anValues, pnValues, 0);

	// printf("4e\n");
	checkArrayLength(env, astrValues, 6);
	// printf("4f\n");
	setStringArrayElement(env, astrValues, 0, snd_ctl_card_info_get_id(card_info));
	setStringArrayElement(env, astrValues, 1, snd_ctl_card_info_get_driver(card_info));
	setStringArrayElement(env, astrValues, 2, snd_ctl_card_info_get_name(card_info));
	setStringArrayElement(env, astrValues, 3, snd_ctl_card_info_get_longname(card_info));
	setStringArrayElement(env, astrValues, 4, snd_ctl_card_info_get_mixername(card_info));
	setStringArrayElement(env, astrValues, 5, snd_ctl_card_info_get_components(card_info));
	// TODO: does this make sense?
	return 0;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    loadCard
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_loadCard
(JNIEnv *env, jclass cls, jint nCard)
{
	return snd_card_load(nCard);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    open
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_open
(JNIEnv *env, jobject obj, jstring strName, jint nMode)
{
	snd_ctl_t*	handle;
	int		nResult;
	const char*	name;

	// printf("1");
	name = env->GetStringUTFChars(strName, NULL);
	// printf("2");
	if (name == NULL)
	{
		// printf("3");
		throwRuntimeException(env, "cannot get characters from string argument");
	}
	// printf("4");
	nResult = snd_ctl_open(&handle, name, nMode);
	// printf("5");
	env->ReleaseStringUTFChars(strName, name);
	// printf("6");
	if (nResult >= 0)
	{
		// printf("7");
		handler.setHandle(env, obj, handle);
	}
	return nResult;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getPcmDevices
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getPcmDevices
(JNIEnv *env, jobject obj)
{
	snd_ctl_t*	handle;
	int		anDevices[128];
	int		nDevice = -1;
	int		nDeviceCount = 0;
	int		nError;
	jintArray	devicesArray;

	handle = handler.getHandle(env, obj);
	nError = snd_ctl_pcm_next_device(handle, &nDevice);
	while (nDevice >= 0 && nError >= 0)
	{
		anDevices[nDeviceCount] = nDevice;
		nDeviceCount++;
		nError = snd_ctl_pcm_next_device(handle, &nDevice);
	}
	devicesArray = env->NewIntArray(nDeviceCount);
	if (devicesArray == NULL)
	{
		throwRuntimeException(env, "cannot allocate int array");
	}
	env->SetIntArrayRegion(devicesArray, 0, nDeviceCount, (jint*) anDevices);
	return devicesArray;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getPcmInfo
 * Signature: ([I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getPcmInfo
(JNIEnv *env, jobject obj, jintArray anValues, jobjectArray astrValues)
{
	// TODO:
	return -1;
}


/*** org_tritonus_lowlevel_alsa_AlsaCtl.cc ***/

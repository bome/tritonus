/*
 *	org_tritonus_lowlevel_alsa_AlsaCtl.c
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaCtl.h"



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_close
(JNIEnv *env, jobject obj)
{
	// TODO: implement
	return -1;
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
	name = (*env)->GetStringUTFChars(env, strCardName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot get characters from string argument");
	}
	nCard = snd_card_get_index(name);
	(*env)->ReleaseStringUTFChars(env, strCardName, name);
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
	strName = (*env)->NewStringUTF(env, name);
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
	strName = (*env)->NewStringUTF(env, name);
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
	cardsArray = (*env)->NewIntArray(env, nCardCount);
	if (cardsArray == NULL)
	{
		throwRuntimeException(env, "cannot allocate int array");
	}
	(*env)->SetIntArrayRegion(env, cardsArray, 0, nCardCount, (jint*) anCards);
	return cardsArray;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_card();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultMixerCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultMixerCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_mixer_card();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultPcmCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultPcmCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_pcm_card();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultPcmDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultPcmDevice
(JNIEnv *env, jclass cls)
{
	return snd_defaults_pcm_device();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultRawmidiCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultRawmidiCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_rawmidi_card();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getDefaultRawmidiDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getDefaultRawmidiDevice
(JNIEnv *env, jclass cls)
{
	return snd_defaults_rawmidi_device();
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaCtl
 * Method:    getHWInfo
 * Signature: ([ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_getHWInfo
(JNIEnv *env, jobject obj, jintArray anValues, jstring astrValues)
{
	// TODO: implement
	return -1;
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
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaCtl_open
(JNIEnv *env, jobject obj, jint nXXX)
{
	// TODO: implement
	return -1;
}



/*** org_tritonus_lowlevel_alsa_AlsaCtl.c ***/

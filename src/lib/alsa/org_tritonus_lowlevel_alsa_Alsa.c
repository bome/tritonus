/*
 *	org_tritonus_lowlevel_alsa_Alsa.c
 */

#include	<sys/asoundlib.h>
#include	"org_tritonus_lowlevel_alsa_Alsa.h"

static void
throwRuntimeException(JNIEnv *env, char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
		if (runtimeExceptionClass == NULL)
		{
			(*env)->FatalError(env, "cannot get class object for java.lang.RuntimeException");
		}
	}
	(*env)->ThrowNew(env, runtimeExceptionClass, pStrMessage);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getCardByName
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getCardByName
(JNIEnv *env, jclass cls, jstring strCardName)
{
	int	nCard;
	const char*	name = NULL;
	name = (*env)->GetStringUTFChars(env, strCardName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot get characters from string argument");
	}
	nCard = snd_card_name(name);
	(*env)->ReleaseStringUTFChars(env, strCardName, name);
	return nCard;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getCardLongName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getCardLongName
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
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getCardName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getCardName
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
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getCards
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getCards
(JNIEnv *env, jclass cls)
{
	return snd_cards();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getCardsMask
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getCardsMask
(JNIEnv *env, jclass cls)
{
	return snd_cards_mask();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_card();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultMixerCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultMixerCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_mixer_card();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultMixerDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultMixerDevice
(JNIEnv *env, jclass cls)
{
	return snd_defaults_mixer_device();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultPcmCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultPcmCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_pcm_card();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultPcmDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultPcmDevice
(JNIEnv *env, jclass cls)
{
	return snd_defaults_pcm_device();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultRawmidiCard
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultRawmidiCard
(JNIEnv *env, jclass cls)
{
	return snd_defaults_rawmidi_card();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getDefaultRawmidiDevice
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getDefaultRawmidiDevice
(JNIEnv *env, jclass cls)
{
	return snd_defaults_rawmidi_device();
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getStringError
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getStringError
(JNIEnv *env, jclass cls, jint nErrnum)
{
	// int	nReturn;
	jstring	strError;
	const char*	err;
	err = snd_strerror(nErrnum);
	if (err == NULL)
	{
		throwRuntimeException(env, "snd_strerror() failed");
	}
	strError = (*env)->NewStringUTF(env, err);
	if (strError == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	return strError;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    loadCard
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_loadCard
(JNIEnv *env, jclass cls, jint nCard)
{
	return snd_card_load(nCard);
}



/*** org_tritonus_lowlevel_alsa_Alsa.c ***/

/*
 *	org_tritonus_lowlevel_alsa_Alsa.c
 */

#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_Alsa.h"



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



/*** org_tritonus_lowlevel_alsa_Alsa.c ***/

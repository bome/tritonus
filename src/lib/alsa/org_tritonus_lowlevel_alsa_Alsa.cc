/*
 *	org_tritonus_lowlevel_alsa_Alsa.c
 */

#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_Alsa.h"


static int	DEBUG = 0;
static FILE*	debug_file = NULL;


/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getStringError
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getStringError
(JNIEnv *env, jclass cls, jint nErrnum)
{
	jstring	strError;
	const char*	err;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_Alsa_getStringError(): begin\n"); }
	err = snd_strerror(nErrnum);
	if (err == NULL)
	{
		throwRuntimeException(env, "snd_strerror() failed");
	}
	strError = env->NewStringUTF(err);
	if (strError == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_Alsa_getStringError(): end\n"); }
	return strError;
}



/*** org_tritonus_lowlevel_alsa_Alsa.c ***/

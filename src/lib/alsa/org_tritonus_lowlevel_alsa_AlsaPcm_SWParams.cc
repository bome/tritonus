/*
 *	org_tritonus_lowlevel_alsa_AlsaPcm_SWParams.c
 */


#include	<sys/asoundlib.h>
#include	<errno.h>
#include	"org_tritonus_lowlevel_alsa_AlsaPcm_SWParams.h"
#include	"common.h"
#include	"HandleFieldHandler.hh"

// hacky
static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_pcm_sw_params_t*>	handler;




snd_pcm_sw_params_t*
getSWParamsNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_malloc
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_SWParams_malloc(): begin\n"); }
	nReturn = snd_pcm_sw_params_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_SWParams_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_SWParams_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_free
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_SWParams_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_sw_params_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_SWParams_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getStartMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getStartMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_start_mode(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getXrunMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getXrunMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_xrun_mode(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getTStampMode
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getTStampMode
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_tstamp_mode(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getSleepMin
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getSleepMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_sleep_min(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getAvailMin
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getAvailMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_avail_min(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getXferAlign
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getXferAlign
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_xfer_align(handle);
	return nReturn;
}




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getStartThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getStartThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_start_threshold(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getStopThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getStopThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_stop_threshold(handle);
	return nReturn;
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getSilenceThreshold
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getSilenceThreshold
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_silence_threshold(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_SWParams
 * Method:    getSilenceSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024SWParams_getSilenceSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_sw_params_t*	handle;
	int			nReturn;

	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_sw_params_get_silence_size(handle);
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaPcm_SWParams.c ***/

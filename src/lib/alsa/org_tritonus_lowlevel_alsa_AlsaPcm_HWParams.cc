/*
 *	org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.c
 */


#include	<sys/asoundlib.h>
#include	<errno.h>
#include	"org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.h"
#include	"common.h"
#include	"HandleFieldHandler.hh"


// hacky
static int	DEBUG = 0;
static FILE*	debug_file = NULL;


static HandleFieldHandler<snd_pcm_hw_params_t*>	handler;



/*
 */
static void
setDirection(JNIEnv* env, jint nDirection, jintArray anDirection)
{
	if (anDirection != NULL)
	{
		checkArrayLength(env, anDirection, 1);
		env->SetIntArrayRegion(anDirection, 0, 1, &nDirection);
	}
}




snd_pcm_hw_params_t*
getHWParamsNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_malloc
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_malloc(): begin\n"); }
	nReturn = snd_pcm_hw_params_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_free
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_pcm_hw_params_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getRate
 * Signature: ([J)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getRate___3J
(JNIEnv *env, jobject obj, jlongArray alValues)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	unsigned int		nNumerator;
	unsigned int		nDenominator;
	jlong			values[2];

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRate(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_rate_numden(handle, &nNumerator, &nDenominator);
	checkArrayLength(env, alValues, 2);
	values[0] = nNumerator;
	values[1] = nDenominator;
	env->SetLongArrayRegion(alValues, 0, 2, values);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRate(): end\n"); }
	return nReturn;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getSBits
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getSBits
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getSBits(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_sbits(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getSBits(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getFifoSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getFifoSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getFifoSize(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_fifo_size(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getFifoSize(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getAccess
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getAccess
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getAccess(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_access(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getAccess(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getFormat
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getFormat
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getFormat(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_format(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getFormat(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getSubformat
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getSubformat
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getSubformat(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_subformat(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getSubformat(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getChannels
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannels(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_channels(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getChannelsMin
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getChannelsMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannelsMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_channels_min(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannelsMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getChannelsMax
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getChannelsMax
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannelsMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_channels_max(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getChannelsMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getRate
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getRate___3I
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRate(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_rate(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRate(): end\n"); }
	return nReturn;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getRateMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getRateMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRateMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_rate_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRateMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getRateMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getRateMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRateMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_rate_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getRateMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodTime
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodTime
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_time(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTime(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodTimeMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodTimeMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTimeMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_time_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTimeMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodTimeMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodTimeMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTimeMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_time_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodTimeMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodSize
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodSize
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSize(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_size(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSize(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodSizeMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodSizeMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSizeMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_size_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSizeMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodSizeMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodSizeMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSizeMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_size_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodSizeMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriods
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriods
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriods(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_periods(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriods(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodsMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodsMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodsMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_periods_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodsMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodsMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodsMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodsMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_periods_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getPeriodsMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferTime
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferTime
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_time(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTime(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferTimeMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferTimeMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTimeMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_time_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTimeMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferTimeMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferTimeMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTimeMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_time_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferTimeMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferSize
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSize(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_size(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSize(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferSizeMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferSizeMin
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSizeMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_size_min(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSizeMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferSizeMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferSizeMax
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSizeMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_size_max(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getBufferSizeMax(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getTickTime
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getTickTime
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTime(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_tick_time(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTime(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getTickTimeMin
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getTickTimeMin
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTimeMin(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_tick_time_min(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTimeMin(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getTickTimeMax
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getTickTimeMax
(JNIEnv *env, jobject obj, jintArray anDirection)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;
	int			nDirection;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTimeMax(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_tick_time_max(handle, &nDirection);
	setDirection(env, nDirection, anDirection);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaPcm_HWParams_getTickTimeMax(): end\n"); }
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.c ***/

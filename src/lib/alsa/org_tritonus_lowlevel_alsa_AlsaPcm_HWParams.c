/*
 *	org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.c
 */


#include	<sys/asoundlib.h>
#include	<errno.h>
#include	"org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.h"
#include	"common.h"



static jfieldID
getNativeSeqFieldID(JNIEnv *env)
{
	static jfieldID	nativeHandleFieldID = NULL;

	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/alsa/AlsaPcm");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.alsa.ASequencer0");
		}
		nativeHandleFieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static snd_pcm_hw_params_t*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	return (snd_pcm_hw_params_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, snd_pcm_hw_params_t* handle)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



snd_pcm_hw_params_t*
getHWParamsNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	return (snd_pcm_hw_params_t*) (*env)->GetLongField(env, obj, fieldID);
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

	nReturn = snd_pcm_hw_params_malloc(&handle);
	setNativeHandle(env, obj, handle);
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

	handle = getNativeHandle(env, obj);
	snd_pcm_hw_params_free(handle);
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getPeriodSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getPeriodSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	handle = getNativeHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_period_size(handle, NULL);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm_HWParams
 * Method:    getBufferSize
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_00024HWParams_getBufferSize
(JNIEnv *env, jobject obj)
{
	snd_pcm_hw_params_t*	handle;
	int			nReturn;

	handle = getNativeHandle(env, obj);
	nReturn = snd_pcm_hw_params_get_buffer_size(handle);
	return nReturn;
}


/*** org_tritonus_lowlevel_alsa_AlsaPcm_HWParams.c ***/

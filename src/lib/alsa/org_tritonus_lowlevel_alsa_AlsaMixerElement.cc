/*
 *	org_tritonus_lowlevel_alsa_AlsaMixerElement.c
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaMixerElement.h"
#include	"HandleFieldHandler.hh"


static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_mixer_elem_t*>	handler;


snd_mixer_t*
getMixerNativeHandle(JNIEnv *env, jobject obj);

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    open
 * Signature: (Lorg/tritonus/lowlevel/alsa/AlsaMixer;ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_open
(JNIEnv *env, jobject obj, jobject mixer, jint nIndex, jstring strName)
{
	snd_mixer_elem_t*	handle;
	snd_mixer_t*		mixerHandle;
	snd_mixer_selem_id_t*	id;
	int			nReturn;
	const char*		name;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_open(): begin\n"); }
	mixerHandle = getMixerNativeHandle(env, mixer);
	snd_mixer_selem_id_alloca(&id);
	snd_mixer_selem_id_set_index(id, nIndex);

	name= env->GetStringUTFChars(strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "GetStringUTFChars() failed");
		return -1;
	}
	snd_mixer_selem_id_set_name(id, name);
	env->ReleaseStringUTFChars(strName, name);
	handle = snd_mixer_find_selem(mixerHandle, id);
	if (handle == NULL)
	{
		nReturn = -1;
	}
	else
	{
		handler.setHandle(env, obj, handle);
		nReturn = 0;
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_open(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getName
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	jstring			name;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	name = env->NewStringUTF(snd_mixer_selem_get_name(handle));
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getName(): end\n"); }
	return name;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getIndex
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getIndex
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getIndex(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_index(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getIndex(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    isPlaybackMono
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isPlaybackMono
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isPlaybackMono(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_is_playback_mono(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isPlaybackMono(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasPlaybackChannel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackChannel
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackChannel(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_playback_channel(handle, (snd_mixer_selem_channel_id_t) nChannelType);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackChannel(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    isCaptureMono
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isCaptureMono
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isCaptureMono(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_is_capture_mono(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_isCaptureMono(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureChannel
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureChannel
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureChannel(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_channel(handle, (snd_mixer_selem_channel_id_t) nChannelType);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureChannel(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getCaptureGroup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureGroup
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureGroup(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_capture_group(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureGroup(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCommonVolume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonVolume
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_common_volume(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonVolume(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasPlaybackVolume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolume
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_playback_volume(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolume(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasPlaybackVolumeJoined
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolumeJoined
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolumeJoined(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_playback_volume_joined(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackVolumeJoined(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureVolume
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolume
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_volume(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolume(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureVolumeJoined
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolumeJoined
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolumeJoined(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_volume_joined(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureVolumeJoined(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCommonSwitch
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonSwitch
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_common_switch(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCommonSwitch(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasPlaybackSwitch
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitch
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_playback_switch(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitch(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasPlaybackSwitchJoined
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitchJoined
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitchJoined(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_playback_switch_joined(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasPlaybackSwitchJoined(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureSwitch
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitch
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_switch(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitch(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureSwitchJoinded
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchJoinded
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchJoinded(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_switch_joined(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchJoinded(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    hasCaptureSwitchExclusive
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchExclusive
(JNIEnv* env, jobject obj)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchExclusive(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_has_capture_switch_exclusive(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_hasCaptureSwitchExclusive(): end\n"); }
	return (jboolean) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getPlaybackVolume
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolume
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;
	long			lValue;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_playback_volume(handle, (snd_mixer_selem_channel_id_t) nChannelType, &lValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolume(): end\n"); }
	return lValue;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getCaptureVolume
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolume
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;
	long			lValue;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_capture_volume(handle, (snd_mixer_selem_channel_id_t) nChannelType, &lValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolume(): end\n"); }
	return lValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getPlaybackSwitch
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackSwitch
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;
	int			nValue;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_playback_switch(handle, (snd_mixer_selem_channel_id_t) nChannelType, &nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackSwitch(): end\n"); }
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getCaptureSwitch
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureSwitch
(JNIEnv* env, jobject obj, jint nChannelType)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;
	int			nValue;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_get_capture_switch(handle, (snd_mixer_selem_channel_id_t) nChannelType, &nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureSwitch(): end\n"); }
	return nValue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setPlaybackVolume
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolume
(JNIEnv* env, jobject obj, jint nChannelType, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_playback_volume(handle, (snd_mixer_selem_channel_id_t) nChannelType, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolume(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setCaptureVolume
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolume
(JNIEnv* env, jobject obj, jint nChannelType, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolume(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_capture_volume(handle, (snd_mixer_selem_channel_id_t) nChannelType, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolume(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setPlaybackVolumeAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeAll
(JNIEnv* env, jobject obj, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeAll(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_playback_volume_all(handle, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeAll(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setCaptureVolumeAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeAll
(JNIEnv* env, jobject obj, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeAll(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_capture_volume_all(handle, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeAll(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setPlaybackSwitch
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitch
(JNIEnv* env, jobject obj, jint nChannelType, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_playback_switch(handle, (snd_mixer_selem_channel_id_t) nChannelType, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitch(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setCaptureSwitch
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitch
(JNIEnv* env, jobject obj, jint nChannelType, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitch(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_capture_switch(handle, (snd_mixer_selem_channel_id_t) nChannelType, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitch(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setPlaybackSwitchAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitchAll
(JNIEnv* env, jobject obj, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitchAll(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_playback_switch_all(handle, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackSwitchAll(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setCaptureSwitchAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitchAll
(JNIEnv* env, jobject obj, jint nValue)
{
	snd_mixer_elem_t*	handle;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitchAll(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_set_capture_switch_all(handle, nValue);
	if (nReturn < 0)
	{
		throwRuntimeException(env, snd_strerror(nReturn));
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureSwitchAll(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getPlaybackVolumeRange
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolumeRange
(JNIEnv* env, jobject obj, jintArray anValues)
{
	snd_mixer_elem_t*	handle;
	jint			values[2];

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolumeRange(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_mixer_selem_get_playback_volume_range(handle, values, values + 1);
	checkArrayLength(env, anValues, 2);
	env->SetIntArrayRegion(anValues, 0, 2, values);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getPlaybackVolumeRange(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getCaptureVolumeRange
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolumeRange
(JNIEnv* env, jobject obj, jintArray anValues)
{
	snd_mixer_elem_t*	handle;
	jint			values[2];

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolumeRange(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_mixer_selem_get_capture_volume_range(handle, values, values + 1);
	checkArrayLength(env, anValues, 2);
	env->SetIntArrayRegion(anValues, 0, 2, values);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getCaptureVolumeRange(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setPlaybackVolumeRange
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeRange
(JNIEnv* env, jobject obj, jint nMin, jint nMax)
{
	snd_mixer_elem_t*	handle;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeRange(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_mixer_selem_set_playback_volume_range(handle, nMin, nMax);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setPlaybackVolumeRange(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setCaptureVolumeRange
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeRange
(JNIEnv* env, jobject obj, jint nMin, jint nMax)
{
	snd_mixer_elem_t*	handle;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeRange(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_mixer_selem_set_capture_volume_range(handle, nMin, nMax);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setCaptureVolumeRange(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    getChannelName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getChannelName
(JNIEnv* env, jclass cls, jint nChannelType)
{
	jstring			channelName;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getChannelName(): begin\n"); }
	channelName = env->NewStringUTF(snd_mixer_selem_channel_name((snd_mixer_selem_channel_id_t) nChannelType));
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_getChannelName(): end\n"); }
	return channelName;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixerElement
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixerElement_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	DEBUG = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaMixerElement.c ***/

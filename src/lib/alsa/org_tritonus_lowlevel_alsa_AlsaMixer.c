/*
 *	org_tritonus_lowlevel_alsa_AlsaMixer.c
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaMixer.h"




static jfieldID
getNativeHandleFieldID(JNIEnv *env)
{
	static jfieldID		nativeHandleFieldID = NULL;

	// printf("hallo\n");
	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/alsa/AlsaMixer");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.alsa.AlsaMixer");
		}
		nativeHandleFieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static snd_mixer_t*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	return (snd_mixer_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, snd_mixer_t* handle)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    open
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_open
(JNIEnv *env, jobject obj, jstring strMixerName)
{
	snd_mixer_t*	handle;
	int		nReturn;
	const char*		mixerName;

	mixerName = (*env)->GetStringUTFChars(env, strMixerName, NULL);
	if (mixerName == NULL)
	{
		throwRuntimeException(env, "cannot retrieve chars from mixer name string");
		return -1;
	}
	nReturn = snd_mixer_open(&handle, (char*) mixerName);
	(*env)->ReleaseStringUTFChars(env, strMixerName, mixerName);
	setNativeHandle(env, obj, handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_close
(JNIEnv *env, jobject obj)
{
	snd_mixer_t*	handle = getNativeHandle(env, obj);
	return snd_mixer_close(handle);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    readControlList
 * Signature: ([I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_readControlList
(JNIEnv *env, jobject obj, jintArray anIndices, jobjectArray astrNames)
{
	snd_mixer_t*	handle = getNativeHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    readControl
 * Signature: (ILjava/lang/String;[I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_readControl
(JNIEnv *env, jobject obj, jint nIndex, jstring strName, jintArray anValues)
{
	snd_mixer_t*	handle = getNativeHandle(env, obj);
	snd_mixer_simple_control_t	control;
	const char*	name;
	int		nReturn;

	int		nLength;
	jboolean	bIsCopy;
	jint*		pnValues;

	control.sid.index = nIndex;
	name = (*env)->GetStringUTFChars(env, strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot retrieve chars from mixer name string");
		return -1;
	}
	(void) strncpy(control.sid.name, name, 59);
	(*env)->ReleaseStringUTFChars(env, strName, name);
	nReturn = snd_mixer_simple_control_read(handle, &control);

	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 12)
	{
		throwRuntimeException(env, "array does not have enough elements (12 required)");
	}
	pnValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopy);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	pnValues[0] = control.caps;
	pnValues[1] = control.channels;
	pnValues[2] = control.mute;
	pnValues[3] = control.capture;
	pnValues[4] = control.min;
	pnValues[5] = control.max;
	pnValues[6] = control.volume.names.front_left;
	pnValues[7] = control.volume.names.front_right;
	pnValues[8] = control.volume.names.front_center;
	pnValues[9] = control.volume.names.rear_left;
	pnValues[10] = control.volume.names.rear_right;
	pnValues[11] = control.volume.names.woofer;
	if (bIsCopy)
	{
		(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);
	}
	return nReturn;
}




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    writeControl
 * Signature: (ILjava/lang/String;[I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_writeControl
(JNIEnv *env, jobject obj, jint nIndex, jstring strName, jintArray anValues)
{
	snd_mixer_t*	handle = getNativeHandle(env, obj);
	snd_mixer_simple_control_t	control;
	const char*	name;
	int		nReturn;

	int		nLength;
	jboolean	bIsCopy;
	jint*		pnValues;

	control.sid.index = nIndex;
	name = (*env)->GetStringUTFChars(env, strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot retrieve chars from mixer name string");
		return -1;
	}
	(void) strncpy(control.sid.name, name, 59);
	(*env)->ReleaseStringUTFChars(env, strName, name);

	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 8)
	{
		throwRuntimeException(env, "array does not have enough elements (8 required)");
	}
	pnValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopy);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	control.mute = pnValues[0];
	control.capture = pnValues[1];
	control.volume.names.front_left = pnValues[2];
	control.volume.names.front_right = pnValues[3];
	control.volume.names.front_center = pnValues[4];
	control.volume.names.rear_left = pnValues[5];
	control.volume.names.rear_right = pnValues[6];
	control.volume.names.woofer = pnValues[7];
	if (bIsCopy)
	{
		(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);
	}
	nReturn = snd_mixer_simple_control_read(handle, &control);
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaMixer.c ***/

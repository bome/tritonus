/*
 *	org_tritonus_lowlevel_alsa_AlsaPcm.c
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


#include	<sys/asoundlib.h>
#include	<errno.h>
#include	"org_tritonus_lowlevel_alsa_AlsaPcm.h"


static int	DEBUG = 1;


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



static snd_pcm_t*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	return (snd_pcm_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, snd_pcm_t* handle)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_close
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_close(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    drainPlayback
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_drainPlayback
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_playback_drain(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    flushCapture
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_flushCapture
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_capture_flush(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    flushChannel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_flushChannel
(JNIEnv *env, jobject obj, jint nChannel)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_channel_flush(handle, nChannel);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    flushPlayback
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_flushPlayback
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_playback_flush(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    getChannelInfo
 * Signature: ([I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_getChannelInfo
(JNIEnv *env, jobject obj, jintArray anValues, jobjectArray astrValues)
{
	jint*	values = NULL;
	jstring	strValue;
	int	nLength;
	snd_pcm_channel_info_t	channelInfo;
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn;
	memset(&channelInfo, 0, sizeof(channelInfo));
	nReturn = snd_pcm_channel_info(handle, &channelInfo);
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 18)
	{
		throwRuntimeException(env, "integer array does not have enough elements (18 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = channelInfo.subdevice;
	values[1] = channelInfo.channel;
	values[2] = channelInfo.mode;
	values[3] = channelInfo.flags;
	values[4] = channelInfo.formats;
	values[5] = channelInfo.rates;
	values[6] = channelInfo.min_rate;
	values[7] = channelInfo.max_rate;
	values[8] = channelInfo.min_voices;
	values[9] = channelInfo.max_voices;
	values[10] = channelInfo.buffer_size;
	values[11] = channelInfo.min_fragment_size;
	values[12] = channelInfo.max_fragment_size;
	values[13] = channelInfo.fragment_align;
	values[14] = channelInfo.fifo_size;
	values[15] = channelInfo.transfer_block_size;
	values[16] = channelInfo.mmap_size;
	values[17] = channelInfo.mixer_device;
/*
	printf("mixer device: %d\n", channelInfo.mixer_device);
	printf("mixer eid name: %s\n", channelInfo.mixer_eid.name);
	printf("mixer eid index: %d\n", channelInfo.mixer_eid.index);
	printf("mixer eid type: %d\n", channelInfo.mixer_eid.type);
*/
	// values[18] = channelInfo.mixer_eid;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);

	nLength = (*env)->GetArrayLength(env, astrValues);
	if (nLength < 1)
	{
		throwRuntimeException(env, "string array does not have enough elements (1 required)");
	}
	strValue = (*env)->NewStringUTF(env, channelInfo.subname);
	if (strValue == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	(*env)->SetObjectArrayElement(env, astrValues, 0, strValue);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    getChannelSetup
 * Signature: ([I[Z)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_getChannelSetup
(JNIEnv *env, jobject obj, jintArray anValues, jbooleanArray abValues)
{
	jint*		values = NULL;
	jboolean*	bvalues = NULL;
	int	nLength;
	snd_pcm_channel_setup_t	channelSetup;
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn;
	memset(&channelSetup, 0, sizeof(channelSetup));
	nReturn = snd_pcm_channel_setup(handle, &channelSetup);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_pcm_channel_setup failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 12)
	{
		throwRuntimeException(env, "array does not have enough elements (12 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = channelSetup.channel;
	values[1] = channelSetup.mode;
	values[2] = channelSetup.format.format;
	values[3] = channelSetup.format.rate;
	values[4] = channelSetup.format.voices;
	values[5] = channelSetup.format.special;
	values[6] = channelSetup.buf.stream.queue_size;
	values[7] = channelSetup.buf.block.frag_size;
	values[8] = channelSetup.buf.block.frags;
	values[9] = channelSetup.buf.block.frags_min;
	values[10] = channelSetup.buf.block.frags_max;
	values[11] = channelSetup.msbits_per_sample;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);

	nLength = (*env)->GetArrayLength(env, abValues);
	if (nLength < 1)
	{
		throwRuntimeException(env, "array does not have enough elements (1 required)");
	}
	bvalues = (*env)->GetBooleanArrayElements(env, abValues, NULL);
	if (bvalues == NULL)
	{
		throwRuntimeException(env, "GetBooleanArrayElements failed");
	}
	bvalues[0] = channelSetup.format.interleave;
	(*env)->ReleaseBooleanArrayElements(env, abValues, bvalues, 0);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    getChannelStatus
 * Signature: (I[I[J)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_getChannelStatus
(JNIEnv *env, jobject obj, jint nChannel, jintArray anValues, jlongArray alValues)
{
	jint*	values = NULL;
	jlong*	lvalues = NULL;
	int	nLength;
	snd_pcm_channel_status_t	channelStatus;
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn;
	memset(&channelStatus, 0, sizeof(channelStatus));
	channelStatus.channel = nChannel;
	printf("getChannelStatus(): handle: %p\n", handle);
	nReturn = snd_pcm_channel_status(handle, &channelStatus);
	if (nReturn < 0)
	{
		return nReturn;
		// throwRuntimeException(env, "snd_pcm_channel_status failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 9)
	{
		throwRuntimeException(env, "array does not have enough elements (9 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = channelStatus.mode;
	values[1] = channelStatus.status;
	values[2] = channelStatus.scount;
	values[3] = channelStatus.frag;
	values[4] = channelStatus.count;
	values[5] = channelStatus.free;
	values[6] = channelStatus.underrun;
	values[7] = channelStatus.overrun;
	values[8] = channelStatus.overrange;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);

	nLength = (*env)->GetArrayLength(env, alValues);
	if (nLength < 2)
	{
		throwRuntimeException(env, "array does not have enough elements (2 required)");
	}
	lvalues = (*env)->GetLongArrayElements(env, alValues, NULL);
	if (lvalues == NULL)
	{
		throwRuntimeException(env, "GetLongArrayElements failed");
	}
	lvalues[0] = (jlong) channelStatus.stime.tv_sec * 1000000 + channelStatus.stime.tv_usec;
	lvalues[1] = channelStatus.ust_stime;
	(*env)->ReleaseLongArrayElements(env, alValues, lvalues, 0);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    getInfo
 * Signature: ([ILjava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_getInfo
(JNIEnv *env, jobject obj, jintArray anValues, jobjectArray astrValues)
{
	jint*		values = NULL;
	jstring		strValue;
	int	nLength;
	snd_pcm_info_t	pcmInfo;
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn;
	memset(&pcmInfo, 0, sizeof(pcmInfo));
	nReturn = snd_pcm_info(handle, &pcmInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_pcm_info failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 4)
	{
		throwRuntimeException(env, "array does not have enough elements (4 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = pcmInfo.type;
	values[1] = pcmInfo.flags;
	values[2] = pcmInfo.playback;
	values[3] = pcmInfo.capture;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);

	nLength = (*env)->GetArrayLength(env, astrValues);
	if (nLength < 2)
	{
		throwRuntimeException(env, "array does not have enough elements (2 required)");
	}
	strValue = (*env)->NewStringUTF(env, pcmInfo.id);
	if (strValue == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	(*env)->SetObjectArrayElement(env, astrValues, 0, strValue);
	strValue = (*env)->NewStringUTF(env, pcmInfo.name);
	if (strValue == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	(*env)->SetObjectArrayElement(env, astrValues, 1, strValue);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    getTransferSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_getTransferSize
(JNIEnv *env, jobject obj, jint nChannel)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_transfer_size(handle, nChannel);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    goCapture
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_goCapture
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_capture_go(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    goChannel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_goChannel
(JNIEnv *env, jobject obj, jint nChannel)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_channel_go(handle, nChannel);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    goPlayback
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_goPlayback
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_playback_go(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    goSync
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_goSync
(JNIEnv *env, jobject obj)
{
	// TODO:
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_close(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    open
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_open__III
(JNIEnv *env, jobject obj, jint nCard, jint nDevice, jint nMode)
{
	snd_pcm_t*	handle;
	int		nReturn = snd_pcm_open(&handle, nCard, nDevice, nMode);
	printf("mode: %d\n", (int) nMode);
	printf("handle: %p\n", handle);
	if (nReturn == 0)
	{
		setNativeHandle(env, obj, handle);
	}
	else
	{
		printf("org_tritonus_lowlevel_alsa_AlsaPcm.open(): returns %d\n", nReturn);
	}
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    open
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_open__IIII
(JNIEnv *env, jobject obj, jint nCard, jint nDevice, jint nSubdevice, jint nMode)
{
	snd_pcm_t*	handle;
	int		nReturn = snd_pcm_open_subdevice(&handle, nCard, nDevice, nSubdevice, nMode);
	if (nReturn == 0)
	{
		setNativeHandle(env, obj, handle);
	}
	else
	{
		printf("org_tritonus_lowlevel_alsa_AlsaPcm.open(): returns %d\n", nReturn);
	}
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    prepareCapture
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_prepareCapture
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_capture_prepare(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    prepareChannel
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_prepareChannel
(JNIEnv *env, jobject obj, jint nChannel)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_channel_prepare(handle, nChannel);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    preparePlayback
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_preparePlayback
(JNIEnv *env, jobject obj)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_playback_prepare(handle);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_read
(JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nBytesRead;
	jbyte		buffer[nLength];
	printf("handle: %p\n", handle);
	printf("buffer: %p\n", buffer);
	printf("length: %d\n", (int) nLength);
	printf("fd: %d\n", snd_pcm_file_descriptor(handle, SND_PCM_CHANNEL_CAPTURE));
	errno = 0;
	nBytesRead = snd_pcm_read(handle, buffer, nLength);
	perror("abc");
	if (nBytesRead > 0)
	{
		(*env)->SetByteArrayRegion(env, abData, nOffset, nBytesRead, buffer);
	}
	if (DEBUG)
	{
		printf("Java_org_tritonus_lowlevel_alsa_AlsaPcm_read: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_alsa_AlsaPcm_read: Read: %d\n", nBytesRead);
	}
	return nBytesRead;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    setChannelParams
 * Signature: (IIZIIIIIIZZIII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_setChannelParams
(JNIEnv *env, jobject obj, jint nChannel, jint nMode, jboolean bInterleave, jint nFormat, jint nRate, jint nVoices, jint nSpecial, jint nStartMode, jint nStopMode, jboolean bTime, jboolean bUstTime, jint nQueueOrFragmentSize, jint nMin, jint nMax)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	snd_pcm_channel_params_t	params;
	int		nReturn;
	memset(&params, 0, sizeof(params));
	params.channel = nChannel;
	params.mode = nMode;
	params.format.interleave = bInterleave;
	params.format.format = nFormat;
	params.format.rate = nRate;
	params.format.voices = nVoices;
	params.format.special = nSpecial;
	// params.digital. ... = ...;
	params.start_mode = nStartMode;
	params.stop_mode = nStopMode;
	params.time = bTime;
	params.ust_time = bUstTime;
	if (nMode == SND_PCM_MODE_STREAM)
	{
		params.buf.stream.queue_size = nQueueOrFragmentSize;
		params.buf.stream.fill = nMin;
		params.buf.stream.max_fill = nMax;
	}
	else
	{
		params.buf.block.frag_size = nQueueOrFragmentSize;
		params.buf.block.frags_min = nMin;
		params.buf.block.frags_max = nMax;
	}
	nReturn = snd_pcm_channel_params(handle, &params);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    setNonblockMode
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_setNonblockMode
(JNIEnv *env, jobject obj, jint nMode)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_nonblock_mode(handle, nMode);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    setPlaybackPause
 * Signature: (Z)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_setPlaybackPause
(JNIEnv *env, jobject obj, jboolean bEnable)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	int		nReturn = snd_pcm_playback_pause(handle, bEnable);
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaPcm
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaPcm_write
(JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	snd_pcm_t*	handle = getNativeHandle(env, obj);
	signed char*	data = (*env)->GetByteArrayElements(env, abData, NULL);
	int		nWritten = snd_pcm_write(handle, data + nOffset, nLength);
	(*env)->ReleaseByteArrayElements(env, abData, data, JNI_ABORT);
	if (DEBUG)
	{
		printf("Java_org_tritonus_lowlevel_alsa_AlsaPcm_write: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_alsa_AlsaPcm_write: Written: %d\n", nWritten);
	}
	return nWritten;
}



/*** org_tritonus_lowlevel_alsa_AlsaPcm.c ***/

/*
 *	org_tritonus_lowlevel_alsa_ASequencer0.c
 */

#include	<errno.h>
#include	<string.h>
#include	<sys/asoundlib.h>
#include	"org_tritonus_lowlevel_alsa_ASequencer0.h"


static void
sendEvent(JNIEnv *env, snd_seq_t* seq, snd_seq_event_t* pEvent);



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
	static jfieldID	nativeSeqFieldID = NULL;

	// printf("hallo\n");
	if (nativeSeqFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/alsa/ASequencer0");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.alsa.ASequencer0");
		}
		nativeSeqFieldID = (*env)->GetFieldID(env, cls, "m_lNativeSeq", "J");
		if (nativeSeqFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeSeq of class Seq");
		}
	}
	return nativeSeqFieldID;
}



static snd_seq_t*
getNativeSeq(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	return (snd_seq_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeSeq(JNIEnv *env, jobject obj, snd_seq_t* seq)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) seq);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    allocQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_alsa_ASequencer0_allocQueue
(JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nQueue = snd_seq_alloc_queue(seq);
	if (nQueue < 0)
	{
		throwRuntimeException(env, "snd_seq_alloc_queue failed");
	}
	return (jint) nQueue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_alsa_ASequencer0_close
  (JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn = snd_seq_close(seq);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_close failed");
	}
}

/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    createPort
 * Signature: (Ljava/lang/String;IIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_alsa_ASequencer0_createPort
(JNIEnv *env, jobject obj, jstring strName, jint nCapabilities, jint nGroupPermissions, jint nType, jint nMidiChannels, jint nMidiVoices, jint nSynthVoices)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_port_info_t	portInfo;
	const char*		name;
	int		nReturn;
	memset(&portInfo, 0, sizeof(portInfo));
	name = (*env)->GetStringUTFChars(env, strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "GetStringUTFChars failed");
	}
	strncpy(portInfo.name, name, 63);
	portInfo.name[64] = 0;
	(*env)->ReleaseStringUTFChars(env, strName, name);
	// portInfo.capability = SND_SEQ_PORT_CAP_WRITE | SND_SEQ_PORT_CAP_SUBS_WRITE | SND_SEQ_PORT_CAP_READ;
	portInfo.capability = nCapabilities;
	portInfo.cap_group = nGroupPermissions;
	// portInfo.type =  SND_SEQ_PORT_TYPE_MIDI_GENERIC;
	portInfo.type =  nType;
	portInfo.midi_channels = nMidiChannels;
	portInfo.midi_voices = nMidiVoices;
	portInfo.synth_voices = nSynthVoices;
	//portInfo.write_use = 1;	// R/O attrs?
	//portInfo.read_use = 1;

	// errno = 0;
	nReturn = snd_seq_create_port(seq, &portInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_create_port failed");
	}
	return (jint) portInfo.port;
}



static void
fillClientInfoArrays(JNIEnv *env, jobject obj, snd_seq_client_info_t* clientInfo, jintArray anValues, jobjectArray astrValues)
{
	int		nLength;
	jstring		strName;
	jstring		strGroupName;
	jboolean	bIsCopy;
	jint*	pnValues;

	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 4)
	{
		throwRuntimeException(env, "array does not have enough elements (4 required)");
	}
	pnValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopy);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	pnValues[0] = clientInfo->client;
	pnValues[1] = clientInfo->type;
	pnValues[2] = clientInfo->filter;
	pnValues[3] = clientInfo->num_ports;
	if (bIsCopy)
	{
		(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);
	}
	nLength = (*env)->GetArrayLength(env, astrValues);
	if (nLength < 2)
	{
		throwRuntimeException(env, "array does not have enough elements (2 required)");
	}
	strName = (*env)->NewStringUTF(env, clientInfo->name);
	(*env)->SetObjectArrayElement(env, astrValues, 0, strName);
	strGroupName = (*env)->NewStringUTF(env, clientInfo->group);
	(*env)->SetObjectArrayElement(env, astrValues, 1, strGroupName);
}



static void
fillPortInfoArrays(JNIEnv *env, jobject obj, snd_seq_port_info_t* portInfo, jintArray anValues, jobjectArray astrValues)
{
	int		nLength;
	jstring		strName;
	jstring		strGroupName;
	jboolean	bIsCopy;
	jint*	pnValues;

	// printf("4a\n");
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 10)
	{
		throwRuntimeException(env, "array does not have enough elements (10 required)");
	}
	// printf("4b\n");
	pnValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopy);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	// printf("4c\n");
	pnValues[0] = portInfo->client;
	pnValues[1] = portInfo->port;
	pnValues[2] = portInfo->capability;
	pnValues[3] = portInfo->cap_group;
	pnValues[4] = portInfo->type;
	pnValues[5] = portInfo->midi_channels;
	pnValues[6] = portInfo->midi_voices;
	pnValues[7] = portInfo->synth_voices;
	pnValues[8] = portInfo->read_use;
	pnValues[9] = portInfo->write_use;
	// printf("4d\n");
	if (bIsCopy)
	{
		(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);
	}
	// printf("4e\n");
	nLength = (*env)->GetArrayLength(env, astrValues);
	if (nLength < 2)
	{
		throwRuntimeException(env, "array does not have enough elements (2 required)");
	}
	// printf("4f\n");
	// printf("name: %s\n", portInfo->name);
	strName = (*env)->NewStringUTF(env, portInfo->name);
	if (strName == NULL)
	{
		printf("NewString failed\n");
	}
	// printf("4g\n");
	// this causes a segment violation
	(*env)->SetObjectArrayElement(env, astrValues, 0, strName);
	// printf("4h\n");
	strGroupName = (*env)->NewStringUTF(env, portInfo->group);
	(*env)->SetObjectArrayElement(env, astrValues, 1, strGroupName);
	// printf("4i\n");
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getClientInfo
 * Signature: (I[I[Ljava/lang/String;)V
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_alsa_ASequencer0_getClientInfo
  (JNIEnv *env, jobject obj, jint nClient, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_client_info_t	clientInfo;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn;
	// int		nLength;
	// jstring		strName;
	// jstring		strGroupName;
	// jboolean	bIsCopy;

	memset(&clientInfo, 0, sizeof(clientInfo));
	nReturn = snd_seq_get_any_client_info(seq, nClient, &clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_any_client_info failed");
	}
	else
	{
		fillClientInfoArrays(env, obj, &clientInfo, anValues, astrValues);
/*
		jint*	pnValues;
		nLength = (*env)->GetArrayLength(env, anValues);
		if (nLength < 4)
		{
			throwRuntimeException(env, "array does not have enough elements (4 required)");
		}
		pnValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopy);
		if (pnValues == NULL)
		{
			throwRuntimeException(env, "GetIntArrayElements failed");
		}
		pnValues[0] = clientInfo.client;
		pnValues[1] = clientInfo.type;
		pnValues[2] = clientInfo.filter;
		pnValues[3] = clientInfo.num_ports;
		if (bIsCopy)
		{
			(*env)->ReleaseIntArrayElements(env, anValues, pnValues, 0);
		}
		nLength = (*env)->GetArrayLength(env, astrValues);
		if (nLength < 2)
		{
			throwRuntimeException(env, "array does not have enough elements (2 required)");
		}
		strName = (*env)->NewStringUTF(env, clientInfo.name);
		(*env)->SetObjectArrayElement(env, astrValues, 0, strName);
		strGroupName = (*env)->NewStringUTF(env, clientInfo.group);
		(*env)->SetObjectArrayElement(env, astrValues, 1, strGroupName);
*/
	}
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getEvent
 * Signature: ([I[J[Ljava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getEvent
(JNIEnv *env, jobject obj, jintArray anValues, jlongArray alValues, jobjectArray aObjValues)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t*	pEvent;
	int		nReturn;
	int		nLength;
	jobject		objectRef;
	jint*		panValues;
	jlong*		palValues;
	jboolean	bIsCopyI;
	jboolean	bIsCopyL;
	jbyteArray	byteArray;

	/*
	 *	snd_seq_event_input() results in a blocking read on a
	 *	device file. There are two problems:
	 *	1. green threads VMs do no blocking read. Therefore, this
	 *	code doesn't work with green threads at all. A solution is
	 *	outstanding.
	 *	2. In some cases, the read is interrupted by a signal. This
	 *	is the reason for the do..while.
	 */
	do
	{
		// printf("1\n");
		//errno = 0;
		nReturn = snd_seq_event_input(seq, &pEvent);
		//printf("return: %d\n", nReturn);
		// printf("event: %p\n", pEvent);
		//printf("errno: %d\n", errno);
		//perror("abc");
		// printf("2\n");
	}
	while (nReturn == -EINTR);
	if (pEvent == NULL)
	{
		return JNI_FALSE;
	}
	// now uesless?
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_event_input failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	// printf("3\n");
	if (nLength < 13)
	{
		throwRuntimeException(env, "array does not have enough elements (13 required)");
	}
	panValues = (*env)->GetIntArrayElements(env, anValues, &bIsCopyI);
	// printf("4\n");
	if (panValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	nLength = (*env)->GetArrayLength(env, alValues);
	// printf("5\n");
	if (nLength < 1)
	{
		throwRuntimeException(env, "array does not have enough elements (1 required)");
	}
	palValues = (*env)->GetLongArrayElements(env, alValues, &bIsCopyL);
	// printf("6\n");
	if (palValues == NULL)
	{
		throwRuntimeException(env, "GetLongArrayElements failed");
	}
	// printf("6a\n");
	panValues[0] = pEvent->type;
	// printf("6b\n");
	panValues[1] = pEvent->flags;
	// printf("6c\n");
	panValues[2] = pEvent->tag;
	// printf("6d\n");
	panValues[3] = pEvent->queue;

	panValues[4] = pEvent->source.client;
	panValues[5] = pEvent->source.port;

	panValues[6] = pEvent->dest.client;
	panValues[7] = pEvent->dest.port;

	if ((pEvent->flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		palValues[0] = pEvent->time.tick;
	}
	else	// time
	{
		palValues[0] = (jlong) pEvent->time.time.tv_sec * (jlong) 1E9 + (jlong) pEvent->time.time.tv_nsec;
	}

	switch (pEvent->type)
	{
	case SND_SEQ_EVENT_NOTE:
	case SND_SEQ_EVENT_NOTEON:
	case SND_SEQ_EVENT_NOTEOFF:
		panValues[8] = pEvent->data.note.channel;
		panValues[9] = pEvent->data.note.note;
		panValues[10] = pEvent->data.note.velocity;
		panValues[11] = pEvent->data.note.off_velocity;
		panValues[12] = pEvent->data.note.duration;
		break;

	case SND_SEQ_EVENT_KEYPRESS:
	case SND_SEQ_EVENT_CONTROLLER:
	case SND_SEQ_EVENT_PGMCHANGE:
	case SND_SEQ_EVENT_CHANPRESS:
	case SND_SEQ_EVENT_PITCHBEND:
	case SND_SEQ_EVENT_CONTROL14:
	case SND_SEQ_EVENT_NONREGPARAM:
	case SND_SEQ_EVENT_REGPARAM:
		panValues[8] = pEvent->data.control.channel;
		panValues[9] = pEvent->data.control.param;
		panValues[10] = pEvent->data.control.value;
		break;

	case SND_SEQ_EVENT_USR9:
		/*
		 *	Processing this event includes deleting a global
		 *	object reference (see comments in sendObjectEvent()).
		 *	Since this must happen only one per reference and the
		 *	sender of this event only aquires one reference, there
		 *	must be only one receiver of this event deleting the
		 *	reference.
		 *	The programmer is responsible for this. Other receivers
		 *	than the dedicated one must pass null or an empty
		 *	array for the object array, so that processing of
		 *	these events does not happen for the other receivers.
		 */
		if (aObjValues != NULL)
		{
			break;
		}
		nLength = (*env)->GetArrayLength(env, aObjValues);
		if (nLength < 1)
		{
			break;
			// throwRuntimeException(env, "array does not have enough elements (1 required)");
		}
		/*
		 *	There is a potential pitfal here. If on a 64-bit
		 *	machine jobject is a 64-bit pointer and the
		 *	architecture allows only 64-bit-aligned access, this
		 *	may result in an error since the data in raw32 events
		 *	is only 32-bit-aligned.
		 */
		objectRef = *((jobject*) pEvent->data.raw32.d);
		(*env)->SetObjectArrayElement(env, aObjValues, 0, objectRef);
		(*env)->DeleteGlobalRef(env, objectRef);
		break;

	case SND_SEQ_EVENT_SYSEX:
	case SND_SEQ_EVENT_BOUNCE:
	case SND_SEQ_EVENT_USR_VAR0:
	case SND_SEQ_EVENT_USR_VAR1:
	case SND_SEQ_EVENT_USR_VAR2:
	case SND_SEQ_EVENT_USR_VAR3:
	case SND_SEQ_EVENT_USR_VAR4:
	{
		jbyteArray	abData;
		abData = (*env)->NewByteArray(env, pEvent->data.ext.len);
		if (abData == NULL)
		{
			throwRuntimeException(env, "NewByteArray failed");
		}
		(*env)->SetByteArrayRegion(env, abData, 0, pEvent->data.ext.len, pEvent->data.ext.ptr);
		nLength = (*env)->GetArrayLength(env, aObjValues);
		if (nLength < 1)
		{
			throwRuntimeException(env, "array does not have enough elements (1 required)");
		}
		(*env)->SetObjectArrayElement(env, aObjValues, 0, abData);
	}
	break;
	}



	if (bIsCopyI == JNI_TRUE)
	{
		(*env)->ReleaseIntArrayElements(env, anValues, panValues, 0);
	}
	if (bIsCopyL == JNI_TRUE)
	{
		(*env)->ReleaseLongArrayElements(env, alValues, palValues, 0);
	}

	if ((pEvent->flags & SND_SEQ_EVENT_LENGTH_MASK) == SND_SEQ_EVENT_LENGTH_VARUSR)
	{
		byteArray = (*env)->NewByteArray(env, pEvent->data.ext.len);
		if (byteArray == NULL)
		{
			throwRuntimeException(env, "NewByteArray failed");
		}
		(*env)->SetByteArrayRegion(env, byteArray, 0, pEvent->data.ext.len, pEvent->data.ext.ptr);
		(*env)->SetObjectArrayElement(env, aObjValues, 0, byteArray);
	}
	// TODO: should events be freed with snd_seq_free_event()?
	return JNI_TRUE;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getNextClientInfo
 * Signature: (I[I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getNextClientInfo
(JNIEnv *env, jobject obj, jint nClient, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_client_info_t	clientInfo;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn;

	memset(&clientInfo, 0, sizeof(clientInfo));
	clientInfo.client = nClient;
	nReturn = snd_seq_query_next_client(seq, &clientInfo);
	// printf("return: %d\n", nReturn);
	// perror("abc");
	// -2 (no such file or directory): returned when no more client is available
	if (nReturn < 0/* && nReturn != -2*/)
	{
		// throwRuntimeException(env, "snd_seq_query_next_client failed");
	}
	else
	{
		fillClientInfoArrays(env, obj, &clientInfo, anValues, astrValues);
	}
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getNextPortInfo
 * Signature: (II[I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getNextPortInfo
(JNIEnv *env, jobject obj, jint nClient, jint nPort, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_port_info_t	portInfo;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn;

	// printf("1\n");
	memset(&portInfo, 0, sizeof(portInfo));
	portInfo.client = nClient;
	portInfo.port = nPort;
	// printf("2\n");
	nReturn = snd_seq_query_next_port(seq, &portInfo);
	// printf("3\n");
	if (nReturn < 0)
	{
		// throwRuntimeException(env, "snd_seq_query_next_port failed");
	}
	else
	{
		// printf("4\n");
		fillPortInfoArrays(env, obj, &portInfo, anValues, astrValues);
		// printf("5\n");
	}
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getQueueStatus
 * Signature: (I[I[J)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getQueueStatus
(JNIEnv *env, jobject obj, jint nQueue, jintArray anValues, jlongArray alValues)
{
	jint*	values = NULL;
	jlong*	lvalues = NULL;
	int	nLength;
	snd_seq_queue_status_t	queueStatus;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn = snd_seq_get_queue_status(seq, nQueue, &queueStatus);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_queue_status failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 3)
	{
		throwRuntimeException(env, "array does not have enough elements (3 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = queueStatus.events;
	values[1] = queueStatus.running;
	values[2] = queueStatus.flags;
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
	lvalues[0] = queueStatus.tick;
	lvalues[1] = (jlong) queueStatus.time.tv_sec * 1000000000 + queueStatus.time.tv_nsec;
	(*env)->ReleaseLongArrayElements(env, alValues, lvalues, 0);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getQueueTempo
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getQueueTempo
(JNIEnv *env, jobject obj, jint nQueue, jintArray anValues)
{
	jint*	values = NULL;
	int	nLength;
	snd_seq_queue_tempo_t	queueTempo;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn = snd_seq_get_queue_tempo(seq, nQueue, &queueTempo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_queue_tempo failed");
	}
	nLength = (*env)->GetArrayLength(env, anValues);
	if (nLength < 2)
	{
		throwRuntimeException(env, "array does not have enough elements (2 required)");
	}
	values = (*env)->GetIntArrayElements(env, anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements failed");
	}
	values[0] = queueTempo.tempo;
	values[1] = queueTempo.ppq;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    getSystemInfo
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_getSystemInfo
(JNIEnv *env, jobject obj, jintArray anValues)
{
	jint*	values = NULL;
	int	nLength;
	snd_seq_system_info_t	systemInfo;
	snd_seq_t*	seq = getNativeSeq(env, obj);
	int		nReturn = snd_seq_system_info(seq, &systemInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_system_info failed");
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
	values[0] = systemInfo.queues;
	values[1] = systemInfo.clients;
	values[2] = systemInfo.ports;
	values[3] = systemInfo.channels;
	(*env)->ReleaseIntArrayElements(env, anValues, values, 0);
}

/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_open
(JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq;
	int		nReturn;
	// errno = 0;
	nReturn = snd_seq_open(&seq, SND_SEQ_OPEN);
	// printf("2\n");
	// printf("return: %d\n", nReturn);
	// perror("abc");
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_open failed");
		return (jint) nReturn;
	}
	// printf("3\n");
	snd_seq_block_mode(seq, 1);
	// printf("4\n");
	setNativeSeq(env, obj, seq);
	// printf("5\n");
	nReturn = snd_seq_client_id(seq);
	// printf("6\n");
	// printf("return: %d\n", nReturn);
	// perror("abc");
	fflush(stdout);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_client_id failed");
	}
	// printf("7\n");
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    sendControlEvent
 * Signature: (IIJIIIIIIIIII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_sendControlEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nChannel, jint nParam, jint nValue)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t	event;
	memset(&event, 0, sizeof(event));

	event.type = nType;
	event.flags = nFlags;
	event.tag = nTag;
	event.queue = nQueue;
	if ((event.flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		event.time.tick = lTime;
	}
	else
	{
		event.time.time.tv_sec = lTime / 1000000000;
		event.time.time.tv_nsec = lTime % 1000000000;
	}

	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nSourcePort;
	event.dest.client = nDestClient;
	event.dest.port = nDestPort;

	event.data.control.channel = nChannel;
	event.data.control.param = nParam;
	event.data.control.value = nValue;

	sendEvent(env, seq, &event);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    sendNoteEvent
 * Signature: (IIJIIIIIIIIIII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_sendNoteEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nChannel, jint nNote, jint nVelocity, jint nOffVelocity, jint nDuration)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t	event;
	memset(&event, 0, sizeof(event));

	event.type = nType;
	//printf("event.type: %d\n", event.type);
	//printf(": %d\n", );
	event.flags = nFlags;
	//printf("event.flags: %d\n", event.flags);
	event.tag = nTag;
	//printf("event.tag: %d\n", event.tag);
	event.queue = nQueue;
	//printf("event.queue: %d\n", event.queue);
	if ((event.flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		event.time.tick = lTime;
		//printf("event.time.tick: %d\n", event.time.tick);
	}
	else
	{
		event.time.time.tv_sec = lTime / 1000000000;
		event.time.time.tv_nsec = lTime % 1000000000;
	}

	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nSourcePort;
	//printf("event.source.port: %d\n", event.source.port);
	event.dest.client = nDestClient;
	//printf("event.dest.client: %d\n", event.dest.client);
	event.dest.port = nDestPort;
	//printf("event.dest.port: %d\n", event.dest.port);

	event.data.note.channel = nChannel;
	event.data.note.note = nNote;
	event.data.note.velocity = nVelocity;
	event.data.note.off_velocity = nOffVelocity;
	event.data.note.duration = nDuration;

	sendEvent(env, seq, &event);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    sendObjectEvent
 * Signature: (IIIIJIIILjava/lang/Object;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_sendObjectEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jobject objectRef)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t	event;
	jobject		globalRef;

	memset(&event, 0, sizeof(event));

	event.type = nType;
	event.flags = nFlags;
	event.tag = nTag;
	event.queue = nQueue;
	// TODO: put setting of time into a subprogram
	if ((event.flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		event.time.tick = lTime;
	}
	else
	{
		event.time.time.tv_sec = lTime / 1000000000;
		event.time.time.tv_nsec = lTime % 1000000000;
	}

	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nSourcePort;
	event.dest.client = nDestClient;
	event.dest.port = nDestPort;

	/*
	 *	Normally, object references passed to native code are only
	 *	valid until the native function returns (local reference).
	 *	Here, we need a global reference, since we want to pass
	 *	the reference through the ALSA sequencer. That's the reason
	 *	why we aquire a global reference (its lifetime lasts until
	 *	it is released explicitely). This reference is deleted in
	 *	getEvent(). There must be only one getEvent() that deletes
	 *	the reference (not trivial if a port has multiple subscribers).
	 */
	globalRef = (*env)->NewGlobalRef(env, objectRef);
	if (globalRef == NULL)
	{
		throwRuntimeException(env, "NewGlobalRef failed");
	}
	/*
	 *	There is a potential pitfal here. If on a 64-bit
	 *	machine jobject is a 64-bit pointer and the
	 *	architecture allows only 64-bit-aligned access, this
	 *	may result in an error since the data in raw32 events
	 *	is only 32-bit-aligned.
	 */
	*((jobject*) (event.data.raw32.d)) = globalRef;

	sendEvent(env, seq, &event);
}

/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    sendQueueControlEvent
 * Signature: (IIIIJIIIIIIJ)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_sendQueueControlEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nControlQueue, jint nControlValue, jlong lControlTime)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t	event;
	memset(&event, 0, sizeof(event));

	event.type = nType;
	event.flags = nFlags;
	event.tag = nTag;
	event.queue = nQueue;
	if ((event.flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		event.time.tick = lTime;
	}
	else
	{
		event.time.time.tv_sec = lTime / 1000000000;
		event.time.time.tv_nsec = lTime % 1000000000;
	}

	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nSourcePort;
	event.dest.client = nDestClient;
	event.dest.port = nDestPort;

	event.data.queue.queue = nControlQueue;
	if (nType == SND_SEQ_EVENT_TEMPO)
	{
		event.data.queue.param.value = nControlValue;
	}
	else if (nType == SND_SEQ_EVENT_SETPOS_TICK)
	{
		event.data.queue.param.time.tick = lTime;
	}
	else if (nType == SND_SEQ_EVENT_SETPOS_TIME)
	{
		event.data.queue.param.time.time.tv_sec = lTime / 1000000000;
		event.data.queue.param.time.time.tv_nsec = lTime % 1000000000;
	}

	sendEvent(env, seq, &event);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    sendVarEvent
 * Signature: (IIIIJIII[BII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_sendVarEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jbyteArray abData, jint nOffset, jint nLength)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_event_t	event;
	jbyte*		data;
	memset(&event, 0, sizeof(event));

	event.type = nType;
	event.flags = nFlags;
	event.tag = nTag;
	event.queue = nQueue;
	if ((event.flags & SND_SEQ_TIME_STAMP_MASK) == SND_SEQ_TIME_STAMP_TICK)
	{
		event.time.tick = lTime;
	}
	else
	{
		event.time.time.tv_sec = lTime / 1000000000;
		event.time.time.tv_nsec = lTime % 1000000000;
	}

	// is set by the sequencer to sending client
	//event.source.client = nSourceClient;
	event.source.port = nSourcePort;
	event.dest.client = nDestClient;
	event.dest.port = nDestPort;

	data = (*env)->GetByteArrayElements(env, abData, NULL);
	if (data == NULL)
	{
		throwRuntimeException(env, "GetByteArrayElements failed");
	}
	event.data.ext.ptr = data + nOffset;
	event.data.ext.len = nLength;

	sendEvent(env, seq, &event);
	// some sort of release of the array elements necessary?
}



static void
sendEvent(JNIEnv *env,
	  snd_seq_t*		seq,
	  snd_seq_event_t*	pEvent)
{
	int	nReturn = 0;

	nReturn = snd_seq_event_output(seq, pEvent);
	// IDEA: execute flush only if event wants to circumvent queues?
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_event_output failed");
	}
	// we have to restart this call in case it is interrupted by a signal.
	do
	{
		errno = 0;
		// printf("before flushing output\n");
		nReturn = snd_seq_flush_output(seq);
		// printf("after flushing output\n");
	}
	while ((nReturn == -1 && errno == EINTR) || nReturn == -EINTR);
	if (nReturn < 0)
	{
		printf("return: %d\n", nReturn);
		printf("errno: %d\n", errno);
		perror("abc");
		throwRuntimeException(env, "snd_seq_flush_output failed");
	}
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    setClientName
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_setClientName
(JNIEnv *env, jobject obj, jstring strName)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_client_info_t	clientInfo;
	const char*		name;
	int	nReturn;

	memset(&clientInfo, 0, sizeof(clientInfo));
	nReturn = snd_seq_get_client_info(seq, &clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_client_info failed");
	}
	name = (*env)->GetStringUTFChars(env, strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "GetStringUTFChars failed");
	}
	strncpy(clientInfo.name, name, 63);
	clientInfo.name[64] = 0;
	nReturn = snd_seq_set_client_info(seq, &clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_set_client_info failed");
	}
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    setQueueLocked
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL 
Java_org_tritonus_lowlevel_alsa_ASequencer0_setQueueLocked
(JNIEnv *env, jobject obj, jint nQueue, jboolean bLocked)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_queue_owner_t	owner;
	int	nReturn;

	memset(&owner, 0, sizeof(owner));
	// owner.queue = nQueue;
	nReturn = snd_seq_get_queue_owner(seq, nQueue, &owner);
	if (nReturn < 0)
	{
		// perror("abc");
		throwRuntimeException(env, "snd_seq_get_queue_owner failed");
	}
	owner.locked = bLocked;
	nReturn = snd_seq_set_queue_owner(seq, nQueue, &owner);
	if (nReturn < 0)
	{
		// perror("abc");
		throwRuntimeException(env, "snd_seq_set_queue_owner failed");
	}
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    setQueueTempo
 * Signature: (III)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_ASequencer0_setQueueTempo
(JNIEnv *env, jobject obj, jint nQueue, jint nResolution, jint nTempo)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_queue_tempo_t	tempo;
	int	nReturn;

	memset(&tempo, 0, sizeof(tempo));
	tempo.queue = nQueue;
	tempo.ppq = nResolution;
	tempo.tempo = nTempo;
	// tempo.tempo = 60*1000000/nTempo;

	nReturn = snd_seq_set_queue_tempo(seq, nQueue, &tempo);
	if (nReturn < 0)
	{
		perror("abc");
		throwRuntimeException(env, "snd_seq_set_queue_tempo failed");
	}
}



/*
 * Class:     org_tritonus_lowlevel_alsa_ASequencer0
 * Method:    subscribePort
 * Signature: (IIIIIZZZIII)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_alsa_ASequencer0_subscribePort
(JNIEnv *env, jobject obj,
 jint nSenderClient, jint nSenderPort, jint nDestClient, jint nDestPort,
 jint nQueue, jboolean bExclusive, jboolean bRealtime, jboolean bConvertTime,
 jint nMidiChannels, jint nMidiVoices, jint nSynthVoices)
{
	snd_seq_t*	seq = getNativeSeq(env, obj);
	snd_seq_port_subscribe_t	subs;
	int	nReturn;
	memset(&subs, 0, sizeof(subs));

	subs.sender.client = nSenderClient;
	subs.sender.port = nSenderPort;
	subs.dest.client = nDestClient;
	subs.dest.port = nDestPort;

	subs.queue = nQueue;
	subs.exclusive = bExclusive;
	subs.realtime = bRealtime;
	subs.convert_time = bConvertTime;

	subs.midi_channels = nMidiChannels;
	subs.midi_voices = nMidiVoices;
	subs.synth_voices = nSynthVoices;

	nReturn = snd_seq_subscribe_port(seq, &subs);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_subscribe_port failed");
	}
}



/*** org_tritonus_lowlevel_alsa_ASequencer0.c ***/

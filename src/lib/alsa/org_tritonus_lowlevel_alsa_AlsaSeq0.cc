/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq0.c
 */

#include	<errno.h>
#include	<string.h>
#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq0.h"
// currently doesn't work for doubious reasons
// #include	"constants_check.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_t*>	handler;


static void
sendEvent(JNIEnv *env, snd_seq_t* seq, snd_seq_event_t* pEvent);




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    allocQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_allocQueue
(JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq;
	int		nQueue;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_allocQueue(): begin\n"); }
	seq = handler.getHandle(env, obj);
	nQueue = snd_seq_alloc_queue(seq);
	if (nQueue < 0)
	{
		throwRuntimeException(env, "snd_seq_alloc_queue() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_allocQueue(): end\n"); }
	return (jint) nQueue;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_close
(JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_close(): begin\n"); }
	seq = handler.getHandle(env, obj);
	nReturn = snd_seq_close(seq);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_close() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_close(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    createPort
 * Signature: (Ljava/lang/String;IIIIII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_createPort
(JNIEnv *env, jobject obj, jstring strName, jint nCapabilities, jint nGroupPermissions, jint nType, jint nMidiChannels, jint nMidiVoices, jint nSynthVoices)
{
	snd_seq_t*		seq;
	snd_seq_port_info_t*	portInfo;
	const char*		name;
	int			nReturn;
	int			nPort;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_createPort(): begin\n");	}
	snd_seq_port_info_alloca(&portInfo);
	seq = handler.getHandle(env, obj);
	// TODO: check if another action is required instead
	// memset(&portInfo, 0, sizeof(portInfo));
	name = env->GetStringUTFChars(strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "GetStringUTFChars() failed");
	}
	snd_seq_port_info_set_name(portInfo, name);
	env->ReleaseStringUTFChars(strName, name);
	snd_seq_port_info_set_capability(portInfo, nCapabilities);
	snd_seq_port_info_set_port(portInfo, nGroupPermissions);
	snd_seq_port_info_set_type(portInfo, nType);
	snd_seq_port_info_set_midi_channels(portInfo, nMidiChannels);
	snd_seq_port_info_set_midi_voices(portInfo, nMidiVoices);
	snd_seq_port_info_set_synth_voices(portInfo, nSynthVoices);
	//portInfo.write_use = 1;	// R/O attrs?
	//portInfo.read_use = 1;

	// errno = 0;
	nReturn = snd_seq_create_port(seq, portInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_create_port() failed");
	}
	nPort = snd_seq_port_info_get_port(portInfo);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_createPort(): end\n"); }
	return (jint) nPort;
}



static void
fillClientInfoArrays(JNIEnv *env, jobject obj, snd_seq_client_info_t* clientInfo, jintArray anValues, jobjectArray astrValues)
{
	jint*		pnValues;

	if (DEBUG) { (void) fprintf(debug_file, "fillClientInfoArrays(): begin\n"); }
	checkArrayLength(env, anValues, 6);
	pnValues = env->GetIntArrayElements(anValues, NULL);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	pnValues[0] = snd_seq_client_info_get_client(clientInfo);
	pnValues[1] = snd_seq_client_info_get_type(clientInfo);
	pnValues[2] = snd_seq_client_info_get_broadcast_filter(clientInfo);
	pnValues[3] = snd_seq_client_info_get_error_bounce(clientInfo);
	pnValues[4] = snd_seq_client_info_get_num_ports(clientInfo);
	pnValues[5] = snd_seq_client_info_get_event_lost(clientInfo);
	env->ReleaseIntArrayElements(anValues, pnValues, 0);

	checkArrayLength(env, astrValues, 2);
	setStringArrayElement(env, astrValues, 0, snd_seq_client_info_get_name(clientInfo));
	// TODO: does not represent a string (unsigned char)?
/*
	strEventFilter = env->NewStringUTF((const char*) snd_seq_client_info_get_event_filter(clientInfo));
	env->SetObjectArrayElement(astrValues, 1, strEventFilter);
*/
	if (DEBUG) { (void) fprintf(debug_file, "fillClientInfoArrays(): end\n"); }
}



static void
fillPortInfoArrays(JNIEnv *env, jobject obj, snd_seq_port_info_t* portInfo, jintArray anValues, jobjectArray astrValues)
{
	jint*		pnValues;

	checkArrayLength(env, anValues, 10);
	pnValues = env->GetIntArrayElements(anValues, NULL);
	if (pnValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	pnValues[0] = snd_seq_port_info_get_client(portInfo);
	pnValues[1] = snd_seq_port_info_get_port(portInfo);
	pnValues[2] = snd_seq_port_info_get_capability(portInfo);
	pnValues[3] = snd_seq_port_info_get_type(portInfo);
	pnValues[4] = snd_seq_port_info_get_midi_channels(portInfo);
	pnValues[5] = snd_seq_port_info_get_midi_voices(portInfo);
	pnValues[6] = snd_seq_port_info_get_synth_voices(portInfo);
	pnValues[7] = snd_seq_port_info_get_read_use(portInfo);
	pnValues[8] = snd_seq_port_info_get_write_use(portInfo);
	pnValues[9] = snd_seq_port_info_get_port_specified(portInfo);
	env->ReleaseIntArrayElements(anValues, pnValues, 0);
	checkArrayLength(env, astrValues, 1);
	setStringArrayElement(env, astrValues, 0, snd_seq_port_info_get_name(portInfo));
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getClientInfo
 * Signature: (I[I[Ljava/lang/String;)V
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getClientInfo
(JNIEnv *env, jobject obj, jint nClient, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_t*		seq;
	snd_seq_client_info_t*	clientInfo;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getClientInfo(): begin\n"); }
	seq = handler.getHandle(env, obj);
	// memset(&clientInfo, 0, sizeof(clientInfo));
	nReturn = snd_seq_get_any_client_info(seq, nClient, clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_any_client_info() failed");
	}
	else
	{
		fillClientInfoArrays(env, obj, clientInfo, anValues, astrValues);
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getClientInfo(): end\n"); }
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getEvent
 * Signature: ([I[J[Ljava/lang/Object;)Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getEvent
(JNIEnv *env, jobject obj, jintArray anValues, jlongArray alValues, jobjectArray aObjValues)
{
	snd_seq_t*		seq;
	snd_seq_event_t*	pEvent;
	int			nReturn;
	jobject			objectRef;
	jint*			panValues;
	jlong*			palValues;
	jbyteArray		byteArray;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);

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
		throwRuntimeException(env, "snd_seq_event_input() failed");
	}
	checkArrayLength(env, anValues, 13);
	panValues = env->GetIntArrayElements(anValues, NULL);
	// printf("4\n");
	if (panValues == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}

	checkArrayLength(env, alValues, 1);
	palValues = env->GetLongArrayElements(alValues, NULL);
	// printf("6\n");
	if (palValues == NULL)
	{
		throwRuntimeException(env, "GetLongArrayElements() failed");
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
		checkArrayLength(env, aObjValues, 1);

		/*
		 *	There is a potential pitfal here. If on a 64-bit
		 *	machine jobject is a 64-bit pointer and the
		 *	architecture allows only 64-bit-aligned access, this
		 *	may result in an error since the data in raw32 events
		 *	is only 32-bit-aligned.
		 */
		objectRef = *((jobject*) pEvent->data.raw32.d);
		env->SetObjectArrayElement(aObjValues, 0, objectRef);
		env->DeleteGlobalRef(objectRef);
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
		abData = env->NewByteArray(pEvent->data.ext.len);
		if (abData == NULL)
		{
			throwRuntimeException(env, "NewByteArray() failed");
		}
		env->SetByteArrayRegion(abData, (jsize) 0, (jsize) pEvent->data.ext.len, (jbyte*) pEvent->data.ext.ptr);
		checkArrayLength(env, aObjValues, 1);
		env->SetObjectArrayElement(aObjValues, 0, abData);
	}
	break;
	}



	env->ReleaseIntArrayElements(anValues, panValues, 0);
	env->ReleaseLongArrayElements(alValues, palValues, 0);

	if ((pEvent->flags & SND_SEQ_EVENT_LENGTH_MASK) == SND_SEQ_EVENT_LENGTH_VARUSR)
	{
		byteArray = env->NewByteArray(pEvent->data.ext.len);
		if (byteArray == NULL)
		{
			throwRuntimeException(env, "NewByteArray() failed");
		}
		env->SetByteArrayRegion(byteArray, (jsize) 0, (jsize) pEvent->data.ext.len, (jbyte*) pEvent->data.ext.ptr);
		env->SetObjectArrayElement(aObjValues, 0, byteArray);
	}
	// TODO: should events be freed with snd_seq_free_event()?
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getEvent(): end\n"); }
	return JNI_TRUE;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getNextClientInfo
 * Signature: (I[I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextClientInfo
(JNIEnv *env, jobject obj, jint nClient, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_t*		seq;
	snd_seq_client_info_t*	clientInfo;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextClientInfo(): begin\n"); }
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextClientInfo(): passed client: %d\n", (int) nClient); }
	seq = handler.getHandle(env, obj);
	snd_seq_client_info_alloca(&clientInfo);
	snd_seq_client_info_set_client(clientInfo, nClient);
	nReturn = snd_seq_query_next_client(seq, clientInfo);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextClientInfo(): return value from snd_seq_query_next_client(): %d\n", nReturn); }
	if (nReturn < 0)
	{
		// -2 (no such file or directory): returned when no more client is available
		if (nReturn != -2)
		{
			throwRuntimeException(env, "snd_seq_query_next_client() failed");
		}
	}
	else
	{
		fillClientInfoArrays(env, obj, clientInfo, anValues, astrValues);
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextClientInfo(): end\n"); }
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getNextPortInfo
 * Signature: (II[I[Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextPortInfo
(JNIEnv *env, jobject obj, jint nClient, jint nPort, jintArray anValues, jobjectArray astrValues)
{
	snd_seq_t*		seq;
	snd_seq_port_info_t*	portInfo;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextPortInfo(): begin\n"); }
	seq = handler.getHandle(env, obj);
	snd_seq_port_info_alloca(&portInfo);
	snd_seq_port_info_set_client(portInfo, nClient);
	snd_seq_port_info_set_port(portInfo, nPort);
	nReturn = snd_seq_query_next_port(seq, portInfo);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextPortInfo(): snd_seq_query_next_port() returns: %d\n", nReturn); }
	if (nReturn < 0)
	{
		// -2 (no such file or directory): returned when no more port is available
		if (nReturn != -2)
		{
			throwRuntimeException(env, "snd_seq_query_next_port() failed");
		}
	}
	else
	{
		fillPortInfoArrays(env, obj, portInfo, anValues, astrValues);
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getNextPortInfo(): end\n"); }
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getQueueStatus
 * Signature: (I[I[J)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueStatus
(JNIEnv *env, jobject obj, jint nQueue, jintArray anValues, jlongArray alValues)
{
	snd_seq_t*		seq;
	snd_seq_queue_status_t*	queueStatus;
	int			nReturn;
	jint*			values = NULL;
	jlong*			lvalues = NULL;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueStatus(): begin\n"); }
	snd_seq_queue_status_alloca(&queueStatus);
	seq = handler.getHandle(env, obj);
	nReturn = snd_seq_get_queue_status(seq, nQueue, queueStatus);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_queue_status() failed");
	}

	checkArrayLength(env, anValues, 2);
	values = env->GetIntArrayElements(anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	// queue ?
	values[0] = snd_seq_queue_status_get_events(queueStatus);
	values[1] = snd_seq_queue_status_get_status(queueStatus);
	env->ReleaseIntArrayElements(anValues, values, 0);

	checkArrayLength(env, alValues, 2);
	lvalues = env->GetLongArrayElements(alValues, NULL);
	if (lvalues == NULL)
	{
		throwRuntimeException(env, "GetLongArrayElements() failed");
	}
	lvalues[0] = snd_seq_queue_status_get_tick_time(queueStatus);
	const snd_seq_real_time_t*	realTime = snd_seq_queue_status_get_real_time(queueStatus);
	lvalues[1] = (jlong) realTime->tv_sec * 1000000000 + realTime->tv_nsec;
	env->ReleaseLongArrayElements(alValues, lvalues, 0);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueStatus(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getQueueTempo
 * Signature: (I[I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueTempo
(JNIEnv *env, jobject obj, jint nQueue, jintArray anValues)
{
	snd_seq_t*		seq;
	snd_seq_queue_tempo_t*	queueTempo;
	jint*			values = NULL;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueTempo(): begin\n"); }
	snd_seq_queue_tempo_alloca(&queueTempo);
	seq = handler.getHandle(env, obj);
	nReturn = snd_seq_get_queue_tempo(seq, nQueue, queueTempo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_queue_tempo() failed");
	}

	checkArrayLength(env, anValues, 2);
	values = env->GetIntArrayElements(anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	values[0] = snd_seq_queue_tempo_get_tempo(queueTempo);
	values[1] = snd_seq_queue_tempo_get_ppq(queueTempo);
	env->ReleaseIntArrayElements(anValues, values, 0);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getQueueTempo(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    getSystemInfo
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getSystemInfo
(JNIEnv *env, jobject obj, jintArray anValues)
{
	snd_seq_t*		seq;
	snd_seq_system_info_t*	systemInfo;
	jint*			values = NULL;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getSystemInfo(): begin\n"); }
	snd_seq_system_info_alloca(&systemInfo);
	seq = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info(seq, systemInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_system_info() failed");
	}
	checkArrayLength(env, anValues, 4);
	values = env->GetIntArrayElements(anValues, NULL);
	if (values == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	values[0] = snd_seq_system_info_get_queues(systemInfo);
	values[1] = snd_seq_system_info_get_clients(systemInfo);
	values[2] = snd_seq_system_info_get_ports(systemInfo);
	values[3] = snd_seq_system_info_get_channels(systemInfo);
	env->ReleaseIntArrayElements(anValues, values, 0);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_getSystemInfo(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    open
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_open
(JNIEnv *env, jobject obj)
{
	snd_seq_t*	seq;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_open(): begin\n"); }
	nReturn = snd_seq_open(&seq, "hw", SND_SEQ_OPEN_DUPLEX, 0);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_open(): snd_seq_open() returns: %d\n", nReturn); }
	// perror("abc");
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_open() failed");
		return (jint) nReturn;
	}
	snd_seq_nonblock(seq, SND_SEQ_NONBLOCK);
	handler.setHandle(env, obj, seq);
	nReturn = snd_seq_client_id(seq);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_open(): snd_seq_client_id() returns: %d\n", nReturn); }
	// perror("abc");
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_client_id() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_open(): end\n"); }
	return (jint) nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    sendControlEvent
 * Signature: (IIJIIIIIIIIII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendControlEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nChannel, jint nParam, jint nValue)
{
	snd_seq_t*		seq;
	snd_seq_event_t		event;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendControlEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);
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
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendControlEvent(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    sendNoteEvent
 * Signature: (IIJIIIIIIIIIII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendNoteEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nChannel, jint nNote, jint nVelocity, jint nOffVelocity, jint nDuration)
{
	snd_seq_t*		seq;
	snd_seq_event_t		event;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendNoteEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);
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
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendNoteEvent(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    sendObjectEvent
 * Signature: (IIIIJIIILjava/lang/Object;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendObjectEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jobject objectRef)
{
	snd_seq_t*		seq;
	snd_seq_event_t		event;
	jobject			globalRef;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendObjectEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);

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
	globalRef = env->NewGlobalRef(objectRef);
	if (globalRef == NULL)
	{
		throwRuntimeException(env, "NewGlobalRef() failed");
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
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendObjectEvent(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    sendQueueControlEvent
 * Signature: (IIIIJIIIIIIJ)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendQueueControlEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jint nControlQueue, jint nControlValue, jlong lControlTime)
{
	snd_seq_t*		seq;
	snd_seq_event_t		event;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendQueueControlEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);
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
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendQueueControlEvent(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    sendVarEvent
 * Signature: (IIIIJIII[BII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendVarEvent
(JNIEnv *env, jobject obj,
 jint nType, jint nFlags, jint nTag, jint nQueue, jlong lTime,
 jint nSourcePort, jint nDestClient, jint nDestPort,
 jbyteArray abData, jint nOffset, jint nLength)
{
	snd_seq_t*		seq;
	snd_seq_event_t		event;
	jbyte*			data;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendVarEvent(): begin\n"); }
	seq = handler.getHandle(env, obj);
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

	data = env->GetByteArrayElements(abData, NULL);
	if (data == NULL)
	{
		throwRuntimeException(env, "GetByteArrayElements() failed");
	}
	event.data.ext.ptr = data + nOffset;
	event.data.ext.len = nLength;

	sendEvent(env, seq, &event);
	// TODO: some sort of release of the array elements necessary?
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_sendVarEvent(): end\n"); }
}



static void
sendEvent(JNIEnv *env,
	  snd_seq_t*		seq,
	  snd_seq_event_t*	pEvent)
{
	int	nReturn = 0;

	nReturn = snd_seq_event_output(seq, pEvent);
	// IDEA: execute drain only if event wants to circumvent queues?
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_event_output() failed");
	}
	// we have to restart this call in case it is interrupted by a signal.
	do
	{
		errno = 0;
		if (DEBUG) { (void) fprintf(debug_file, "before draining output\n"); }
		nReturn = snd_seq_drain_output(seq);
		if (DEBUG) { (void) fprintf(debug_file, "after draining output\n"); }
	}
	while ((nReturn == -1 && errno == EINTR) || nReturn == -EINTR);
	if (nReturn < 0)
	{
		(void) fprintf(debug_file, "return: %d\n", nReturn);
		(void) fprintf(debug_file, "errno: %d\n", errno);
		perror("abc");
		throwRuntimeException(env, "snd_seq_drain_output() failed");
	}
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    setClientName
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setClientName
(JNIEnv *env, jobject obj, jstring strName)
{
	snd_seq_t*		seq;
	snd_seq_client_info_t*	clientInfo;
	const char*		name;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setClientName(): begin\n"); }
	snd_seq_client_info_alloca(&clientInfo);
	seq = handler.getHandle(env, obj);
	// memset(&clientInfo, 0, sizeof(clientInfo));
	nReturn = snd_seq_get_client_info(seq, clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_get_client_info() failed");
	}
	name = env->GetStringUTFChars(strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "GetStringUTFChars() failed");
	}
	snd_seq_client_info_set_name(clientInfo, name);
	nReturn = snd_seq_set_client_info(seq, clientInfo);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_set_client_info() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setClientName(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    setQueueLocked
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL 
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueLocked
(JNIEnv *env, jobject obj, jint nQueue, jboolean bLocked)
{
// TODO: rework using *queue_info*
// 	snd_seq_t*		seq;
// 	snd_seq_queue_owner_t	owner;
// 	int			nReturn;

// 	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueLocked(): begin\n"); }
// 	seq = handler.getHandle(env, obj);
// 	memset(&owner, 0, sizeof(owner));
// 	// owner.queue = nQueue;
// 	nReturn = snd_seq_get_queue_owner(seq, nQueue, &owner);
// 	if (nReturn < 0)
// 	{
// 		// perror("abc");
// 		throwRuntimeException(env, "snd_seq_get_queue_owner() failed");
// 	}
// 	owner.locked = bLocked;
// 	nReturn = snd_seq_set_queue_owner(seq, nQueue, &owner);
// 	if (nReturn < 0)
// 	{
// 		// perror("abc");
// 		throwRuntimeException(env, "snd_seq_set_queue_owner() failed");
// 	}
// 	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueLocked(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    setQueueTempo
 * Signature: (III)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueTempo
(JNIEnv *env, jobject obj, jint nQueue, jint nResolution, jint nTempo)
{
	snd_seq_t*		seq;
	snd_seq_queue_tempo_t*	tempo;
	int			nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueTempo(): begin\n"); }
	seq = handler.getHandle(env, obj);
	snd_seq_queue_tempo_alloca(&tempo);
	// memset(&tempo, 0, sizeof(tempo));
	snd_seq_queue_tempo_set_ppq(tempo, nResolution);
	snd_seq_queue_tempo_set_tempo(tempo, nTempo);

	nReturn = snd_seq_set_queue_tempo(seq, nQueue, tempo);
	if (nReturn < 0)
	{
		perror("abc");
		throwRuntimeException(env, "snd_seq_set_queue_tempo() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setQueueTempo(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    subscribePort
 * Signature: (IIIIIZZZIII)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_subscribePort
(JNIEnv *env, jobject obj,
 jint nSenderClient, jint nSenderPort, jint nDestClient, jint nDestPort,
 jint nQueue, jboolean bExclusive, jboolean bRealtime, jboolean bConvertTime,
 jint nMidiChannels, jint nMidiVoices, jint nSynthVoices)
{
	snd_seq_t*			seq;
	snd_seq_port_subscribe_t*	subs;
	snd_seq_addr_t			sender;
	snd_seq_addr_t			dest;
	int				nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_subscribePort(): begin\n"); }
	seq = handler.getHandle(env, obj);
	snd_seq_port_subscribe_alloca(&subs);
	// memset(&subs, 0, sizeof(subs));

	sender.client = nSenderClient;
	sender.port = nSenderPort;
	snd_seq_port_subscribe_set_sender(subs, &sender);

	dest.client = nDestClient;
	dest.port = nDestPort;
	snd_seq_port_subscribe_set_dest(subs, &dest);

	snd_seq_port_subscribe_set_queue(subs, nQueue);
	snd_seq_port_subscribe_set_exclusive(subs, bExclusive);
	snd_seq_port_subscribe_set_time_update(subs, bConvertTime);
	snd_seq_port_subscribe_set_time_real(subs, bRealtime);
/* TODO: what happened to this?
	subs.midi_channels = nMidiChannels;
	subs.midi_voices = nMidiVoices;
	subs.synth_voices = nSynthVoices;
*/
	nReturn = snd_seq_subscribe_port(seq, subs);
	if (nReturn < 0)
	{
		throwRuntimeException(env, "snd_seq_subscribe_port() failed");
	}
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq0_subscribePort(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq0
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq0_setTrace
(JNIEnv *env, jclass cls, jboolean bTrace)
{
	DEBUG = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq0.c ***/

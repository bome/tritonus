/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe.cc
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_port_subscribe_t*>	handler;


snd_seq_port_subscribe_t*
getPortSubscribeNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_malloc(): begin\n"); }
	nReturn = snd_seq_port_subscribe_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_free
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_subscribe_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getSenderClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderClient
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_port_subscribe_get_sender(handle);
	nReturn = address->client;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getSenderPort
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderPort
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderPort(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_port_subscribe_get_sender(handle);
	nReturn = address->port;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getSenderPort(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getDestClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestClient
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_port_subscribe_get_dest(handle);
	nReturn = address->client;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getDestPort
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestPort
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	const snd_seq_addr_t*		address;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestPort(): begin\n"); }
	handle = handler.getHandle(env, obj);
	address = snd_seq_port_subscribe_get_dest(handle);
	nReturn = address->port;
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getDestPort(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getQueue
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getQueue
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_subscribe_get_queue(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getQueue(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getExclusive
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getExclusive
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getExclusive(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_subscribe_get_exclusive(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getExclusive(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getTimeUpdate
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeUpdate
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeUpdate(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_subscribe_get_time_update(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeUpdate(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    getTimeReal
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeReal
(JNIEnv* env, jobject obj)
{
	snd_seq_port_subscribe_t*	handle;
	int				nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeReal(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_subscribe_get_time_real(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_getTimeReal(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setSender
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setSender
(JNIEnv* env, jobject obj, jint nClient, jint nPort)
{
	snd_seq_port_subscribe_t*	handle;
	snd_seq_addr_t			newAddress;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setSender(): begin\n"); }
	handle = handler.getHandle(env, obj);
	newAddress.client = nClient;
	newAddress.port = nPort;
	snd_seq_port_subscribe_set_sender(handle, &newAddress);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setSender(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setDest
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setDest
(JNIEnv* env, jobject obj, jint nClient, jint nPort)
{
	snd_seq_port_subscribe_t*	handle;
	snd_seq_addr_t			newAddress;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setDest(): begin\n"); }
	handle = handler.getHandle(env, obj);
	newAddress.client = nClient;
	newAddress.port = nPort;
	snd_seq_port_subscribe_set_dest(handle, &newAddress);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setDest(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setQueue
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setQueue
(JNIEnv* env, jobject obj, jint nQueue)
{
	snd_seq_port_subscribe_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setQueue(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_subscribe_set_queue(handle, nQueue);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setQueue(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setExclusive
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setExclusive
(JNIEnv* env, jobject obj, jboolean bExclusive)
{
	snd_seq_port_subscribe_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setExclusive(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_subscribe_set_exclusive(handle, bExclusive);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setExclusive(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setTimeUpdate
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeUpdate
(JNIEnv* env, jobject obj, jboolean bUpdate)
{
	snd_seq_port_subscribe_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeUpdate(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_subscribe_set_time_update(handle, bUpdate);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeUpdate(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe
 * Method:    setTimeReal
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeReal
(JNIEnv* env, jobject obj, jboolean bReal)
{
	snd_seq_port_subscribe_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeReal(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_subscribe_set_time_real(handle, bReal);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortSubscribe_setTimeReal(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_PortSubscribe.cc ***/

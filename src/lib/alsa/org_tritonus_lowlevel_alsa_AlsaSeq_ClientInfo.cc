/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo.cc
 */

#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_client_info_t*>	handler;


snd_seq_client_info_t*
getClientInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_malloc(): begin\n"); }
	nReturn = snd_seq_client_info_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_client_info_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getClient
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_client(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getType
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getType(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_type(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getType(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getName
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	const char*		pName;
	jstring			strName;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	pName = snd_seq_client_info_get_name(handle);
	strName = env->NewStringUTF(pName);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getName(): end\n"); }
	return strName;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getBroadcastFilter
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getBroadcastFilter
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getBroadcastFilter(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_broadcast_filter(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getBroadcastFilter(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getErrorBounce
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getErrorBounce
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getErrorBounce(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_error_bounce(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getErrorBounce(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getNumPorts
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getNumPorts
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getNumPorts(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_num_ports(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getNumPorts(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    getEventLost
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getEventLost
(JNIEnv* env, jobject obj)
{
	snd_seq_client_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getEventLost(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_client_info_get_event_lost(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_getEventLost(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    setClient
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setClient
(JNIEnv* env, jobject obj, jint nClient)
{
	snd_seq_client_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_client_info_set_client(handle, nClient);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setClient(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    setName
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setName
(JNIEnv* env, jobject obj, jstring strName)
{
	snd_seq_client_info_t*	handle;
	const char*		pName;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	pName = env->GetStringUTFChars(strName, NULL);
	snd_seq_client_info_set_name(handle, pName);
	env->ReleaseStringUTFChars(strName, pName);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setName(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    setBroadcastFilter
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setBroadcastFilter
(JNIEnv* env, jobject obj, jint nBroadcastFilter)
{
	snd_seq_client_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setBroadcastFilter(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_client_info_set_broadcast_filter(handle, nBroadcastFilter);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setBroadcastFilter(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo
 * Method:    setErrorBounce
 * Signature: (I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setErrorBounce
(JNIEnv* env, jobject obj, jint nErrorBounce)
{
	snd_seq_client_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setErrorBounce(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_client_info_set_error_bounce(handle, nErrorBounce);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024ClientInfo_setErrorBounce(): end\n"); }
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_ClientInfo.cc ***/

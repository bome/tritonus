/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.cc
 */

#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_system_info_t*>	handler;


snd_seq_system_info_t*
getSystemInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}


/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): begin\n"); }
	nReturn = snd_seq_system_info_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_system_info_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_queues(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getQueues(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_clients(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getPorts
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_ports(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getPorts(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_channels(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getCurrentClients
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_cur_clients(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentClients(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo
 * Method:    getCurrentQueues
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues
(JNIEnv* env, jobject obj)
{
	snd_seq_system_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_system_info_get_cur_queues(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024SystemInfo_getCurrentQueues(): end\n"); }
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_SystemInfo.cc ***/

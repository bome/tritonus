/*
 *	org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo.cc
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo.h"
#include	"HandleFieldHandler.hh"

static int DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_seq_port_info_t*>	handler;


snd_seq_port_info_t*
getPortInfoNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}




/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_malloc
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	// extremely hacky
	debug_file = stderr;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_malloc(): begin\n"); }
	nReturn = snd_seq_port_info_malloc(&handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_malloc(): handle: %p\n", handle); }
	handler.setHandle(env, obj, handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_malloc(): end\n"); }
	return nReturn;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_free
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_free(): begin\n"); }
	handle = handler.getHandle(env, obj);
	snd_seq_port_info_free(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getClient
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getClient
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getClient(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_client(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getClient(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getPort
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPort
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPort(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_port(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPort(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getName
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	const char*		name;
	jstring			nameObj;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getName(): begin\n"); }
	handle = handler.getHandle(env, obj);
	name = snd_seq_port_info_get_name(handle);
	nameObj = env->NewStringUTF(name);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getName(): end\n"); }
	return nameObj;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getCapability
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getCapability
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getCapability(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_capability(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getCapability(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getType
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getType
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getType(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_type(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getType(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getMidiChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiChannels
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiChannels(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_midi_channels(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiChannels(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getMidiVoices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiVoices
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiVoices(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_midi_voices(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getMidiVoices(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getSynthVoices
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getSynthVoices
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getSynthVoices(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_synth_voices(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getSynthVoices(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getReadUse
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getReadUse
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getReadUse(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_read_use(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getReadUse(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getWriteUse
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getWriteUse
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getWriteUse(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_write_use(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getWriteUse(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo
 * Method:    getPortSpecified
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPortSpecified
(JNIEnv* env, jobject obj)
{
	snd_seq_port_info_t*	handle;
	int			nReturn;

	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPortSpecified(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_seq_port_info_get_port_specified(handle);
	if (DEBUG) { fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaSeq_00024PortInfo_getPortSpecified(): end\n"); }
	return nReturn;
}



/*** org_tritonus_lowlevel_alsa_AlsaSeq_PortInfo.cc ***/

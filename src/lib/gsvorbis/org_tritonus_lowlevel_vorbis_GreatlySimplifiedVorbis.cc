/*
 *	org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis.cc
 */

#include	"common.h"
#include	"HandleFieldHandler.hh"
#include	"org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis.h"



static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<handle_t*>	handler;


static jfieldID
getNativeHandleFieldID(JNIEnv *env)
{
	static jfieldID	nativeHandleFieldID = NULL;

	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/cdda/cdparanoia/Cdparanoia");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.cdda.cdparanoia.CdParanoia");
		}
		nativeHandleFieldID = (*env)->GetFieldID(env, cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static handle_t*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	return (handle_t*) (*env)->GetLongField(env, obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, handle_t* handle)
{
	jfieldID	fieldID = getNativeHandleFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis
 * Method:    open
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis_open
(JNIEnv* env, jobject obj, jint nSampleRate, jint nChannels)
{
	return -1;
}



/*
 * Class:     org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis_close
(JNIEnv* env, jobject obj)
{
}



/*
 * Class:     org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis
 * Method:    encode
 * Signature: ([B[B)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis_encode
(JNIEnv* env, jobject obj, jbyteArray abPcm, jbyteArray abVorbis)
{
	return -1;
}



/*
 * Class:     org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
}



/*** org_tritonus_lowlevel_vorbis_GreatlySimplifiedVorbis.cc ***/

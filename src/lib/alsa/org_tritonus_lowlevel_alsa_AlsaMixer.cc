/*
 *	org_tritonus_lowlevel_alsa_AlsaMixer.c
 */


#include	<sys/asoundlib.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_alsa_AlsaMixer.h"
#include	"HandleFieldHandler.hh"


static int	DEBUG = 0;
static FILE*	debug_file = NULL;

static HandleFieldHandler<snd_mixer_t*>	handler;


snd_mixer_t*
getMixerNativeHandle(JNIEnv *env, jobject obj)
{
	return handler.getHandle(env, obj);
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    open
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_open
(JNIEnv *env, jobject obj, jint nMode)
{
	snd_mixer_t*	handle;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_open(): begin\n"); }
	nReturn = snd_mixer_open(&handle, nMode);
	handler.setHandle(env, obj, handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_open(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    attach
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_attach
(JNIEnv *env, jobject obj, jstring strCardName)
{
	snd_mixer_t*	handle;
	int		nReturn;
	const char*	cardName;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_attach(): begin\n"); }
	handle = handler.getHandle(env, obj);
	cardName = env->GetStringUTFChars(strCardName, NULL);
	if (cardName == NULL)
	{
		throwRuntimeException(env, "cannot retrieve chars from card name string");
		return -1;
	}
	nReturn = snd_mixer_attach(handle, cardName);
	env->ReleaseStringUTFChars(strCardName, cardName);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_attach(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    register
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_register
(JNIEnv *env, jobject obj)
{
	snd_mixer_t*	handle;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_register(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_selem_register(handle, NULL, NULL);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_register(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    load
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_load
(JNIEnv *env, jobject obj)
{
	snd_mixer_t*	handle;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_load(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_load(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_load(): end\n"); }
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
	snd_mixer_t*	handle;
	int		nReturn;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_close(): begin\n"); }
	handle = handler.getHandle(env, obj);
	nReturn = snd_mixer_close(handle);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_close(): end\n"); }
	return nReturn;
}

/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    findElement
 * Signature: (Ljava/lang/String;I)Lorg/tritonus/lowlevel/alsa/AlsaMixerElement;
 */
JNIEXPORT jobject JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_findElement
(JNIEnv *env, jobject obj, jstring strName, jint nIndex)
{
	jobject			element;
	snd_mixer_t*		handle;
	snd_mixer_elem_t*	elem;
	snd_mixer_selem_id_t*	sid;
	const char*		name;
	jclass			element_class;
	jmethodID		constructorID;
	jfieldID		handleFieldID;

	if (DEBUG)
	{
		(void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_findElement(): begin\n");
	}
	handle = handler.getHandle(env, obj);
	snd_mixer_selem_id_alloca(&sid);
	snd_mixer_selem_id_set_index(sid, nIndex);
	name = env->GetStringUTFChars(strName, NULL);
	if (name == NULL)
	{
		throwRuntimeException(env, "cannot retrieve chars from mixer name string");
		return NULL;
	}
	snd_mixer_selem_id_set_name(sid, name);
	env->ReleaseStringUTFChars(strName, name);
	elem = snd_mixer_find_selem(handle, sid);
	if (elem == NULL)
	{
		return NULL;
	}

	element_class = env->FindClass("org/tritonus/lowlevel/alsa/AlsaMixerElement");
	if (element_class == NULL)
	{
		throwRuntimeException(env, "cannot get class object for AlsaMixerElement");
	}
	constructorID = env->GetMethodID(element_class, "<init>", "(Lorg/tritonus/lowlevel/alsa/AlsaMixer;ILjava/lang/String;)V");
	if (constructorID == NULL)
	{
		throwRuntimeException(env, "cannot get method ID for constructor");
	}
	element = env->NewObject(element_class, constructorID, NULL, 0, NULL);
	if (element == NULL)
	{
		throwRuntimeException(env, "object creation failed");
	}
	// TODO: set the handle)
	handleFieldID = env->GetFieldID(element_class, "m_lNativeHandle", "J");
	if (handleFieldID == NULL)
	{
		throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
	}
	env->SetLongField(element, handleFieldID, (jlong) (long) elem);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_findElement(): end\n"); }
	return element;
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
	snd_mixer_t*		handle;
	int			nReturn;
	int			nIndex;
	snd_mixer_elem_t*	element;
	jint*			indices = NULL;

	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_readControlList(): begin\n"); }
	handle = handler.getHandle(env, obj);
	indices = env->GetIntArrayElements(anIndices, NULL);
	if (indices == NULL)
	{
		throwRuntimeException(env, "GetIntArrayElements() failed");
	}
	nIndex = 0;
	element = snd_mixer_first_elem(handle);
	while (element != NULL)
	{
		// TODO: should not throw exception, but return -1 (and clean the array)
		checkArrayLength(env, anIndices, nIndex + 1);
		checkArrayLength(env, astrNames, nIndex + 1);
		indices[nIndex] = snd_mixer_selem_get_index(element);
		setStringArrayElement(env, astrNames, nIndex,
				      snd_mixer_selem_get_name(element));
		nIndex++;
		element = snd_mixer_elem_next(element);
	}
	nReturn = nIndex;
	env->ReleaseIntArrayElements(anIndices, indices, 0);
	if (DEBUG) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_AlsaMixer_readControlList(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_alsa_AlsaMixer
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_alsa_AlsaMixer_setTrace
(JNIEnv *env, jclass cls, jboolean bTrace)
{
	DEBUG = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_alsa_AlsaMixer.c ***/

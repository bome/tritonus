/*
 *	org_tritonus_lowlevel_saint_Saint.cpp
*/

#include	<iostream>
#include	<Saint.hpp>

#include	"org_tritonus_lowlevel_saint_Saint.h"
#include	"jstreambuf.hpp"

// static int	DEBUG = 1;


static void
throwRuntimeException(JNIEnv *env, char* pStrMessage)
{
	static  jclass	runtimeExceptionClass = NULL;

	if (runtimeExceptionClass == NULL)
	{
		runtimeExceptionClass = env->FindClass("java/lang/RuntimeException");
		if (runtimeExceptionClass == NULL)
		{
			env->FatalError("cannot get class object for java.lang.RuntimeException");
		}
	}
	env->ThrowNew(runtimeExceptionClass, pStrMessage);
}



static jfieldID
getNativeSeqFieldID(JNIEnv *env)
{
	static jfieldID	nativeHandleFieldID = NULL;

	if (nativeHandleFieldID == NULL)
	{
		// TODO: use a passed object rather than the name of the class
		jclass	cls = env->FindClass("org/tritonus/lowlevel/saint/Saint");
		if (cls == NULL)
		{
			throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.saint.Saint");
		}
		nativeHandleFieldID = env->GetFieldID(cls, "m_lNativeHandle", "J");
		if (nativeHandleFieldID == NULL)
		{
			throwRuntimeException(env, "cannot get field ID for m_lNativeHandle");
		}
	}
	return nativeHandleFieldID;
}



static Saint*
getNativeHandle(JNIEnv *env, jobject obj)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	return (Saint*) env->GetLongField(obj, fieldID);
}



static void
setNativeHandle(JNIEnv *env, jobject obj, Saint* handle)
{
	jfieldID	fieldID = getNativeSeqFieldID(env);
	env->SetLongField(obj, fieldID, (jlong) handle);
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    getChannelCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_saint_Saint_getChannelCount
(JNIEnv *env, jobject obj)
{
	Saint*	saint = getNativeHandle(env, obj);
	return saint->getChannelCount();
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    getSamplingRate
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_saint_Saint_getSamplingRate
(JNIEnv *env, jobject obj)
{
	Saint*	saint = getNativeHandle(env, obj);
	return saint->getSamplingRate();
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    init
 * Signature: (Ljava/io/InputStream;)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_init__Ljava_io_InputStream_2
(JNIEnv *env, jobject obj, jobject bitStream)
{
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    init
 * Signature: (Ljava/io/InputStream;Ljava/io/InputStream;)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_saint_Saint_init__Ljava_io_InputStream_2Ljava_io_InputStream_2
(JNIEnv *env, jobject obj, jobject orchestraStream, jobject scoreStream)
{
	jstreambuf*	orchestraStreamBuf = new jstreambuf(env, orchestraStream);
	istream*	orchestraIStream = new istream(orchestraStreamBuf);
	jstreambuf*	scoreStreamBuf = new jstreambuf(env, scoreStream);
	istream*	scoreIStream = new istream(scoreStreamBuf);
	Saint*	saint = new Saint(orchestraIStream, scoreIStream);
	setNativeHandle(env, obj, saint);
}

/*
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_init__Ljava_io_InputStream_2Ljava_io_InputStream_2Ljava_io_OutputStream_2I
(JNIEnv *env, jobject obj, jobject orchestraStream, jobject scoreStream, jobject outputStream, jint nOutputFormat)
{
	jstreambuf*	orchestraStreamBuf = new jstreambuf(env, orchestraStream);
	istream*	orchestraIStream = new istream(orchestraStreamBuf);
	Saint*	saint = new Saint(orchestraIStream, scoreIStream, nOutputFormat);
	setNativeHandle(env, obj, saint);
}
*/


/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    run
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_run
(JNIEnv *env, jobject obj)
{
	Saint*	saint = getNativeHandle(env, obj);
	saint->run();
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    setOutput
 * Signature: (Ljava/io/OutputStream;I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_setOutput
(JNIEnv *env, jobject obj, jobject outputStream, jint nOutputFormat)
{
	cerr << "in (native) setOutput()" << endl;
	jstreambuf*	outputStreamBuf = new jstreambuf(env, outputStream, 128000);
	ostream*	outputOStream = new ostream(outputStreamBuf);
	Out*	pOut = new Out(outputOStream, nOutputFormat);
	Saint*	saint = getNativeHandle(env, obj);
	saint->setOutput(pOut);
}


/*** org_tritonus_lowlevel_saint_Saint.cpp ***/
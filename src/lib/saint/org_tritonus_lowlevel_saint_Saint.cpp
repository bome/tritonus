/*
 *	org_tritonus_lowlevel_saint_Saint.cpp
*/

#include	"org_tritonus_lowlevel_saint_Saint.h"

/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    getChannelCount
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_saint_Saint_getChannelCount
(JNIEnv *env, jobject obj)
{
	return -1;
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
	return -1;
}

/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    init
 * Signature: (Ljava/io/InputStream;Ljava/io/InputStream;Ljava/io/OutputStream;I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_init__Ljava_io_InputStream_2Ljava_io_InputStream_2Ljava_io_OutputStream_2I
(JNIEnv *env, jobject obj, jobject orchestraStream, jobject scoreStream, jobject outputStream, jint nOutputFormat)
{
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    init
 * Signature: (Ljava/io/InputStream;Ljava/io/OutputStream;I)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_init__Ljava_io_InputStream_2Ljava_io_OutputStream_2I
(JNIEnv *env, jobject obj, jobject bitStream, jobject outputStream, jint nOutputFormat)
{
}



/*
 * Class:     org_tritonus_lowlevel_saint_Saint
 * Method:    run
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_saint_Saint_run
(JNIEnv *env, jobject obj)
{
}



/*** org_tritonus_lowlevel_saint_Saint.cpp ***/

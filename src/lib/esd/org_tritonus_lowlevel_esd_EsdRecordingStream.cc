/*
 *	org_tritonus_lowlevel_esd_EsdRecordingStream.cc
 */

#include	<assert.h>
#include	<stdio.h>
#include	<unistd.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_esd_EsdRecordingStream.h"


static HandleFieldHandler<int>	handler;



/*
 * Class:     org_tritonus_lowlevel_esd_EsdRecordingStream
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdRecordingStream_open
  (JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate)
{
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_open(): begin\n"); }
	int		nFd = esd_record_stream/*_fallback*/(nFormat, nSampleRate, NULL, "abc");
	if (nFd < 0)
	{
		// cerr << "cannot create esd stream\n";
		// jclass	cls = env->FindClass("org/tritonus_lowlevel/nas/DeviceAttributes");
		// env->ThrowNew(cls, "exc: cannot create esd stream");
	}
	handler.setHandle(env, obj, nFd);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_open(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdRecordingStream
 * Method:    read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read
(JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read(): begin\n"); }
	// int		i;
	int		nFd = handler.getHandle(env, obj);
	// signed char*	data;
	int		nBytesRead;
	// TODO: variable-sized arrays are not allowed in ANSI C
	jbyte		buffer[nLength];
	// data = env->GetByteArrayElements(abData, NULL);
	// TODO: check data for != NULL
	nBytesRead = read(nFd, buffer, nLength);
	// nRead = read(nFd, data + nOffset, nLength);
/*
	printf("read: %d\n", nRead);
	for (i = 0; i < nRead; i++)
	{
		printf("%d ", buffer[i]);
	}
	printf("\n");
*/
	// env->ReleaseByteArrayElements(abData, data, 0);
	if (nBytesRead > 0)
	{
		env->SetByteArrayRegion(abData, nOffset, nBytesRead, buffer);
	}
	if (debug_flag)
	{
		printf("Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read: Read: %d\n", nBytesRead);
	}
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read(): end\n"); }
	return nBytesRead;
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdRecordingStream
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdRecordingStream_close
(JNIEnv *env, jobject obj)
{
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_close(): begin\n"); }
	int		nFd = handler.getHandle(env, obj);
	close(nFd);
	handler.setHandle(env, obj, -1);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdRecordingStream_close(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdRecordingStream_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}


/*** org_tritonus_lowlevel_esd_EsdRecordingStream.cc ***/

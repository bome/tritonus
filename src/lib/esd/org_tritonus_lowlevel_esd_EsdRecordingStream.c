/*
 *	org_tritonus_lowlevel_esd_EsdRecordingStream.c
 */

#include	<assert.h>
#include	<stdio.h>
#include	<unistd.h>
#include	<esd.h>
#include	"org_tritonus_lowlevel_esd_EsdRecordingStream.h"


static int	DEBUG = 0;

// TODO: store in static variable
static int	getNativeFd(JNIEnv *env, jobject obj)
{
	jclass	cls = (*env)->GetObjectClass(env, obj);
	jfieldID	fid = (*env)->GetFieldID(env, cls, "m_lNativeFd", "J");
	return (*env)->GetIntField(env, obj, fid);
}


static void	setNativeFd(JNIEnv *env, jobject obj, int nFd)
{
	jclass	cls = (*env)->GetObjectClass(env, obj);
	jfieldID	fid = (*env)->GetFieldID(env, cls, "m_lNativeFd", "J");
	(*env)->SetIntField(env, obj, fid, nFd);
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
	int		nFd = getNativeFd(env, obj);
	close(nFd);
	setNativeFd(env, obj, -1);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdRecordingStream
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdRecordingStream_open
  (JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate)
{
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Format: " << nFormat << "\n";
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Sample Rate: " << nSampleRate << "\n";
	int		nFd = esd_record_stream/*_fallback*/(nFormat, nSampleRate, NULL, "abc");
	if (nFd < 0)
	{
		// cerr << "cannot create esd stream\n";
		// jclass	cls = (*env)->FindClass(env, "org/tritonus_lowlevel/nas/DeviceAttributes");
		// (*env)->ThrowNew(env, cls, "exc: cannot create esd stream");
	}
	setNativeFd(env, obj, nFd);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdRecordingStream
 * Method:    read
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read
  (JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	// int		i;
	int		nFd = getNativeFd(env, obj);
	// signed char*	data;
	int		nBytesRead;
	// TODO: variable-sized arrays are not allowed in ANSI C
	jbyte		buffer[nLength];
	// data = (*env)->GetByteArrayElements(env, abData, NULL);
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
	// (*env)->ReleaseByteArrayElements(env, abData, data, 0);
	if (nBytesRead > 0)
	{
		(*env)->SetByteArrayRegion(env, abData, nOffset, nBytesRead, buffer);
	}
	if (DEBUG)
	{
		printf("Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_esd_EsdRecordingStream_read: Read: %d\n", nBytesRead);
	}
	return nBytesRead;
}



/*** org_tritonus_lowlevel_esd_EsdRecordingStream.c ***/

/* 
 *	org_tritonus_lowlevel_esd_EsdSample.c
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


#include	<assert.h>
#include	<unistd.h>
#include	<errno.h>
#include	<esd.h>
#include	"org_tritonus_lowlevel_esd_EsdSample.h"



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


static int	getNativeId(JNIEnv *env, jobject obj)
{
	jclass	cls = (*env)->GetObjectClass(env, obj);
	jfieldID	fid = (*env)->GetFieldID(env, cls, "m_lNativeId", "J");
	return (int) (*env)->GetLongField(env, obj, fid);
}


static void	setNativeId(JNIEnv *env, jobject obj, int nId)
{
	jclass	cls = (*env)->GetObjectClass(env, obj);
	jfieldID	fid = (*env)->GetFieldID(env, cls, "m_lNativeId", "J");
	(*env)->SetLongField(env, obj, fid, nId);
}





/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_close
  (JNIEnv *env, jobject obj)
{
	int		nFd = getNativeFd(env, obj);
	close(nFd);
	setNativeFd(env, obj, -1);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_free
  (JNIEnv *env, jobject obj)
{
	int		nFd = getNativeFd(env, obj);
	int		nId = getNativeId(env, obj);
	esd_sample_free(nFd, nId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    kill
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_kill
  (JNIEnv *env, jobject obj)
{
	// int		nFd = getNativeFd(env, obj);
	// int		nId = getNativeId(env, obj);
	// results in undefined symbol
	// esd_sample_kill(nFd, nId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    loop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_loop
  (JNIEnv *env, jobject obj)
{
	// cout << "hallo2\n";
	int		nFd = getNativeFd(env, obj);
	int		nId = getNativeId(env, obj);
	// cout << "naticeId: " << nId << "\n";
	esd_sample_loop(nFd, nId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_open
  (JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate, jint nLength)
{
	// cout << "EsdSample.open(): Format: " << nFormat << "\n";
	// cout << "EsdSample.open(): Length: " << nLength << "\n";
	// cout << "EsdSample.open(): Sample Rate: " << nSampleRate << "\n";
	int		nId = 0;
	int		nFd = esd_open_sound(NULL);
	if (nFd < 0)
	{
		// cout << "EsdSample.open(): cannot create esd sample\n";
		// jclass	cls = (*env)->FindClass(env, "org/tritonus_lowlevel/nas/DeviceAttributes");
		// (*env)->ThrowNew(env, cls, "exc: cannot create esd stream");
	}
	setNativeFd(env, obj, nFd);
	nId = esd_sample_cache(nFd, nFormat, nSampleRate, nLength, "def");
	// cout << "EsdSample.open(): sample id: " << nId << "\n";
	setNativeId(env, obj, nId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    play
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_play
  (JNIEnv *env, jobject obj)
{
	int		nFd = getNativeFd(env, obj);
	int		nId = getNativeId(env, obj);
	esd_sample_play(nFd, nId);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    setVolume
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_setVolume
  (JNIEnv *env, jobject obj, jint nLeftValue, jint nRightValue)
{
	int		nFd = getNativeFd(env, obj);
	int		nId = getNativeId(env, obj);
	esd_set_default_sample_pan(nFd, nId, nLeftValue, nRightValue);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    stop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_stop
  (JNIEnv *env, jobject obj)
{
	int		nFd = getNativeFd(env, obj);
	int		nId = getNativeId(env, obj);
	esd_sample_stop(nFd, nId);
}





/*
 * Class:     org_tritonus_lowlevel_esd_EsdSample
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_esd_EsdSample_write
  (JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	// cout << "EsdSample.write(): Length: " << nLength << "\n";
	int		nFd = getNativeFd(env, obj);
	signed char*	data = (*env)->GetByteArrayElements(env, abData, NULL);
	int		nWritten = 0;
	errno = 0;
	nWritten = write(nFd, data + nOffset, nLength);
	(*env)->ReleaseByteArrayElements(env, abData, data, JNI_ABORT);
	// cout << "EsdSample.write(): Written: " << nWritten << "\n";
	if (nWritten < 0)
	{
		// cout << "EsdSample.write(): error: " << errno << "\n";
		perror("E:");
	}
	return nWritten;
}



/*** org_tritonus_lowlevel_esd_EsdSample.c ***/

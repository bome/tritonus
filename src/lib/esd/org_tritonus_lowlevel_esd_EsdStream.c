/* 
 *	org_tritonus_lowlevel_esd_EsdStream.c
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
#include	<stdio.h>
#include	<unistd.h>
#include	<esd.h>
#include	"org_tritonus_lowlevel_esd_EsdStream.h"


static int	DEBUG = 0;


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
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdStream_close
  (JNIEnv *env, jobject obj)
{
	int		nFd = getNativeFd(env, obj);
	close(nFd);
	setNativeFd(env, obj, -1);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_esd_EsdStream_open
  (JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate)
{
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Format: " << nFormat << "\n";
	// cerr << "Java_org_tritonus_lowlevel_esd_EsdStream_write: Sample Rate: " << nSampleRate << "\n";
	int		nFd = esd_play_stream/*_fallback*/(nFormat, nSampleRate, NULL, "abc");
	if (nFd < 0)
	{
		// cerr << "cannot create esd stream\n";
		// jclass	cls = (*env)->FindClass(env, "org/tritonus_lowlevel/nas/DeviceAttributes");
		// (*env)->ThrowNew(env, cls, "exc: cannot create esd stream");
	}
	setNativeFd(env, obj, nFd);
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_esd_EsdStream_write
  (JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	int		nFd = getNativeFd(env, obj);
	signed char*	data = (*env)->GetByteArrayElements(env, abData, NULL);
	int		nWritten = write(nFd, data + nOffset, nLength);
	(*env)->ReleaseByteArrayElements(env, abData, data, JNI_ABORT);
	if (DEBUG)
	{
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Written: %d\n", nWritten);
	}
	return nWritten;
}



/*** org_tritonus_lowlevel_esd_EsdStream.c ***/

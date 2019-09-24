/* 
 *	org_tritonus_lowlevel_esd_EsdStream.c
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer
 *
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

#include	<assert.h>
#include	<errno.h>
#include	<stdio.h>
#include	<unistd.h>
#include	"common.h"
#include	"org_tritonus_lowlevel_esd_EsdStream.h"

// TODO: remove, replace by debug_flag
//static int	debug_flag = 0;

HandleFieldHandlerDeclaration(handler, int)



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    open
 * Signature: (II)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_open
(JNIEnv *env, jobject obj, jint nFormat, jint nSampleRate)
{
	int		nFd = -1;
	char		name[20];

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_open(): begin\n"); }
	sprintf(name, "trit%d", (int) obj);	// DANGEROUS!!
	// printf("name: %s\n", name);
	errno = 0;
	//perror("abc");
	nFd = esd_play_stream/*_fallback*/(nFormat, nSampleRate, NULL, name);
	if (nFd < 0)
	{
		// TODO: error message
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_open: cannot create esd stream\n");
		perror("abc");
		// jclass	cls = env->FindClass("org/tritonus_lowlevel/nas/DeviceAttributes");
		// env->ThrowNew(cls, "exc: cannot create esd stream");
	}
	// printf("fd: %d\n", nFd);
	//perror("abc");
	setHandle(env, obj, nFd);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_open(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    write
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_write
(JNIEnv *env, jobject obj, jbyteArray abData, jint nOffset, jint nLength)
{
	int		nFd = -1;
	signed char*	data = NULL;
	int		nWritten = -1;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_write(): begin\n"); }
	nFd = getHandle(env, obj);
	data = (*env)->GetByteArrayElements(env, abData, NULL);
	nWritten = write(nFd, data + nOffset, nLength);
	(*env)->ReleaseByteArrayElements(env, abData, data, JNI_ABORT);
	if (debug_flag)
	{
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Length: %d\n", (int) nLength);
		printf("Java_org_tritonus_lowlevel_esd_EsdStream_write: Written: %d\n", nWritten);
	}
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_write(): end\n"); }
	return nWritten;
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    close
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_close
(JNIEnv *env, jobject obj)
{
	int		nFd = -1;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_close(): begin\n"); }
	nFd = getHandle(env, obj);
	close(nFd);
	setHandle(env, obj, -1);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_esd_EsdStream_close(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_esd_EsdStream
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_esd_EsdStream_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}


/*** org_tritonus_lowlevel_esd_EsdStream.c ***/

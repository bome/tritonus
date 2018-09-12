/*
 *	org_tritonus_lowlevel_ogg_SyncState.c
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

#include "common.h"
#include "org_tritonus_lowlevel_ogg_SyncState.h"



HandleFieldHandlerDeclaration(handler, ogg_sync_state*)

ogg_page*
getPageNativeHandle(JNIEnv *env, jobject obj);


/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_malloc
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_malloc(): begin\n"); }
	handle = malloc(sizeof(ogg_sync_state));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_free
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    init
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_init
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_init(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_sync_init(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_init(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_clear
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_clear(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_sync_clear(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_clear(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    reset
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_reset
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_reset(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_sync_reset(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_reset(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_destroy
(JNIEnv* env, jobject obj)
{
	ogg_sync_state*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_destroy(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_sync_destroy(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_destroy(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    write
 * Signature: ([BI)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_write
(JNIEnv* env, jobject obj, jbyteArray abBuffer, jint nBytes)
{
	ogg_sync_state*	handle;
	char*			buffer;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_write(): begin\n"); }
	handle = getHandle(env, obj);
	buffer = ogg_sync_buffer(handle, nBytes);
	(*env)->GetByteArrayRegion(env, abBuffer,
				   0, nBytes, (jbyte*) buffer);
	nReturn = ogg_sync_wrote(handle, nBytes);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_write(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    pageseek
 * Signature: (Lorg/tritonus/lowlevel/ogg/Page;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_pageseek
(JNIEnv* env, jobject obj, jobject page)
{
	ogg_sync_state*	handle;
	ogg_page*		pageHandle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_pageseek(): begin\n"); }
	handle = getHandle(env, obj);
	pageHandle = getPageNativeHandle(env, page);
	nReturn = ogg_sync_pageseek(handle, pageHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_pageseek(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    pageOut
 * Signature: (Lorg/tritonus/lowlevel/ogg/Page;)I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_pageOut
(JNIEnv* env, jobject obj, jobject page)
{
	ogg_sync_state*	handle;
	ogg_page*		pageHandle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_pageOut(): begin\n"); }
	handle = getHandle(env, obj);
	pageHandle = getPageNativeHandle(env, page);
	nReturn = ogg_sync_pageout(handle, pageHandle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_SyncState_pageOut(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_SyncState
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_SyncState_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_ogg_SyncState.c ***/

/*
 *	org_tritonus_lowlevel_ogg_Packet.c
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
#include "org_tritonus_lowlevel_ogg_Packet.h"

HandleFieldHandlerDeclaration(handler, ogg_packet*)


ogg_packet*
getPacketNativeHandle(JNIEnv *env, jobject obj)
{
        return getHandle(env, obj);
}
 

/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    malloc
 * Signature: ()I
 */
JNIEXPORT jint JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_malloc
(JNIEnv* env, jobject obj)
{
	ogg_packet*		handle;
	int			nReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_malloc(): begin\n"); }
	handle = malloc(sizeof(ogg_packet));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_malloc(): handle: %p\n", handle); }
	setHandle(env, obj, handle);
	nReturn = (handle == NULL) ? -1 : 0;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_malloc(): end\n"); }
	return nReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    free
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_free
(JNIEnv* env, jobject obj)
{
	ogg_packet*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_free(): begin\n"); }
	handle = getHandle(env, obj);
	free(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_free(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    clear
 * Signature: ()V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_clear
(JNIEnv* env, jobject obj)
{
	ogg_packet*	handle;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_clear(): begin\n"); }
	handle = getHandle(env, obj);
	ogg_packet_clear(handle);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_clear(): end\n"); }
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    getData
 * Signature: ()[B
 */
JNIEXPORT jbyteArray JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_getData
(JNIEnv* env, jobject obj)
{
	ogg_packet*	handle;
	jbyteArray	abData;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_getData(): begin\n"); }
	handle = getHandle(env, obj);
	abData = (*env)->NewByteArray(env, handle->bytes);
	(*env)->SetByteArrayRegion(env, abData, 0, handle->bytes,
								(jbyte*) (handle->packet));
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_getData(): end\n"); }
	return abData;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    isBos
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_isBos
(JNIEnv* env, jobject obj)
{
	ogg_packet*	handle;
	jboolean	bReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_isBos(): begin\n"); }
	handle = getHandle(env, obj);
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_isBos(): b_o_s: %d\n", (int) handle->b_o_s); }
	bReturn = (handle->b_o_s != 0) ? JNI_TRUE : JNI_FALSE;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_isBos(): end\n"); }
	return bReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    isEos
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_isEos
(JNIEnv* env, jobject obj)
{
	ogg_packet*	handle;
	jboolean	bReturn;

	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_isEos(): begin\n"); }
	handle = getHandle(env, obj);
	bReturn = (handle->e_o_s != 0) ? JNI_TRUE : JNI_FALSE;
	if (debug_flag) { fprintf(debug_file, "Java_org_tritonus_lowlevel_ogg_Packet_isEos(): end\n"); }
	return bReturn;
}



/*
 * Class:     org_tritonus_lowlevel_ogg_Packet
 * Method:    setTrace
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL
Java_org_tritonus_lowlevel_ogg_Packet_setTrace
(JNIEnv* env, jclass cls, jboolean bTrace)
{
	debug_flag = bTrace;
	debug_file = stderr;
}



/*** org_tritonus_lowlevel_ogg_Packet.c ***/

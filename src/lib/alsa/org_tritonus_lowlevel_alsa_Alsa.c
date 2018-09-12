/*
 *	org_tritonus_lowlevel_alsa_Alsa.cc
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

#include "common.h"
#include "org_tritonus_lowlevel_alsa_Alsa.h"



/*
 * Class:     org_tritonus_lowlevel_alsa_Alsa
 * Method:    getStringError
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL
Java_org_tritonus_lowlevel_alsa_Alsa_getStringError
(JNIEnv *env, jclass cls, jint nErrnum)
{
	jstring	strError;
	const char*	err;

	if (debug_flag) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_Alsa_getStringError(): begin\n"); }
	err = snd_strerror(nErrnum);
	if (err == NULL)
	{
		throwRuntimeException(env, "snd_strerror() failed");
	}
	// strError = env->NewStringUTF(err);
	strError = (*env)->NewStringUTF(env, err);
	if (strError == NULL)
	{
		throwRuntimeException(env, "NewStringUTF() failed");
	}
	if (debug_flag) { (void) fprintf(debug_file, "Java_org_tritonus_lowlevel_alsa_Alsa_getStringError(): end\n"); }
	return strError;
}



/*** org_tritonus_lowlevel_alsa_Alsa.cc ***/

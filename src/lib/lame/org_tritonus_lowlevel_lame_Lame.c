/*
 *	org_tritonus_lowlevel_lame_Lame.c
 */


/*
 *  Copyright (c) 2000,2001 by Florian Bomers <florian@bome.com>
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


#include	<stdlib.h>

#include	"lame/lame.h"
#include	"org_tritonus_lowlevel_lame_Lame.h"

/*
 * Hack: as lame does not support byte swapping (?), add a flag to 
 * lame_global_flags.
 * "swapbyte hack"
 */

typedef struct tagLameConf {
	lame_global_flags* gf;
	int channels;
	int swapbytes;
} LameConf;


static int debug=0;


//todo: this is BUGGY ! seg fault...
static void throwRuntimeException(JNIEnv *env, char* pStrMessage) {
	static  jclass	runtimeExceptionClass = NULL;

	if (runtimeExceptionClass == NULL) {
		runtimeExceptionClass = (*env)->FindClass(env, "java/lang/RuntimeException");
		if (runtimeExceptionClass == NULL) {
			(*env)->FatalError(env, "cannot get class object for java.lang.RuntimeException");
		}
	}
	(*env)->ThrowNew(env, runtimeExceptionClass, pStrMessage);
}


static jfieldID getFieldID(JNIEnv *env, char* name, char* signature) {
	jfieldID result;
	jclass	cls = (*env)->FindClass(env, "org/tritonus/lowlevel/lame/Lame");
	if (cls == NULL) {
		throwRuntimeException(env, "cannot get class object for org.tritonus.lowlevel.lame.Lame");
	}
	result = (*env)->GetFieldID(env, cls, name, signature);
	if (result == NULL) {
		throwRuntimeException(env, "cannot get field ID in class Lame");
	}
	return result;
}

static jfieldID	nativeGlobalFlagsFieldID = NULL;

static jfieldID getNativeGlobalFlagsFieldID(JNIEnv *env) {
	if (nativeGlobalFlagsFieldID == NULL) {
		nativeGlobalFlagsFieldID = getFieldID(env, "m_lNativeGlobalFlags", "J");
	}
	return nativeGlobalFlagsFieldID;
}


static LameConf* getNativeGlobalFlags(JNIEnv *env, jobject obj) {
	jfieldID	fieldID = getNativeGlobalFlagsFieldID(env);
	return (LameConf*) ((unsigned int) (*env)->GetLongField(env, obj, fieldID));
}


static void setNativeGlobalFlags(JNIEnv *env, jobject obj, LameConf* flags) {
	jfieldID	fieldID = getNativeGlobalFlagsFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) ((unsigned int) flags));
}

static void setIntField(JNIEnv *env, jobject obj, char* name, int value) {
	jfieldID fieldID = getFieldID(env, name, "I");
	(*env)->SetIntField(env, obj, fieldID, (jint) value);
}
	

#define LA_ENDIAN_NOT_TESTED 0
#define LA_BIG_ENDIAN 1
#define LA_LITTLE_ENDIAN 2

static int platformEndianness=LA_ENDIAN_NOT_TESTED;

inline void CheckEndianness() {
	if (platformEndianness==LA_ENDIAN_NOT_TESTED) {
		int dummy=1;
		char* pDummy=(char*) (&dummy);
		if (*pDummy) {
			platformEndianness=LA_LITTLE_ENDIAN;
		} else {
			platformEndianness=LA_BIG_ENDIAN;
		}
	}
}


//////////////////////////////////////// native functions ////////////////////////////////////

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nInitParams
 * Signature: (IIIIIZZ)I
 * returns >=0 on success
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nInitParams
  (JNIEnv * env, jobject obj, jint channels, jint sampleRate, 
   jint bitrate, jint mode, jint quality, jboolean VBR, jboolean bigEndian) {
	jint result;
	LameConf* conf;
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_initParams: \n");
		printf("   %d channels, %d Hz, %d KBit/s, mode %d, quality=%d VBR=%d bigEndian=%d\n",
		       (int) channels, (int) sampleRate, (int) bitrate, 
		       (int) mode, (int) quality, (int) VBR, (int) bigEndian);
	}
	nativeGlobalFlagsFieldID = NULL;
	conf=(LameConf*) calloc(sizeof(LameConf),1);
	setNativeGlobalFlags(env, obj, conf);
	if (conf==NULL) {
		//throwRuntimeException(env, "out of memory");
		return org_tritonus_lowlevel_lame_Lame_OUT_OF_MEMORY;
	}
	conf->gf=lame_init();
	if (conf->gf==NULL) {
		//throwRuntimeException(env, "out of memory");
		return org_tritonus_lowlevel_lame_Lame_OUT_OF_MEMORY;
	}
	
	//set parameters
	lame_set_num_channels(conf->gf, (int) channels);
	conf->channels=channels;
	lame_set_in_samplerate(conf->gf, (int) sampleRate);
	/*if (mode!=org_tritonus_lowlevel_lame_Lame_CHANNEL_MODE_AUTO) {
		lame_set_mode(conf->gf, (int) mode);
	}*/
	if (VBR) {
		// not implemented yet
		//lame_set_VBR(conf->gf, vbr_default);
		//lame_set_VBR_q(conf->gf, (int) quality);
		conf->gf->VBR=vbr_default;
		conf->gf->VBR_q=(int) quality;
	} else {
		// not implemented yet
		//lame_set_brate(conf->gf, (int) bitrate);
		//conf->gf->brate=(int) bitrate;
	}
	lame_set_quality(conf->gf, (int) quality);
	CheckEndianness();
	if ((bigEndian && platformEndianness==LA_LITTLE_ENDIAN) ||
	    (!bigEndian && platformEndianness==LA_BIG_ENDIAN)) {
		// swap samples
		//gf->swapbytes=1;
		// swapbyte hack
		conf->swapbytes=1;
	}
	result=(jint) lame_init_params(conf->gf);

	// update the Lame instance with the effective values
	//setIntField(env, obj, "effQuality", VBR?lame_get_VBR_q(conf->gf):lame_get_quality(conf->gf));
	setIntField(env, obj, "effQuality", VBR?conf->gf->VBR_q:lame_get_quality(conf->gf));
	// not implemented yet in lame
	//setIntField(env, obj, "effBitRate", lame_get_brate(conf->gf));
	setIntField(env, obj, "effBitRate", conf->gf->brate);
	// not implemented yet in lame
	//setIntField(env, obj, "effVbr", lame_get_VBR(conf->gf));
	setIntField(env, obj, "effVbr", conf->gf->VBR);
	//	setIntField(env, obj, "effChMode", lame_get_mode(conf->gf));
	setIntField(env, obj, "effSampleRate", lame_get_out_samplerate(conf->gf));
	setIntField(env, obj, "effEncoding", lame_get_version(conf->gf));
	return result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nGetPCMBufferSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nGetPCMBufferSize
(JNIEnv *env, jobject obj, jint wishedBufferSize) {
	return wishedBufferSize;
}


typedef struct tagTwoChar {
    char a,b;
} twoChar;


/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nEncodeBuffer
 * Signature: ([BII[B)I
 *
 * returns result of lame_encode_buffer:
 * return code     number of bytes output in mp3buf. Can be 0 
 *                 -1:  mp3buf was too small
 *                 -2:  malloc() problem
 *                 -3:  lame_init_params() not called
 *                 -4:  psycho acoustic problems 
 *                 -5:  ogg cleanup encoding error
 *                 -6:  ogg frame encoding error
 * 
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nEncodeBuffer
(JNIEnv *env, jobject obj, jbyteArray pcm, jint offset, jint length, jbyteArray encoded) {
	LameConf* conf;
	int i; //hack
	twoChar* s; //hack
	int result=0;
	char* encodedBytes, *pcmSamplesOrig;
	short* pcmSamples;
	int pcmSamplesLength;
	//jsize pcmArrayByteSize=(*env)->GetArrayLength(env, pcm);
	// todo: consistency check for pcm array ?
	int encodedArrayByteSize=(int) ((*env)->GetArrayLength(env, encoded));
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeBuffer: \n");
		printf("   offset: %d, length:%d\n", (int) offset, (int) length);
		//printf("   %d bytes in PCM array\n", (int) pcmArrayByteSize);
		//printf("   %d bytes in to-be-encoded array\n", (int) encodedArrayByteSize);
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL && conf->gf!=NULL) {
		pcmSamplesLength=length/(conf->channels*2); // always 16 bit
		pcmSamplesOrig=(*env)->GetByteArrayElements(env, pcm, NULL);
		pcmSamples=(short*) pcmSamplesOrig;
		pcmSamples+=(offset/2); // 16bit
		// start swapbyte hack
		if (conf->swapbytes) {
			// swap bytes
			i=length/2;
			s=(twoChar*) pcmSamples;
			if (debug) {
				printf("Swapping samples.\n");
			}
			for (; i>0; i--, s++) {
				char help=s->a;
				s->a=s->b;
				s->b=help;
			}
		}
		// end swapbyte hack
		encodedBytes=(*env)->GetByteArrayElements(env, encoded, NULL);
		
		if (debug) {
			printf("   Encoding %d samples at %p into buffer %p of size %d bytes.\n", 
			       pcmSamplesLength, pcmSamples, encodedBytes, encodedArrayByteSize);
			printf("   Sample1=%d Sample2=%d\n", pcmSamples[0], pcmSamples[1]);
		}

		result=lame_encode_buffer_interleaved(conf->gf, pcmSamples, pcmSamplesLength, 
						      encodedBytes, encodedArrayByteSize);
		if (debug) {
			printf("   MP3-1=%d MP3-2=%d\n", (int) encodedBytes[0], (int) encodedBytes[1]);
		}
		// clean up:
		// discard any changes in pcmArray
		(*env)->ReleaseByteArrayElements(env, pcm, pcmSamplesOrig, JNI_ABORT);
		// commit the encoded bytes
		(*env)->ReleaseByteArrayElements(env, encoded, encodedBytes, 0);
	} else {
		if (debug) {
			printf("Java_org_tritonus_lowlevel_lame_Lame_nEncodeBuffer: \n");
			printf("   no global flags !");
		}
		//throwRuntimeException(env, "not initialized");
		return org_tritonus_lowlevel_lame_Lame_NOT_INITIALIZED;
	}
	return (jint) result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    **************************************** nEncodeFinish
 * Signature: ([B)I                                    /////////////
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nEncodeFinish
(JNIEnv *env, jobject obj, jbyteArray buffer) {
	int result=0;
	LameConf* conf;
	if (debug) {
		//jsize length=(*env)->GetArrayLength(env, buffer);
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		//printf("   %d bytes in the array\n", (int) length);
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL && conf->gf!=NULL) {
		jsize charBufferSize=(*env)->GetArrayLength(env, buffer);
		char* charBuffer=NULL;
		if (charBufferSize>0) {
			charBuffer=(*env)->GetByteArrayElements(env, buffer, NULL);
		}
		result=lame_encode_flush(conf->gf, charBuffer, charBufferSize);
		if (debug) {
			printf("   %d bytes returned\n", (int) result);
		}
		(*env)->ReleaseByteArrayElements(env, buffer, charBuffer, 0);
		lame_close(conf->gf);
		conf->gf=NULL;
		setNativeGlobalFlags(env, obj, 0);
		free(conf);
	} 
	else if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		printf("   no global flags !\n");
	}
	return (jint) result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    ************************************** nClose
 * Signature: ()V                                    //////
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_lame_Lame_nClose(JNIEnv * env, jobject obj) {
	LameConf* conf;

	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_close. \n");
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL) {
		if (conf->gf!=NULL) {
			lame_close(conf->gf);
		}
		setNativeGlobalFlags(env, obj, 0);
		free(conf);
	}
}

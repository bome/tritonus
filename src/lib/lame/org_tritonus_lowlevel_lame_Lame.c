/*
 *	org_tritonus_lowlevel_lame_Lame.c
 */


/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
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


#include	<errno.h>
#include	<string.h>
#include	<stdio.h>
#include	<stdlib.h>

#include	"lame.h"
#include	"org_tritonus_lowlevel_lame_Lame.h"

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


static lame_global_flags* getNativeGlobalFlags(JNIEnv *env, jobject obj) {
	jfieldID	fieldID = getNativeGlobalFlagsFieldID(env);
	return (lame_global_flags*) ((unsigned int) (*env)->GetLongField(env, obj, fieldID));
}


static void setNativeGlobalFlags(JNIEnv *env, jobject obj, lame_global_flags* flags) {
	jfieldID	fieldID = getNativeGlobalFlagsFieldID(env);
	(*env)->SetLongField(env, obj, fieldID, (jlong) ((unsigned int) flags));
}

static void setIntField(JNIEnv *env, jobject obj, char* name, int value) {
	jfieldID fieldID = getFieldID(env, name, "I");
	(*env)->SetIntField(env, obj, fieldID, (jint) value);
}
	

int doEncodeFinish(JNIEnv* env, jobject obj, char* buffer, int bufferSize) {
	lame_global_flags* gf;
	int result=0;
	gf=getNativeGlobalFlags(env, obj);
	if (gf!=NULL) {
		result=lame_encode_finish(gf, buffer, bufferSize);
		free(gf);
		setNativeGlobalFlags(env, obj, 0);
	}
	return result;
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
 * Hack: as lame does not support byte swapping (?), add a flag to 
 * lame_global_flags.
 * "swapbyte hack"
 */

typedef struct tagSwapByteGlobalFlags {
	lame_global_flags flags;
	int swapbytes;
} SwapByteGlobalFlags;


/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nInitParams
 * Signature: (IIIIIZZ)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nInitParams
  (JNIEnv * env, jobject obj, jint channels, jint sampleRate, 
   jint bitrate, jint mode, jint quality, jboolean VBR, jboolean bigEndian) {
	jint result;
	SwapByteGlobalFlags* sb;
	lame_global_flags* gf;
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_initParams: \n");
		printf("   %d channels, %d Hz, %d KBit/s, mode %d, quality=%d VBR=%d bigEndian=%d\n",
		       (int) channels, (int) sampleRate, (int) bitrate, 
		       (int) mode, (int) quality, (int) VBR, (int) bigEndian);
	}
	nativeGlobalFlagsFieldID = NULL;
	// swapbyte hack
	//gf=(lame_global_flags*) calloc(sizeof(lame_global_flags),1);
	sb=(SwapByteGlobalFlags*) calloc(sizeof(SwapByteGlobalFlags),1);
	gf=(lame_global_flags*) sb;
	setNativeGlobalFlags(env, obj, gf);
	if (gf==NULL) {
		//throwRuntimeException(env, "out of memory");
		return org_tritonus_lowlevel_lame_Lame_OUT_OF_MEMORY;
	}
	if (lame_init(gf)!=0) {
		//throwRuntimeException(env, "out of memory");
		return org_tritonus_lowlevel_lame_Lame_OUT_OF_MEMORY;
	}
	
	//set gf-fields
	gf->num_channels=(int) channels;
	gf->in_samplerate = (int) sampleRate;
	if (mode==org_tritonus_lowlevel_lame_Lame_CHANNEL_MODE_AUTO) {
		if (channels==1) {
			gf->mode = org_tritonus_lowlevel_lame_Lame_CHANNEL_MODE_MONO;
		} else {
			gf->mode = org_tritonus_lowlevel_lame_Lame_CHANNEL_MODE_JOINT_STEREO;
		}
	} else {
		gf->mode = (int) mode;
	}
	if (VBR) {
		gf->VBR=vbr_default;
		gf->VBR_q=(int) quality;
	} else {
		gf->brate = (int) bitrate;
	}
	gf->quality = (int) quality;
	CheckEndianness();
	if ((bigEndian && platformEndianness==LA_LITTLE_ENDIAN) ||
	    (!bigEndian && platformEndianness==LA_BIG_ENDIAN)) {
		// swap samples
		//gf->swapbytes=1;
		// swapbyte hack
		sb->swapbytes=1;
	}
	result=(jint) lame_init_params(gf);

	// update the Lame instance with the effective values
	setIntField(env, obj, "effQuality", gf->VBR?gf->VBR_q:gf->quality);
	setIntField(env, obj, "effBitRate", gf->brate);
	setIntField(env, obj, "effVbr", gf->VBR);
	setIntField(env, obj, "effChMode", gf->mode);
	setIntField(env, obj, "effSampleRate", gf->out_samplerate);
	setIntField(env, obj, "effEncoding", gf->version);
	return result;
}

typedef struct tagTwoChar {
    char a,b;
} twoChar;


/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nEncodeBuffer
 * Signature: ([BII[B)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nEncodeBuffer
(JNIEnv *env, jobject obj, jbyteArray pcm, jint offset, jint length, jbyteArray encoded) {
	lame_global_flags* gf;
	int i; //hack
	twoChar* s; //hack
	int result=0;
	char* encodedBytes;
	short* pcmSamples;
	int pcmSamplesLength;
	//jsize pcmArrayByteSize=(*env)->GetArrayLength(env, pcm);
	// todo: consistency check for pcm array ?
	jsize encodedArrayByteSize=(*env)->GetArrayLength(env, encoded);
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeBuffer: \n");
		printf("   offset: %d, length:%d\n", (int) offset, (int) length);
		//printf("   %d bytes in PCM array\n", (int) pcmArrayByteSize);
		//printf("   %d bytes in to-be-encoded array\n", (int) encodedArrayByteSize);
	}
	gf=getNativeGlobalFlags(env, obj);
	if (gf!=NULL) {
		pcmSamplesLength=length/(gf->num_channels*2); // always 16 bit
		pcmSamples=(short*) ((*env)->GetByteArrayElements(env, pcm, NULL));
		pcmSamples+=(offset/2); // 16bit
		// start swapbyte hack
		if (((SwapByteGlobalFlags*) gf)->swapbytes) {
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
		result=lame_encode_buffer_interleaved(gf, pcmSamples, pcmSamplesLength, 
						      encodedBytes, encodedArrayByteSize);
	} else {
		if (debug) {
			printf("Java_org_tritonus_lowlevel_lame_Lame_nEncodeBuffer: \n");
			printf("   no global flags !");
		}
		//throwRuntimeException(env, "not initialized");
		return org_tritonus_lowlevel_lame_Lame_NOT_INITIALIZED;
	}
	return result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    **************************************** encodeFinish
 * Signature: ([B)I                                    ////////////
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_encodeFinish
(JNIEnv *env, jobject obj, jbyteArray buffer) {
	int result=0;
	lame_global_flags* gf;
	if (debug) {
		//jsize length=(*env)->GetArrayLength(env, buffer);
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		//printf("   %d bytes in the array\n", (int) length);
	}
	gf=getNativeGlobalFlags(env, obj);
	if (gf!=NULL) {
		jsize charBufferSize=(*env)->GetArrayLength(env, buffer);
		char* charBuffer=NULL;
		if (charBufferSize>0) {
			charBuffer=(*env)->GetByteArrayElements(env, buffer, NULL);
		}
		result=doEncodeFinish(env, obj, charBuffer, charBufferSize);
		if (debug) {
			printf("   %d bytes returned\n", (int) result);
		}
	} 
	else if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		printf("   no global flags !\n");
	}
	return (jint) result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    ************************************** close
 * Signature: ()V                                    //////
 */
JNIEXPORT void JNICALL Java_org_tritonus_lowlevel_lame_Lame_close(JNIEnv * env, jobject obj) {
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_close. \n");
	}
	doEncodeFinish(env, obj, NULL, 0);
}





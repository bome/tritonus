/*
 *	org_tritonus_lowlevel_lame_Lame.c
 *  for Windows bladenc-style DLLs
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


#include	<windows.h>
#include	<stdlib.h>

#include	"BladeMP3EncDLL.h"
#include	"org_tritonus_lowlevel_lame_Lame.h"

#define DLL_TYPE_LAME 1
#define DLL_TYPE_BLADENC 2

typedef struct tagLameConf {
	BE_CONFIG beConfig;
	HBE_STREAM hbeStream;
	int dllType;
	DWORD PCMBufferSize;
	DWORD MP3BufferSize;
	int channels;
	int swapbytes;
} LameConf;

#ifdef _DEBUG
static int debug=1;
#else
static int debug=0;
#endif

#ifndef _BLADEDLL
BEINITSTREAM		beInitStream=NULL;
BEENCODECHUNK		beEncodeChunk=NULL;
BEDEINITSTREAM		beDeinitStream=NULL;
BECLOSESTREAM		beCloseStream=NULL;
BEVERSION			beVersion=NULL;
BEWRITEVBRHEADER	beWriteVBRHeader=NULL;
HINSTANCE	hDLL=NULL;

// returns 0 on error / or one of DLL_TYPE_xx constants
int loadLameLibrary(const char* name) {
	if (hDLL==NULL) {
		hDLL=LoadLibrary(name);
		if(hDLL==NULL) {
			if (debug) {
				printf("Unable to load bladenc library\n");
				fflush(stdout);
			}
			return 0;
		}
	}
	if(!beInitStream || !beEncodeChunk || !beDeinitStream || !beCloseStream || !beVersion) {
		// Get Interface functions
		beInitStream	= (BEINITSTREAM) GetProcAddress(hDLL, TEXT_BEINITSTREAM);
		beEncodeChunk	= (BEENCODECHUNK) GetProcAddress(hDLL, TEXT_BEENCODECHUNK);
		beDeinitStream	= (BEDEINITSTREAM) GetProcAddress(hDLL, TEXT_BEDEINITSTREAM);
		beCloseStream	= (BECLOSESTREAM) GetProcAddress(hDLL, TEXT_BECLOSESTREAM);
		beVersion		= (BEVERSION) GetProcAddress(hDLL, TEXT_BEVERSION);
		beWriteVBRHeader= (BEWRITEVBRHEADER) GetProcAddress(hDLL,TEXT_BEWRITEVBRHEADER);
	}

	// Check if all interfaces are present
	if(!beInitStream || !beEncodeChunk || !beDeinitStream || !beCloseStream || !beVersion) {
		if (debug) {
			printf("Unable to get bladenc functions from dll\n");
			fflush(stdout);
		}
		return 0;
	}
	if (beWriteVBRHeader) {
		if (debug) {
			printf("DLL ist lame.\n");
			fflush(stdout);
		}
		return DLL_TYPE_LAME;
	}
	if (debug) {
		printf("DLL ist bladenc.\n");
		fflush(stdout);
	}
	return DLL_TYPE_BLADENC;
}
#endif


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
	

//////////////////////////////////////// native functions ////////////////////////////////////

int getMpegVersion(int bitrate) {
	switch (bitrate) {
		case 32:
		case 40:
		case 48:
		case 56:
		case 64:
		case 80:
		case 96:
		case 112:
		case 128:
		case 160:
		case 192:
		case 224:
		case 256:
		case 320: return MPEG1;
		//case 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160: return MPEG2;
	}
	return MPEG2;
}

int getLameMpegVersion(int version) {
	// Lame: 0=MPEG-2  1=MPEG-1  (2=MPEG-2.5)
	if (version==MPEG2) {
		return 0;
	}
	return 1;
}

int getMpegMode(int channels, int mode, int includeJointStereo) {
	if (channels==1) {
		return BE_MP3_MODE_MONO;
	}
	if (includeJointStereo) {
		return BE_MP3_MODE_JSTEREO;
	}
	return BE_MP3_MODE_STEREO;
}

int getLameMpegMode(int bladencMode) {
	//Lame: STEREO=0, JOINT_STEREO=1, DUAL_CHANNEL=2, MONO=3

	switch (bladencMode) {
		case BE_MP3_MODE_STEREO: return 0;
		case BE_MP3_MODE_JSTEREO: return 1;
		case BE_MP3_MODE_DUALCHANNEL: return 2;
		case BE_MP3_MODE_MONO: return 3;
	}
	return -1;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nInitParams
 * Signature: (IIIIIZZ)I
 * returns >=0 on success
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nInitParams
  (JNIEnv * env, jobject obj, jint channels, jint sampleRate, 
   jint bitrate, jint mode, jint quality, jboolean VBR, jboolean bigEndian) {
	LameConf* conf;
	int initDone;
	BE_ERR err=0;
	
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_initParams: \n");
		printf("   %d channels, %d Hz, %d KBit/s, mode %d, quality=%d VBR=%d bigEndian=%d\n",
		       (int) channels, (int) sampleRate, (int) bitrate, 
		       (int) mode, (int) quality, (int) VBR, (int) bigEndian);
		fflush(stdout);
	}
	nativeGlobalFlagsFieldID = NULL;
	conf=(LameConf*) calloc(sizeof(LameConf),1);
	if (conf==NULL) {
		//throwRuntimeException(env, "out of memory");
		return org_tritonus_lowlevel_lame_Lame_OUT_OF_MEMORY;
	}
#ifndef _BLADEDLL
	conf->dllType=loadLameLibrary("lame_enc.dll");
	if (!conf->dllType) {
		free(conf);
		return org_tritonus_lowlevel_lame_Lame_LAME_ENC_NOT_FOUND;
	}
#else
	// first try with LAME style config
	conf->dllType=DLL_TYPE_LAME;
#endif		
	conf->channels=channels;
	initDone=0;
	//TODO: respect quality setting
	while (!initDone) {
		if (conf->dllType==DLL_TYPE_LAME) {
			// fill lame-style fields
			conf->beConfig.dwConfig = BE_CONFIG_LAME;
			conf->beConfig.format.LHV1.dwStructVersion	= CURRENT_STRUCT_VERSION;
			conf->beConfig.format.LHV1.dwStructSize		= CURRENT_STRUCT_SIZE;
			conf->beConfig.format.LHV1.dwSampleRate		= sampleRate;				// INPUT FREQUENCY
			conf->beConfig.format.LHV1.dwReSampleRate		= 0;					// DON"T RESAMPLE
			conf->beConfig.format.LHV1.nMode				= getMpegMode(channels, mode, 1);
			conf->beConfig.format.LHV1.dwBitrate			= bitrate;					// MINIMUM BIT RATE
			conf->beConfig.format.LHV1.nPreset			= LQP_HIGH_QUALITY;		// QUALITY PRESET SETTING
			conf->beConfig.format.LHV1.dwMpegVersion		= getMpegVersion(bitrate);				// MPEG VERSION (I or II)
			if (VBR) {
				conf->beConfig.format.LHV1.bEnableVBR			= TRUE;					// USE VBR
				conf->beConfig.format.LHV1.nVBRQuality		= quality;				// SET VBR QUALITY
			}
		} else {
			// fill bladenc-style fields
			conf->beConfig.dwConfig = BE_CONFIG_MP3;
			conf->beConfig.format.mp3.dwSampleRate=sampleRate;	// 48000, 44100 and 32000 allowed
			conf->beConfig.format.mp3.byMode=getMpegMode(channels, mode, 0);	// BE_MP3_MODE_STEREO, BE_MP3_MODE_DUALCHANNEL, BE_MP3_MODE_MONO
			conf->beConfig.format.mp3.wBitrate=(WORD) bitrate;		// 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256 and 320 allowed
		}
		// Init the MP3 Stream
		err = beInitStream(&(conf->beConfig), 
				&(conf->PCMBufferSize), 
				&(conf->MP3BufferSize),
				&(conf->hbeStream));
	
		// Check result
		if(err != BE_ERR_SUCCESSFUL) {
			if (conf->dllType==DLL_TYPE_LAME) {
				// retry with bladenc-style
				conf->dllType=DLL_TYPE_BLADENC;
			} else {
				if (debug) {
					printf("Error opening encoding stream (%lu)\n", err);
					fflush(stdout);
				}
				free(conf);
				// TODO: better error return
				return -1;
			}
		} else {
			initDone=1;
		}
	}
	
	
	if (bigEndian) {
		conf->swapbytes=1;
	}

	// update the Lame instance with the effective values
	if (conf->dllType==DLL_TYPE_LAME) {
		if (debug) {
			printf("using LAME dll format\n");
			fflush(stdout);
		}
		setIntField(env, obj, "effBitRate", conf->beConfig.format.LHV1.dwBitrate);
		setIntField(env, obj, "effVbr", conf->beConfig.format.LHV1.bEnableVBR);
		setIntField(env, obj, "effChMode", getLameMpegMode(conf->beConfig.format.LHV1.nMode));
		setIntField(env, obj, "effSampleRate", conf->beConfig.format.LHV1.dwReSampleRate);
		setIntField(env, obj, "effEncoding", getLameMpegVersion(conf->beConfig.format.LHV1.dwMpegVersion));
	} else {
		if (debug) {
			printf("using BLADENC dll format\n");
			fflush(stdout);
		}
		setIntField(env, obj, "effBitRate", conf->beConfig.format.mp3.wBitrate);
		setIntField(env, obj, "effVbr", 0);
		setIntField(env, obj, "effChMode", getLameMpegMode(conf->beConfig.format.mp3.byMode));
		setIntField(env, obj, "effSampleRate", conf->beConfig.format.mp3.dwSampleRate);
		setIntField(env, obj, "effEncoding", getLameMpegVersion(MPEG1));
	}		
	setNativeGlobalFlags(env, obj, conf);
	return 0;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    nGetPCMBufferSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nGetPCMBufferSize
  (JNIEnv *env, jobject obj, jint wishedBufferSize) {
    int result=(int) wishedBufferSize;
	LameConf* conf;
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL) {
		if ((result % conf->PCMBufferSize)!=0) {
			result=(result/conf->PCMBufferSize)*conf->PCMBufferSize;
			if (result==0) {
				result=conf->PCMBufferSize;
			}
		}
	} else {
		//throwRuntimeException(env, "not initialized");
		return org_tritonus_lowlevel_lame_Lame_NOT_INITIALIZED;
	}
	return result;
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
	int i; 
	twoChar* s;
	int result=0;
	char* encodedBytes, *encodedBytesOrig, *pcmSamplesOrig;
	short* pcmSamples;
	int pcmSamplesLength;

	DWORD pcmSize;
	DWORD mp3Size;

	BE_ERR err=0;

	//jsize pcmArrayByteSize=(*env)->GetArrayLength(env, pcm);
	// todo: consistency check for pcm array ?
	int encodedArrayByteSize=(int) ((*env)->GetArrayLength(env, encoded));
	if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeBuffer: \n");
		printf("   offset: %d, length:%d\n", (int) offset, (int) length);
		//printf("   %d bytes in PCM array\n", (int) pcmArrayByteSize);
		//printf("   %d bytes in to-be-encoded array\n", (int) encodedArrayByteSize);
		fflush(stdout);
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL) {
		// pcmSamplesLength is here the number of shorts !
		pcmSamplesLength=length/2; // always 16 bit
		pcmSamplesOrig=(*env)->GetByteArrayElements(env, pcm, NULL);
		pcmSamples=(short*) pcmSamplesOrig;
		pcmSamples+=(offset/2); // 16bit. offset is in bytes
		if (conf->swapbytes) {
			// swap bytes
			i=length/2;
			s=(twoChar*) pcmSamples;
			if (debug) {
				printf("Swapping samples.\n");
				fflush(stdout);
			}
			for (; i>0; i--, s++) {
				char help=s->a;
				s->a=s->b;
				s->b=help;
			}
		}
		encodedBytesOrig=(*env)->GetByteArrayElements(env, encoded, NULL);
		encodedBytes=encodedBytesOrig;
		
		// bladenc prefers a fixed buffer size. if necessary, call bladenc several times
		while (pcmSamplesLength>0) {
			pcmSize=pcmSamplesLength;
			if (pcmSize>conf->PCMBufferSize) {
				pcmSize=conf->PCMBufferSize;
			}
			mp3Size=encodedArrayByteSize;
			if (debug) {
				printf("   Encoding %d samples at %p into buffer %p of size %d bytes.\n", 
				       pcmSize, pcmSamples, encodedBytes, mp3Size);
				printf("   Sample1=%d Sample2=%d\n", pcmSamples[0], pcmSamples[1]);
				fflush(stdout);
			}
	
			err = beEncodeChunk(conf->hbeStream, pcmSize, pcmSamples, encodedBytes, &mp3Size);
	
			// Check result
			if(err != BE_ERR_SUCCESSFUL) {
				result=-10;
				break;
			} else {
				if (debug) {
					printf("   encoded %d bytes. MP3-1=%d MP3-2=%d\n", (int) mp3Size, (int) encodedBytes[0], (int) encodedBytes[1]);
					fflush(stdout);
				}
				result+=mp3Size;
				pcmSamplesLength-=pcmSize;
				pcmSamples+=pcmSize;
				encodedBytes+=mp3Size;
				encodedArrayByteSize-=mp3Size;
			}
		}
		// clean up:
		// discard any changes in pcmArray
		(*env)->ReleaseByteArrayElements(env, pcm, pcmSamplesOrig, JNI_ABORT);
		// commit the encoded bytes
		(*env)->ReleaseByteArrayElements(env, encoded, encodedBytesOrig, 0);
	} else {
		if (debug) {
			printf("Java_org_tritonus_lowlevel_lame_Lame_nEncodeBuffer: \n");
			printf("   no global flags !\n");
			fflush(stdout);
		}
		//throwRuntimeException(env, "not initialized");
		return org_tritonus_lowlevel_lame_Lame_NOT_INITIALIZED;
	}
	if (debug) {
		printf("   returning %d encoded MP3 bytes\n", result);
		fflush(stdout);
	}
	return (jint) result;
}

/*
 * Class:     org_tritonus_lowlevel_lame_Lame
 * Method:    **************************************** nEncodeFinish
 * Signature: ([B)I                                    ////////////
 */
JNIEXPORT jint JNICALL Java_org_tritonus_lowlevel_lame_Lame_nEncodeFinish
(JNIEnv *env, jobject obj, jbyteArray buffer) {
	int result=0;
	LameConf* conf;
	BE_ERR err=0;

	if (debug) {
		//jsize length=(*env)->GetArrayLength(env, buffer);
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		//printf("   %d bytes in the array\n", (int) length);
		fflush(stdout);
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL) {
		jsize charBufferSize=(*env)->GetArrayLength(env, buffer);
		char* charBuffer=NULL;
		if (charBufferSize>0) {
			charBuffer=(*env)->GetByteArrayElements(env, buffer, NULL);
		}
		result=charBufferSize;
		err = beDeinitStream(conf->hbeStream, charBuffer, &result);
		if(err != BE_ERR_SUCCESSFUL) {
			result=-1;
		}
		
		if (debug) {
			printf("   %d bytes returned\n", (int) result);
			fflush(stdout);
		}
		(*env)->ReleaseByteArrayElements(env, buffer, charBuffer, 0);
		
		beCloseStream(conf->hbeStream);
		setNativeGlobalFlags(env, obj, 0);
		free(conf);
	} 
	else if (debug) {
		printf("Java_org_tritonus_lowlevel_lame_Lame_encodeFinish: \n");
		printf("   no global flags !\n");
		fflush(stdout);
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
		fflush(stdout);
	}
	conf=getNativeGlobalFlags(env, obj);
	if (conf!=NULL) {
		beCloseStream(conf->hbeStream);
		setNativeGlobalFlags(env, obj, 0);
		free(conf);
	}
}





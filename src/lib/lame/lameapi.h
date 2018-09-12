/*
 *	lameapi.h
 */


/*
 *  Copyright (c) 2001 by Florian Bomers
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

#ifndef LAME_API_H_INCLUDED
#define LAME_API_H_INCLUDED

#include	<stdlib.h>
#include	<string.h>
#include	"org_tritonus_lowlevel_lame_Lame.h"
#include	<lame/lame.h>

typedef struct tagLameConf {
	int channels;
	int sampleRate;
	int bitrate;
	int mode;
	int quality;
	int VBR;
	int mpegVersion;
	int swapbytes;
	lame_global_flags* gf;
} LameConf;

extern int doInit(LameConf* conf);
extern int doGetPCMBufferSize(LameConf* conf, int wishedBufferSize);
extern int doEncode(LameConf* conf, short* pcmSamples, int pcmLengthInFrames, char* encodedBytes, int encodedArrayByteSize);
extern int doEncodeFinish(LameConf* conf, char* encodedBytes, int encodedArrayByteSize);
extern void doClose(LameConf* conf);
extern int doGetEncoderVersion(LameConf* conf, char* charBuffer, int charBufferSize);

#endif

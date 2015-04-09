/*
 *	AJDebug.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2002 by Matthias Pfisterer
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

package org.tritonus.debug;

import org.aspectj.lang.JoinPoint;

import org.tritonus.share.TDebug;

import org.tritonus.sampled.convert.vorbis.VorbisFormatConversionProvider;
import org.tritonus.sampled.convert.vorbis.VorbisFormatConversionProvider.DecodedVorbisAudioInputStream;



/** Debugging output aspect.
 */
privileged aspect AJDebugVorbis
extends Utils
{
	pointcut allExceptions(): handler(Throwable+);

	pointcut AudioConverterCalls():
		execution(JorbisFormatConversionProvider.new(..)) ||
		execution(* JorbisFormatConversionProvider.*(..)) ||
		execution(DecodedJorbisAudioInputStream.new(..)) ||
		execution(* DecodedJorbisAudioInputStream.*(..));


// 	pointcut sourceDataLine():
// 		call(* SourceDataLine+.*(..));


	// currently not used
// 	pointcut printVelocity(): execution(* JavaSoundToneGenerator.playTone(..)) && call(JavaSoundToneGenerator.ToneThread.new(..));

	// pointcut tracedCall(): execution(protected void JavaSoundAudioPlayer.doRealize() throws Exception);


	///////////////////////////////////////////////////////
	//
	//	ACTIONS
	//
	///////////////////////////////////////////////////////


	before(): AudioConverterCalls()
		{
			if (TDebug.TraceAudioConverter)
			{
				outEnteringJoinPoint(thisJoinPoint);
			}
		}

	after(): AudioConverterCalls()
		{
			if (TDebug.TraceAudioConverter)
			{
				outLeavingJoinPoint(thisJoinPoint);
			}
		}


	before(Throwable t): allExceptions() && args(t)
		{
			if (TDebug.TraceAllExceptions)
			{
				TDebug.out(t);
			}
		}
}


/*** AJDebug.java ***/


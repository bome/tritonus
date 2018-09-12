/*
 *	AudioFormats.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999,2000 by Matthias Pfisterer
 *  Copyright (c) 1999 by Florian Bomers
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share.sampled;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;



public class AudioFormats
{
	private static boolean doMatch(int i1, int i2)
	{
		return i1 == AudioSystem.NOT_SPECIFIED
			|| i2 == AudioSystem.NOT_SPECIFIED
			|| i1 == i2;
	}



	private static boolean doMatch(float f1, float f2)
	{
		return f1 == AudioSystem.NOT_SPECIFIED
			|| f2 == AudioSystem.NOT_SPECIFIED
			|| Math.abs(f1 - f2) < 1.0e-9;
	}



	/**
	 * Tests whether 2 AudioFormats have matching formats.
	 * A field matches when it is AudioSystem.NOT_SPECIFIED in
	 * at least one of the formats or the field is the same
	 * in both formats.<br>
	 * Exceptions:
	 * <ul>
	 * <li>Encoding must always be equal for a match.
	 * <li> For a match, endianness must be equal if SampleSizeInBits is not
	 * AudioSystem.NOT_SPECIFIED and greater than 8bit in both formats.<br>
	 * In other words: If SampleSizeInBits is AudioSystem.NOT_SPECIFIED
	 * in either format or both formats have a SampleSizeInBits<8,
	 * endianness does not matter.
	 * </ul>
	 * This is a proposition to be used as AudioFormat.matches.
	 * It can therefore be considered as a temporary workaround.
	 */
	// IDEA: create a special "NOT_SPECIFIED" encoding
	// and a AudioFormat.Encoding.matches method.
	public static boolean matches(AudioFormat format1,
				      AudioFormat format2)
	{
		//$$fb 19 Dec 99: endian must be checked, too.
		//
		// we do have a problem with redundant elements:
		// e.g.
		// encoding=ALAW || ULAW -> bigEndian and samplesizeinbits don't matter
		// sample size in bits == 8 -> bigEndian doesn't matter
		// sample size in bits > 8 -> PCM is always signed.
		// This is an overall issue in JavaSound, I think.
		// At present, it is not consistently implemented to support these
		// redundancies and implicit definitions
		//
		// As a workaround of this issue I return in the converters
		// all combinations, e.g. for ULAW I return bigEndian and !bigEndian formats.
/* old version
*/
		// as proposed by florian
		return format1.getEncoding().equals(format2.getEncoding())
			&& (format2.getSampleSizeInBits()<=8
			    || format1.getSampleSizeInBits()==AudioSystem.NOT_SPECIFIED
			    || format2.getSampleSizeInBits()==AudioSystem.NOT_SPECIFIED
			    || format1.isBigEndian()==format2.isBigEndian())
			&& doMatch(format1.getChannels(),format2.getChannels())
			&& doMatch(format1.getSampleSizeInBits(), format2.getSampleSizeInBits())
			&& doMatch(format1.getFrameSize(), format2.getFrameSize())
			&& doMatch(format1.getSampleRate(), format2.getSampleRate())
			&& doMatch(format1.getFrameRate(),format2.getFrameRate());
	}

	/**
	 * Tests for exact equality of 2 AudioFormats.
	 * This is the behaviour of AudioFormat.matches in JavaSound 1.0.
	 * <p>
	 * This is a proposition to be used as AudioFormat.equals.
	 * It can therefore be considered as a temporary workaround.
	 */
	public static boolean equals(AudioFormat format1,
				      AudioFormat format2)
	{
		return format1.getEncoding().equals(format2.getEncoding())
			&& format1.getChannels() == format2.getChannels()
			&& format1.getSampleSizeInBits() == format2.getSampleSizeInBits()
			&& format1.getFrameSize() == format2.getFrameSize()
			&& (Math.abs(format1.getSampleRate() - format2.getSampleRate()) < 1.0e-9)
			&& (Math.abs(format1.getFrameRate() - format2.getFrameRate()) < 1.0e-9);
	}

}



/*** AudioFormats.java ***/

/*
 *	FloatAudioInputStream.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
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

package org.tritonus.dsp.ais;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import org.tritonus.share.sampled.FloatSampleBuffer;
import org.tritonus.share.sampled.convert.TSynchronousFilteredAudioInputStream;



/** Base class for ... .
 */
public abstract class FloatAudioInputStream
extends TSynchronousFilteredAudioInputStream
{
	private AudioFormat intermediateFloatBufferFormat;
	private FloatSampleBuffer	m_floatBuffer = null;



	public FloatAudioInputStream(AudioInputStream sourceStream, AudioFormat targetFormat)
	{
		// transform the targetFormat so that
		// FrameRate, and SampleRate match the sourceFormat
		super (sourceStream, new AudioFormat(
			       targetFormat.getEncoding(),
			       sourceStream.getFormat().getSampleRate(),
			       targetFormat.getSampleSizeInBits(),
			       targetFormat.getChannels(),
			       targetFormat.getChannels()*targetFormat.getSampleSizeInBits()/8,
			       sourceStream.getFormat().getFrameRate(),
			       targetFormat.isBigEndian()));

		int floatChannels = targetFormat.getChannels();
		intermediateFloatBufferFormat = new AudioFormat(
			targetFormat.getEncoding(),
			sourceStream.getFormat().getSampleRate(),
			targetFormat.getSampleSizeInBits(),
			floatChannels,
			floatChannels*targetFormat.getSampleSizeInBits()/8,
			sourceStream.getFormat().getFrameRate(),
			targetFormat.isBigEndian());
	}



	protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount)
	{
		int sampleCount = inFrameCount * getOriginalStream().getFormat().getChannels();
		int byteCount = sampleCount * (getOriginalStream().getFormat().getSampleSizeInBits()/8);
		if (m_floatBuffer == null)
		{
			m_floatBuffer = new FloatSampleBuffer();
		}
		m_floatBuffer.initFromByteArray(inBuffer, 0, byteCount, getOriginalStream().getFormat());
		convert(m_floatBuffer);
		m_floatBuffer.convertToByteArray(outBuffer, outByteOffset, intermediateFloatBufferFormat);
		return inFrameCount;
	}



	protected abstract void convert(FloatSampleBuffer buffer);
}



/*** FloatAudioInputStream.java ***/

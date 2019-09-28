/*
 *	BaseAudioOutputStreamTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.test.tritonus.sampled.file;

import	java.io.ByteArrayOutputStream;
import	java.io.File;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import	org.tritonus.share.sampled.file.AudioOutputStream;
import	org.tritonus.share.sampled.file.TDataOutputStream;
import	org.tritonus.share.sampled.file.TNonSeekableDataOutputStream;
import	org.tritonus.share.sampled.file.TSeekableDataOutputStream;
import	org.tritonus.test.Util;

import static org.junit.jupiter.api.Assertions.assertTrue;



/*
TEST:
- length (not) given/ (not) seekable
- illegal cases should throw exception
- formats:

signed 16 bit
signed 24 bit
signed 32 bit
unsigned 8 bit
signed 8 bit? check for exception?
always: stereo and mono

either big or little, depending on file format
- illegal endianess should throw exception

 */
public abstract class BaseAudioOutputStreamTestCase
{
	private static final boolean DEBUG = true;

	/** List of sample rates that are used for testing.
	 */
	private static final int[]	SAMPLE_RATES =
	{
		8000, 11025, 12000,
		16000, 22050, 24000,
		32000, 44100, 48000,
		96000, 192000
	};

	/** List of sample sizes that are used for testing.
	 */
	private static final int[]	SAMPLE_SIZES =
	{
		8, 16, 24, 32
	};

	/** List of (number of) channels that are used for testing.
	 */
	private static final int[]	CHANNEL_COUNTS =
	{1, 2};


	private File					m_file;
	private ByteArrayOutputStream	m_baos;



	// non-seekable, given length
    @Test
	public void testAOS1()
		throws Exception
	{
		doTest(false, true);
	}



	// non-seekable, unknown length
    @Test
	public void testAOS2()
		throws Exception
	{
		doTest(false, false);
	}



	// seekable, given length
    @Test
	public void testAOS3()
		throws Exception
	{
		doTest(true, true);
	}



	// seekable, unknown length
    @Test
	public void testAOS4()
		throws Exception
	{
		doTest(true, false);
	}



	private void doTest(boolean bSeekable, boolean bLengthGiven)
		throws Exception
	{
		for (int nSampleRateIndex = 0;
			 nSampleRateIndex < SAMPLE_RATES.length;
			 nSampleRateIndex++)
		{
			if (DEBUG) out("sample rate: " + SAMPLE_RATES[nSampleRateIndex]);
			for (int nSampleSizeIndex = 0;
				 nSampleSizeIndex < SAMPLE_SIZES.length;
				 nSampleSizeIndex++)
			{
				if (DEBUG) out("sample size: " + SAMPLE_SIZES[nSampleSizeIndex]);
				for (int nChannelCountIndex = 0;
					 nChannelCountIndex < CHANNEL_COUNTS.length;
					 nChannelCountIndex++)
				{
					if (DEBUG) out("sample size: " + CHANNEL_COUNTS[nChannelCountIndex]);
					boolean bSigned = ! (SAMPLE_SIZES[nSampleSizeIndex] == 8
										 && is8bitUnsigned());
					AudioFormat	audioFormat = new AudioFormat(
						SAMPLE_RATES[nSampleRateIndex],
						SAMPLE_SIZES[nSampleSizeIndex],
						CHANNEL_COUNTS[nChannelCountIndex],
						bSigned, getBigEndian());
					if (DEBUG) out("AudioFormat: " + audioFormat);
					doTest(audioFormat, bSeekable, bLengthGiven);
				}
			}
		}
	}


	private void doTest(AudioFormat audioFormat,
						boolean bSeekable, boolean bLengthGiven)
		throws Exception
	{
		byte[]	abData = createAudioData(audioFormat.getFrameSize());
		int	nStatedLength = 0;
		if (bLengthGiven)
		{
			nStatedLength = abData.length;
		}
		else
		{
			nStatedLength = AudioSystem.NOT_SPECIFIED;
		}
		AudioOutputStream	aos =
			createAudioOutputStream(audioFormat,
									nStatedLength,
									bSeekable);
		aos.write(abData, 0, abData.length);
		aos.close();
		byte[] abExpectedHeaderData = getExpectedHeaderData(audioFormat, abData.length, bSeekable, bLengthGiven);
		byte[]	abResultingData = getWrittenData(bSeekable);
		if (DEBUG)
		{
			out("expected:");
			Util.dumpByteArray(abExpectedHeaderData);
			out("actual:");
			Util.dumpByteArray(abResultingData);
		}
		boolean bHeaderDataOk = Util.compareByteArrays(abExpectedHeaderData, 0, abResultingData, 0, abExpectedHeaderData.length);
		if (DEBUG) out("headerok: " + bHeaderDataOk);
		assertTrue(bHeaderDataOk, "header data");
		assertTrue(Util.compareByteArrays(abData, 0, abResultingData, abExpectedHeaderData.length + getExpectedAdditionalHeaderLength(), abData.length), "audio data");
		if (m_file != null)
		{
			m_file.delete();
			m_file = null;
		}
	}


	private byte[] createAudioData(int nFrameSize)
	{
		byte[] abData = new byte[8 * nFrameSize];
		for (int i = 0; i < abData.length; i++)
		{
			abData[i] = (byte) i;
		}
		return abData;
	}


	private TDataOutputStream createDataOutputStream(boolean bSeekable)
		throws Exception
	{
		TDataOutputStream dataOutputStream;
		if (bSeekable)
		{
			m_file = File.createTempFile("aos", "au");
			dataOutputStream = new TSeekableDataOutputStream(m_file);
		}
		else
		{
			m_baos = new ByteArrayOutputStream();
			dataOutputStream = new TNonSeekableDataOutputStream(m_baos);
		}
		return dataOutputStream;
	}


	private AudioOutputStream createAudioOutputStream(
		AudioFormat audioFormat,
		long nLength,
		boolean bSeekable)
		throws Exception
	{
		TDataOutputStream dataOutputStream = createDataOutputStream(bSeekable);
		return createAudioOutputStreamImpl(audioFormat,
										   nLength,
										   dataOutputStream);
	}


	protected abstract AudioOutputStream createAudioOutputStreamImpl(
		AudioFormat audioFormat,
		long nLength,
		TDataOutputStream dataOutputStream)
		throws Exception;


	private byte[] getWrittenData(boolean bSeekable)
		throws Exception
	{
		byte[]	abResultingData = null;
		if (bSeekable)
		{
			abResultingData = Util.getByteArrayFromFile(m_file);
		}
		else
		{
			abResultingData = m_baos.toByteArray();
		}
		return abResultingData;
	}


	protected abstract byte[] getExpectedHeaderData(AudioFormat audioFormat,
													int nLength,
													boolean bSeekable,
													boolean bLengthGiven);


	protected abstract int getExpectedAdditionalHeaderLength();

	protected abstract boolean getBigEndian();

	protected abstract boolean is8bitUnsigned();


	protected void out(String strMessage)
	{
		System.out.println(strMessage);
	}
}



/*** BaseAudioOutputStreamTestCase.java ***/

/*
 *	BaseAudioFileReaderTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2003 by Matthias Pfisterer
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

package org.tritonus.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.spi.FormatConversionProvider;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.tritonus.share.sampled.Encodings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Disabled
public class BaseFormatConversionProviderTestCase
extends BaseProviderTestCase
{
	private static final AudioFormat.Encoding[]	EMPTY_ENCODING_ARRAY = new AudioFormat.Encoding[0];
	private static final AudioFormat.Encoding[]	ALL_ENCODINGS = new AudioFormat.Encoding[]
	{
		AudioFormat.Encoding.PCM_SIGNED,
		AudioFormat.Encoding.PCM_UNSIGNED,
		AudioFormat.Encoding.ULAW,
		AudioFormat.Encoding.ALAW,
		Encodings.getEncoding("GSM0610"),
		Encodings.getEncoding("MPEG1L1"),
		Encodings.getEncoding("MPEG1L2"),
		Encodings.getEncoding("MPEG1L3"),
		Encodings.getEncoding("MPEG2L1"),
		Encodings.getEncoding("MPEG2L2"),
		Encodings.getEncoding("MPEG2L3"),
		Encodings.getEncoding("MPEG2DOT5L1"),
		Encodings.getEncoding("MPEG2DOT5L2"),
		Encodings.getEncoding("MPEG2DOT5L3"),
		Encodings.getEncoding("VORBIS"),
		Encodings.getEncoding("IMA_ADPCM"),
	};




	private static final boolean	DEBUG = true;
	private static final String	RESOURCE_BASENAME = "formatconversionprovider";



	public BaseFormatConversionProviderTestCase()
	{
		super(RESOURCE_BASENAME);
	}



	protected FormatConversionProvider getFormatConversionProvider()
	{
		return (FormatConversionProvider) getProvider();
	}



    @Test
	public void testGetSourceEncodings()
	{
		AudioFormat.Encoding[]	aEncodings = null;
		if (getTestProvider())
		{
			aEncodings = getFormatConversionProvider().getSourceEncodings();
			checkEncodings(aEncodings, true);
		}
	}



    @Test
	public void testGetTargetEncodings()
	{
		AudioFormat.Encoding[]	aEncodings = null;
		if (getTestProvider())
		{
			aEncodings = getFormatConversionProvider().getTargetEncodings();
			checkEncodings(aEncodings, false);
		}
	}



	private void checkEncodings(AudioFormat.Encoding[] aEncodings,
				    boolean bSource)
	{
		AudioFormat.Encoding[] aExpectedEncodings = getEncodings(bSource);
		Iterator	iter;
		List	encodings = Arrays.asList(aEncodings);
		List	expectedEncodings = Arrays.asList(aExpectedEncodings);
		iter = encodings.iterator();
		while (iter.hasNext())
		{
			Object encoding = iter.next();
			assertTrue(expectedEncodings.contains(encoding),
				   "returned encoding in expected encodings");
		}
		iter = expectedEncodings.iterator();
		while (iter.hasNext())
		{
			Object encoding = iter.next();
			assertTrue(encodings.contains(encoding),
				   "expected encoding in returned encodings");
		}
	}



    @Test
	public void testIsSourceEncodingsSupported()
	{
		implTestIsEncodingSupported(true);
	}



    @Test
	public void testIsTargetEncodingsSupported()
	{
		implTestIsEncodingSupported(false);
	}



	private void implTestIsEncodingSupported(boolean bSource)
	{
		if (getTestProvider())
		{
			AudioFormat.Encoding[] aExpectedEncodings = getEncodings(bSource);
			for (int i = 0; i < aExpectedEncodings.length; i++)
			{
				boolean	bSupported;
				if (bSource)
				{
					bSupported = getFormatConversionProvider().isSourceEncodingSupported(aExpectedEncodings[i]);
				}
				else
				{
					bSupported = getFormatConversionProvider().isTargetEncodingSupported(aExpectedEncodings[i]);
				}
				assertTrue(bSupported,
					   "expected encoding supported");
			}
			AudioFormat.Encoding[] aUnexpectedEncodings = getUnexpectedEncodings(bSource);
			for (int i = 0; i < aUnexpectedEncodings.length; i++)
			{
				boolean	bSupported;
				if (bSource)
				{
					bSupported = getFormatConversionProvider().isSourceEncodingSupported(aUnexpectedEncodings[i]);
				}
				else
				{
					bSupported = getFormatConversionProvider().isTargetEncodingSupported(aUnexpectedEncodings[i]);
				}
				assertTrue(! bSupported,
					   "unexpected encoding supported");
			}
		}
	}



	private void checkAudioInputStream(AudioInputStream audioInputStream, boolean bRealLengthExpected)
		throws Exception
	{
		checkAudioFormat(audioInputStream.getFormat());
		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
		if (/*getCheckRealLengths() ||*/ bRealLengthExpected)
		{
			lExpectedFrameLength = getFrameLength();
		}
		assertEquals(lExpectedFrameLength,
			     audioInputStream.getFrameLength(),
			     "frame length");
		if (/*getCheckRealLengths() ||*/ bRealLengthExpected)
		{
			int	nExpectedDataLength = (int) (lExpectedFrameLength * getFrameSize());
			byte[]	abRetrievedData = new byte[nExpectedDataLength];
			int	nRead = audioInputStream.read(abRetrievedData);
			assertEquals(nExpectedDataLength,
				     nRead,
				     "reading data");
// 			for (int i = 0; i < nExpectedDataLength; i++)
// 			{
// 				assertEquals("data content", 0, abRetrievedData[i]);
// 			}
		}
		else
		{
			// TODO: try to at least read some bytes?
		}
	}



	private void checkAudioFormat(AudioFormat audioFormat)
		throws Exception
	{
		assertEquals(getEncoding(),
			     audioFormat.getEncoding(),
			     "encoding");
		assertEquals(
			     getSampleRate(),
			     audioFormat.getSampleRate(),
			     DELTA, "sample rate");
		assertEquals(getSampleSizeInBits(),
			     audioFormat.getSampleSizeInBits(),
			     "sample size (bits)");
		assertEquals(getChannels(),
			     audioFormat.getChannels(),
			     "channels");
		assertEquals(getFrameSize(),
			     audioFormat.getFrameSize(),
			     "frame size");
		assertEquals(
			     getFrameRate(),
			     audioFormat.getFrameRate(),
			     DELTA, "frame rate");
		assertEquals(getBigEndian(),
			     audioFormat.isBigEndian(),
			     "big endian");
	}



	private String getFilename()
	{
		String	strFileName = getResourceString(getResourcePrefix() + ".filename");
		return strFileName;
	}



	private long getByteLength()
	{
		String	strByteLength = getResourceString(getResourcePrefix() + ".byteLength");
		long	lByteLength = Long.parseLong(strByteLength);
		return lByteLength;
	}



	private AudioFormat.Encoding getEncoding()
	{
		String	strEncodingName = getResourceString(getResourcePrefix() + ".format.encoding");
		AudioFormat.Encoding	encoding = Encodings.getEncoding(strEncodingName);
		return encoding;
	}



	private float getSampleRate()
	{
		String	strSampleRate = getResourceString(getResourcePrefix() + ".format.sampleRate");
		float	fSampleRate = Float.parseFloat(strSampleRate);
		return fSampleRate;
	}



	private int getSampleSizeInBits()
	{
		String	strSampleSizeInBits = getResourceString(getResourcePrefix() + ".format.sampleSizeInBits");
		int	nSampleSizeInBits = Integer.parseInt(strSampleSizeInBits);
		return nSampleSizeInBits;
	}



	private int getChannels()
	{
		String	strChannels = getResourceString(getResourcePrefix() + ".format.channels");
		int	nChannels = Integer.parseInt(strChannels);
		return nChannels;
	}



	private int getFrameSize()
	{
		String	strFrameSize = getResourceString(getResourcePrefix() + ".format.frameSize");
		int	nFrameSize = Integer.parseInt(strFrameSize);
		return nFrameSize;
	}



	private float getFrameRate()
	{
		String	strFrameRate = getResourceString(getResourcePrefix() + ".format.frameRate");
		float	fFrameRate = Float.parseFloat(strFrameRate);
		return fFrameRate;
	}



	private boolean getBigEndian()
	{
		String	strBigEndian = getResourceString(getResourcePrefix() + ".format.bigEndian");
		boolean	bBigEndian = strBigEndian.equals("true");
		return bBigEndian;
	}



	private long getFrameLength()
	{
		String	strFrameLength = getResourceString(getResourcePrefix() + ".frameLength");
		long	lFrameLength = Long.parseLong(strFrameLength);
		return lFrameLength;
	}



	private AudioFormat.Encoding[] getEncodings(boolean bSource)
	{
		if (bSource)
		{
			return getEncodings("sourceEncodings");
		}
		else
		{
			return getEncodings("targetEncodings");
		}
	}



	private AudioFormat.Encoding[] getUnexpectedEncodings(boolean bSource)
	{
		AudioFormat.Encoding[]	aExpectedEncodings;
		if (bSource)
		{
			aExpectedEncodings = getEncodings("sourceEncodings");
		}
		else
		{
			aExpectedEncodings = getEncodings("targetEncodings");
		}
		List	expectedEncodings = Arrays.asList(aExpectedEncodings);
		AudioFormat.Encoding[]	aAllEncodings = ALL_ENCODINGS;
		AudioFormat.Encoding[]	aUnexpectedEncodings = new AudioFormat.Encoding[aAllEncodings.length - aExpectedEncodings.length];
		int	nIndex = 0;
		for (int i = 0; i < aAllEncodings.length; i++)
		{
			if (! expectedEncodings.contains(aAllEncodings[i]))
			{
				aUnexpectedEncodings[nIndex] = aAllEncodings[i];
				nIndex++;
			} 
		}
		return aUnexpectedEncodings;
	}



	private AudioFormat.Encoding[] getEncodings(String strKey)
	{
		String	strEncodings = getResourceString(getResourcePrefix() + "." + strKey);
		List<AudioFormat.Encoding> encodingsList =
			new ArrayList<AudioFormat.Encoding>();
		StringTokenizer	tokenizer = new StringTokenizer(strEncodings);
		while (tokenizer.hasMoreTokens())
		{
			String	strEncodingName = tokenizer.nextToken();
			AudioFormat.Encoding	encoding = Encodings.getEncoding(strEncodingName);
			encodingsList.add(encoding);
		}
		return (AudioFormat.Encoding[]) encodingsList.toArray(EMPTY_ENCODING_ARRAY);
	}
}



/*** BaseFormatConversionProviderTestCase.java ***/

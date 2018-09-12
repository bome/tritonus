/*
 *	BaseAudioFileReaderTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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

import	java.io.BufferedInputStream;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.InputStream;

import	java.net.URL;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.spi.AudioFileReader;

import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.Encodings;



public class BaseAudioFileReaderTestCase
extends BaseProviderTestCase
{
	private static final boolean	DEBUG = true;
	private static final String	RESOURCE_BASENAME = "audiofilereader";
	private static final String	PROVIDER_PREFIX = "(Provider:) ";
	private static final String	AUDIOSYSTEM_PREFIX = "(AudioSystem:) ";



	private boolean			m_bCheckRealLengths;



	public BaseAudioFileReaderTestCase(String strName)
	{
		super(strName,
		      RESOURCE_BASENAME);
		setCheckRealLengths(true);
	}



	protected void setCheckRealLengths(boolean bCheckRealLengths)
	{
		m_bCheckRealLengths = bCheckRealLengths;
	}



	private boolean getCheckRealLengths()
	{
		return m_bCheckRealLengths;
	}



	protected AudioFileReader getAudioFileReader()
	{
		return (AudioFileReader) getProvider();
	}



	public void testAudioFileFormatFile()
		throws Exception
	{
		File	file = new File(getFilename());
		AudioFileFormat	audioFileFormat = null;
		if (getTestProvider())
		{
			audioFileFormat = getAudioFileReader().getAudioFileFormat(file);
			checkAudioFileFormat(audioFileFormat, true, true);
		}
		if (getTestAudioSystem())
		{
			audioFileFormat = AudioSystem.getAudioFileFormat(file);
			checkAudioFileFormat(audioFileFormat, true, false);
		}
	}



	public void testAudioFileFormatURL()
		throws Exception
	{
		URL	url = new URL("file:" + getFilename());
		AudioFileFormat	audioFileFormat = null;
		if (getTestProvider())
		{
			audioFileFormat = getAudioFileReader().getAudioFileFormat(url);
			checkAudioFileFormat(audioFileFormat, false, true);
		}
		if (getTestAudioSystem())
		{
			audioFileFormat = AudioSystem.getAudioFileFormat(url);
			checkAudioFileFormat(audioFileFormat, false, false);
		}
	}



	public void testAudioFileFormatInputStream()
		throws Exception
	{
		InputStream	inputStream = new FileInputStream(getFilename());
		BufferedInputStream	bufferedInputStream = new BufferedInputStream(inputStream);
		AudioFileFormat	audioFileFormat = null;
		if (getTestProvider())
		{
			audioFileFormat = getAudioFileReader().getAudioFileFormat(bufferedInputStream);
			checkAudioFileFormat(audioFileFormat, false, true);
		}
		inputStream = new FileInputStream(getFilename());
		bufferedInputStream = new BufferedInputStream(inputStream);
		if (getTestAudioSystem())
		{
			audioFileFormat = AudioSystem.getAudioFileFormat(bufferedInputStream);
			checkAudioFileFormat(audioFileFormat, false, false);
		}
	}



	public void testAudioInputStreamFile()
		throws Exception
	{
		File	file = new File(getFilename());
		AudioInputStream	audioInputStream = null;
		if (getTestProvider())
		{
			audioInputStream = getAudioFileReader().getAudioInputStream(file);
			checkAudioInputStream(audioInputStream, true, true);
		}
		if (getTestAudioSystem())
		{
			audioInputStream = AudioSystem.getAudioInputStream(file);
			checkAudioInputStream(audioInputStream, true, false);
		}
	}



	public void testAudioInputStreamURL()
		throws Exception
	{
		URL	url = new URL("file:" + getFilename());
		AudioInputStream	audioInputStream = null;
		if (getTestProvider())
		{
			audioInputStream = getAudioFileReader().getAudioInputStream(url);
			checkAudioInputStream(audioInputStream, false, true);
		}
		if (getTestAudioSystem())
		{
			audioInputStream = AudioSystem.getAudioInputStream(url);
			checkAudioInputStream(audioInputStream, false, false);
		}
	}



	public void testAudioInputStreamInputStream()
		throws Exception
	{
		InputStream	inputStream = new FileInputStream(getFilename());
		BufferedInputStream	bufferedInputStream = new BufferedInputStream(inputStream);
		AudioInputStream	audioInputStream = null;
		if (getTestProvider())
		{
			audioInputStream = getAudioFileReader().getAudioInputStream(bufferedInputStream);
			checkAudioInputStream(audioInputStream, false, true);
		}
		inputStream = new FileInputStream(getFilename());
		bufferedInputStream = new BufferedInputStream(inputStream);
		if (getTestAudioSystem())
		{
			audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
			checkAudioInputStream(audioInputStream, false, false);
		}
	}



	private void checkAudioFileFormat(AudioFileFormat audioFileFormat,
					  boolean bRealLengthExpected,
					  boolean bProviderDirect)
		throws Exception
	{
		if (bProviderDirect)
		{
			checkAudioFileFormat(audioFileFormat, bRealLengthExpected,
					     PROVIDER_PREFIX);
		}
		else
		{
			checkAudioFileFormat(audioFileFormat, bRealLengthExpected,
					     AUDIOSYSTEM_PREFIX);
		}
	}



	private void checkAudioFileFormat(AudioFileFormat audioFileFormat, boolean bRealLengthExpected, String strMessagePrefix)
		throws Exception
	{
		assertEquals(strMessagePrefix + "type",
			     getType(),
			     audioFileFormat.getType());
		checkAudioFormat(audioFileFormat.getFormat(), strMessagePrefix);
		long	lExpectedByteLength = AudioSystem.NOT_SPECIFIED;
		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
		if (getCheckRealLengths() || bRealLengthExpected)
		{
			lExpectedByteLength = getByteLength();
			lExpectedFrameLength = getFrameLength();
		}
		assertEquals(strMessagePrefix + "byte length",
			     lExpectedByteLength,
			     audioFileFormat.getByteLength());
		assertEquals(strMessagePrefix + "frame length",
			     lExpectedFrameLength,
			     audioFileFormat.getFrameLength());
	}



	private void checkAudioInputStream(AudioInputStream audioInputStream,
					   boolean bRealLengthExpected,
					   boolean bProviderDirect)
		throws Exception
	{
		if (bProviderDirect)
		{
			checkAudioInputStream(audioInputStream,
					      bRealLengthExpected,
					      PROVIDER_PREFIX);
		}
		else
		{
			checkAudioInputStream(audioInputStream,
					      bRealLengthExpected,
					      AUDIOSYSTEM_PREFIX);
		}
	}



	private void checkAudioInputStream(AudioInputStream audioInputStream,
					   boolean bRealLengthExpected,
					   String strMessagePrefix)
		throws Exception
	{
		checkAudioFormat(audioInputStream.getFormat(), strMessagePrefix);
		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
		if (getCheckRealLengths() || bRealLengthExpected)
		{
			lExpectedFrameLength = getFrameLength();
		}
		assertEquals(strMessagePrefix + "frame length",
			     lExpectedFrameLength,
			     audioInputStream.getFrameLength());
		if (getCheckRealLengths() || bRealLengthExpected)
		{
			int	nExpectedDataLength = (int) (lExpectedFrameLength * getFrameSize());
			byte[]	abRetrievedData = new byte[nExpectedDataLength];
			int	nRead = audioInputStream.read(abRetrievedData);
			assertEquals(strMessagePrefix + "reading data",
				     nExpectedDataLength,
				     nRead);
// 			for (int i = 0; i < nExpectedDataLength; i++)
// 			{
// 				assertEquals(strMessagePrefix + "data content", 0, abRetrievedData[i]);
// 			}
		}
		else
		{
			// TODO: try to at least read some bytes?
		}
	}



	private void checkAudioFormat(AudioFormat audioFormat, String strMessagePrefix)
		throws Exception
	{
		assertEquals(strMessagePrefix + "encoding",
			     getEncoding(),
			     audioFormat.getEncoding());
		assertEquals(strMessagePrefix + "sample rate",
			     getSampleRate(),
			     audioFormat.getSampleRate(),
			     DELTA);
		assertEquals(strMessagePrefix + "sample size (bits)",
			     getSampleSizeInBits(),
			     audioFormat.getSampleSizeInBits());
		assertEquals(strMessagePrefix + "channels",
			     getChannels(),
			     audioFormat.getChannels());
		assertEquals(strMessagePrefix + "frame size",
			     getFrameSize(),
			     audioFormat.getFrameSize());
		assertEquals(strMessagePrefix + "frame rate",
			     getFrameRate(),
			     audioFormat.getFrameRate(),
			     DELTA);
		assertEquals(strMessagePrefix + "big endian",
			     getBigEndian(),
			     audioFormat.isBigEndian());
	}



	private String getFilename()
	{
		String	strFileName = getResourceString(getResourcePrefix() + ".filename");
		return strFileName;
	}



	private AudioFileFormat.Type getType()
	{
		String	strTypeName = getResourceString(getResourcePrefix() + ".type");
		AudioFileFormat.Type	type = AudioFileTypes.getType(strTypeName);
		return type;
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
}



/*** BaseAudioFileReaderTestCase.java ***/

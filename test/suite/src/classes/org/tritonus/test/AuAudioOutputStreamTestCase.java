/*
 *	AuAudioOutputStreamTestCase.java
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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
 */

package org.tritonus.test;

import	java.io.BufferedInputStream;
import	java.io.ByteArrayOutputStream;
import	java.io.File;
import	java.io.FileInputStream;
import	java.io.InputStream;
import	java.io.IOException;

import	java.net.URL;

import	java.util.MissingResourceException;
import	java.util.ResourceBundle;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.spi.AudioFileReader;

import	junit.framework.TestCase;

import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.Encodings;

import	org.tritonus.share.sampled.file.AudioOutputStream;
import	org.tritonus.share.sampled.file.TDataOutputStream;
import	org.tritonus.share.sampled.file.TSeekableDataOutputStream;
import	org.tritonus.share.sampled.file.TNonSeekableDataOutputStream;
import	org.tritonus.sampled.file.AuAudioOutputStream;



public class AuAudioOutputStreamTestCase
extends TestCase
{
// 	private static final boolean	DEBUG = true;
 	private static final String	RESOURCE_BASENAME = "audiooutputstream";

// 	/**	Precision for float comparisons.
// 	 */
// 	private static final float	DELTA = 0.1F;


 	private ResourceBundle		m_resourceBundle;
 	private String			m_strResourcePrefix;
// 	private AudioFileReader		m_audioFileReader;
// 	private boolean			m_bCheckRealLengths;



	public AuAudioOutputStreamTestCase(String strName)
	{
		super(strName);
 		m_resourceBundle = loadResourceBundle(RESOURCE_BASENAME);
		setResourcePrefix("au");
// 		setCheckRealLengths(true);
	}



	protected void setResourcePrefix(String strResourcePrefix)
	{
		m_strResourcePrefix = strResourcePrefix;
	}



// 	protected void setCheckRealLengths(boolean bCheckRealLengths)
// 	{
// 		m_bCheckRealLengths = bCheckRealLengths;
// 	}



// 	private boolean getCheckRealLengths()
// 	{
// 		return m_bCheckRealLengths;
// 	}



// 	protected void setUp()
// 		throws Exception
// 	{
// 		if (getTestAudioFileReader())
// 		{
// 			String	strClassName = getClassName();
// 			Class	cls = Class.forName(strClassName);
// 			m_audioFileReader = (AudioFileReader) cls.newInstance();
// 		}
// 	}



// 	protected AudioFileReader getAudioFileReader()
// 	{
// 		return m_audioFileReader;
// 	}



	// non-seekable, given length
	public void testAOS1()
		throws Exception
	{
		doTest(false, true);
	}



	// non-seekable, unknown length
	public void testAOS2()
		throws Exception
	{
		doTest(false, false);
	}



	// seekable, given length
	public void testAOS3()
		throws Exception
	{
		doTest(true, true);
	}



	// seekable, unknown length
	public void testAOS4()
		throws Exception
	{
		doTest(true, false);
	}



	private void doTest(boolean bSeekable, boolean bLengthGiven)
		throws Exception
	{
		TDataOutputStream	dataOutputStream = null;
		File			file = null;
		ByteArrayOutputStream	baos = null;
		if (bSeekable)
		{
			file = new File(getFilename());
			dataOutputStream = new TSeekableDataOutputStream(file);
		}
		else
		{
			baos = new ByteArrayOutputStream();
			dataOutputStream = new TNonSeekableDataOutputStream(baos);
		}
		AudioFormat	audioFormat = new AudioFormat(getEncoding(),
							      getSampleRate(),
							      getSampleSizeInBits(),
							      getChannels(),
							      getFrameSize(),
							      getFrameRate(),
							      getBigEndian());
		byte[]	abData = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
		int	nLength = 0;
		if (bLengthGiven)
		{
			nLength = abData.length;
		}
		else
		{
			nLength = AudioSystem.NOT_SPECIFIED;
		}
		AudioOutputStream	aos =
			new AuAudioOutputStream(audioFormat,
						nLength,
						dataOutputStream);
		aos.write(abData, 0, abData.length);
		aos.close();
		int	nExpectedAdditionalHeaderLength = 20;
		byte[]	abExpectedHeaderData = null;
		if (bLengthGiven || bSeekable)
		{
			abExpectedHeaderData = new byte[]{
			0x2e, 0x73, 0x6e, 0x64,
			0, 0, 0, (byte) (24 + nExpectedAdditionalHeaderLength),
			0, 0, 0, (byte) abData.length,
			0, 0, 0, 3,
			0, 0, (byte) (44100 / 256), (byte) (44100 % 256),
			0, 0, 0, 2};
		}
		else
		{
			abExpectedHeaderData = new byte[]{
			0x2e, 0x73, 0x6e, 0x64,
			0, 0, 0, (byte) (24 + nExpectedAdditionalHeaderLength),
			(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
			0, 0, 0, 3,
			0, 0, (byte) (44100 / 256), (byte) (44100 % 256),
			0, 0, 0, 2};
		}
		byte[]	abResultingData = null;
		if (bSeekable)
		{
			abResultingData = Util.getByteArrayFromFile(file);
		}
		else
		{
			abResultingData = baos.toByteArray();
		}
		// Util.dumpByteArray(abResultingData);
		assertTrue(Util.compareByteArrays(abExpectedHeaderData, 0, abResultingData, 0, 24));
		assertTrue(Util.compareByteArrays(abData, 0, abResultingData, 24 + nExpectedAdditionalHeaderLength, abData.length));
	}



// 	public void testAudioFileFormatFile()
// 		throws Exception
// 	{
// 		File	file = new File(getFilename());
// 		AudioFileFormat	audioFileFormat = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioFileFormat = getAudioFileReader().getAudioFileFormat(file);
// 			checkAudioFileFormat(audioFileFormat, true);
// 		}
// 		if (getTestAudioSystem())
// 		{
// 			audioFileFormat = AudioSystem.getAudioFileFormat(file);
// 			checkAudioFileFormat(audioFileFormat, true);
// 		}
// 	}



// 	public void testAudioFileFormatURL()
// 		throws Exception
// 	{
// 		URL	url = new URL("file:" + getFilename());
// 		AudioFileFormat	audioFileFormat = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioFileFormat = getAudioFileReader().getAudioFileFormat(url);
// 			checkAudioFileFormat(audioFileFormat, false);
// 		}
// 		if (getTestAudioSystem())
// 		{
// 			audioFileFormat = AudioSystem.getAudioFileFormat(url);
// 			checkAudioFileFormat(audioFileFormat, false);
// 		}
// 	}



// 	public void testAudioFileFormatInputStream()
// 		throws Exception
// 	{
// 		InputStream	inputStream = new FileInputStream(getFilename());
// 		BufferedInputStream	bufferedInputStream = new BufferedInputStream(inputStream);
// 		AudioFileFormat	audioFileFormat = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioFileFormat = getAudioFileReader().getAudioFileFormat(bufferedInputStream);
// 			checkAudioFileFormat(audioFileFormat, false);
// 		}
// 		inputStream = new FileInputStream(getFilename());
// 		bufferedInputStream = new BufferedInputStream(inputStream);
// 		if (getTestAudioSystem())
// 		{
// 			audioFileFormat = AudioSystem.getAudioFileFormat(bufferedInputStream);
// 			checkAudioFileFormat(audioFileFormat, false);
// 		}
// 	}



// 	private void checkAudioFileFormat(AudioFileFormat audioFileFormat, boolean bRealLengthExpected)
// 		throws Exception
// 	{
// 		assertEquals("type",
// 			     getType(),
// 			     audioFileFormat.getType());
// 		checkAudioFormat(audioFileFormat.getFormat());
// 		long	lExpectedByteLength = AudioSystem.NOT_SPECIFIED;
// 		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
// 		if (getCheckRealLengths() || bRealLengthExpected)
// 		{
// 			lExpectedByteLength = getByteLength();
// 			lExpectedFrameLength = getFrameLength();
// 		}
// 		assertEquals("byte length",
// 			     lExpectedByteLength,
// 			     audioFileFormat.getByteLength());
// 		assertEquals("frame length",
// 			     lExpectedFrameLength,
// 			     audioFileFormat.getFrameLength());
// 	}



// 	public void testAudioInputStreamFile()
// 		throws Exception
// 	{
// 		File	file = new File(getFilename());
// 		AudioInputStream	audioInputStream = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioInputStream = getAudioFileReader().getAudioInputStream(file);
// 			checkAudioInputStream(audioInputStream, true);
// 		}
// 		if (getTestAudioSystem())
// 		{
// 			audioInputStream = AudioSystem.getAudioInputStream(file);
// 			checkAudioInputStream(audioInputStream, true);
// 		}
// 	}



// 	public void testAudioInputStreamURL()
// 		throws Exception
// 	{
// 		URL	url = new URL("file:" + getFilename());
// 		AudioInputStream	audioInputStream = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioInputStream = getAudioFileReader().getAudioInputStream(url);
// 			checkAudioInputStream(audioInputStream, false);
// 		}
// 		if (getTestAudioSystem())
// 		{
// 			audioInputStream = AudioSystem.getAudioInputStream(url);
// 			checkAudioInputStream(audioInputStream, false);
// 		}
// 	}



// 	public void testAudioInputStreamInputStream()
// 		throws Exception
// 	{
// 		InputStream	inputStream = new FileInputStream(getFilename());
// 		BufferedInputStream	bufferedInputStream = new BufferedInputStream(inputStream);
// 		AudioInputStream	audioInputStream = null;
// 		if (getTestAudioFileReader())
// 		{
// 			audioInputStream = getAudioFileReader().getAudioInputStream(bufferedInputStream);
// 			checkAudioInputStream(audioInputStream, false);
// 		}
// 		inputStream = new FileInputStream(getFilename());
// 		bufferedInputStream = new BufferedInputStream(inputStream);
// 		if (getTestAudioSystem())
// 		{
// 			audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
// 			checkAudioInputStream(audioInputStream, false);
// 		}
// 	}



// 	private void checkAudioInputStream(AudioInputStream audioInputStream, boolean bRealLengthExpected)
// 		throws Exception
// 	{
// 		checkAudioFormat(audioInputStream.getFormat());
// 		long	lExpectedFrameLength = AudioSystem.NOT_SPECIFIED;
// 		if (getCheckRealLengths() || bRealLengthExpected)
// 		{
// 			lExpectedFrameLength = getFrameLength();
// 		}
// 		assertEquals("frame length",
// 			     lExpectedFrameLength,
// 			     audioInputStream.getFrameLength());
// 		if (getCheckRealLengths() || bRealLengthExpected)
// 		{
// 			int	nExpectedDataLength = (int) (lExpectedFrameLength * getFrameSize());
// 			byte[]	abRetrievedData = new byte[nExpectedDataLength];
// 			int	nRead = audioInputStream.read(abRetrievedData);
// 			assertEquals("reading data",
// 				     nExpectedDataLength,
// 				     nRead);
// // 			for (int i = 0; i < nExpectedDataLength; i++)
// // 			{
// // 				assertEquals("data content", 0, abRetrievedData[i]);
// // 			}
// 		}
// 		else
// 		{
// 			// TODO: try to at least read some bytes?
// 		}
// 	}



// 	private void checkAudioFormat(AudioFormat audioFormat)
// 		throws Exception
// 	{
// 		assertEquals("encoding",
// 			     getEncoding(),
// 			     audioFormat.getEncoding());
// 		assertEquals("sample rate",
// 			     getSampleRate(),
// 			     audioFormat.getSampleRate(),
// 			     DELTA);
// 		assertEquals("sample size (bits)",
// 			     getSampleSizeInBits(),
// 			     audioFormat.getSampleSizeInBits());
// 		assertEquals("channels",
// 			     getChannels(),
// 			     audioFormat.getChannels());
// 		assertEquals("frame size",
// 			     getFrameSize(),
// 			     audioFormat.getFrameSize());
// 		assertEquals("frame rate",
// 			     getFrameRate(),
// 			     audioFormat.getFrameRate(),
// 			     DELTA);
// 		assertEquals("big endian",
// 			     getBigEndian(),
// 			     audioFormat.isBigEndian());
// 	}



	private ResourceBundle loadResourceBundle(String sResourceBasename)
	{
		ResourceBundle	resourceBundle = null;
		try
		{
			resourceBundle = ResourceBundle.getBundle(sResourceBasename);
		}
		catch (MissingResourceException e)
		{
			e.printStackTrace();
/*			System.err.println("ActionManager.loadResourceBundle(): cannot find property file!");
			System.exit(1);
*/		}
		return resourceBundle;
	}



	private String getResourcePrefix()
	{
		return m_strResourcePrefix;
	}



	private String getResourceString(String strKey)
	{
		return m_resourceBundle.getString(strKey);
	}



// 	private String getClassName()
// 	{
// 		String	strClassName = getResourceString(getResourcePrefix() + ".class");
// 		return strClassName;
// 	}



	private String getFilename()
	{
		String	strFileName = getResourceString(getResourcePrefix() + ".filename");
		return strFileName;
	}



// 	private AudioFileFormat.Type getType()
// 	{
// 		String	strTypeName = getResourceString(getResourcePrefix() + ".type");
// 		AudioFileFormat.Type	type = AudioFileTypes.getType(strTypeName);
// 		return type;
// 	}



// 	private long getByteLength()
// 	{
// 		String	strByteLength = getResourceString(getResourcePrefix() + ".byteLength");
// 		long	lByteLength = Long.parseLong(strByteLength);
// 		return lByteLength;
// 	}



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



// 	private long getFrameLength()
// 	{
// 		String	strFrameLength = getResourceString(getResourcePrefix() + ".frameLength");
// 		long	lFrameLength = Long.parseLong(strFrameLength);
// 		return lFrameLength;
// 	}
}



/*** AuAudioOutputStreamTestCase.java ***/

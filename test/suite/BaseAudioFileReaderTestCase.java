/*
 *	BaseAudioFileReaderTestCase.java
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


import	java.io.File;
import	java.io.FileInputStream;
import	java.io.InputStream;

import	java.net.URL;

import	java.util.MissingResourceException;
import	java.util.ResourceBundle;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.spi.AudioFileReader;

import	junit.framework.TestCase;

import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.Encodings;



public class BaseAudioFileReaderTestCase
extends TestCase
{
	private static final boolean	DEBUG = true;
	private static final String	RESOURCE_BASENAME = "audiofilereader";

	private ResourceBundle		m_resourceBundle;
	private String			m_strResourcePrefix;
	private AudioFileReader		m_audioFileReader;



	public BaseAudioFileReaderTestCase(String strName)
	{
		super(strName);
		m_resourceBundle = loadResourceBundle(RESOURCE_BASENAME);
	}



	protected void setResourcePrefix(String strResourcePrefix)
	{
		m_strResourcePrefix = strResourcePrefix;
	}



	protected void setUp()
		throws Exception
	{
		String	strClassName = getClassName();
		Class	cls = Class.forName(strClassName);
		m_audioFileReader = (AudioFileReader) cls.newInstance();
	}



	protected AudioFileReader getAudioFileReader()
	{
		return m_audioFileReader;
	}



	public void testAudioFileFormatFile()
		throws Exception
	{
		File	file = new File(getFilename());
		AudioFileFormat	audioFileFormat = getAudioFileReader().getAudioFileFormat(file);
		checkAudioFileFormat(audioFileFormat);
	}



	public void testAudioFileFormatURL()
		throws Exception
	{
		URL	url = new URL("file:" + getFilename());
		AudioFileFormat	audioFileFormat = getAudioFileReader().getAudioFileFormat(url);
		checkAudioFileFormat(audioFileFormat);
	}



	public void testAudioFileFormatInputStream()
		throws Exception
	{
		InputStream	inputStream = new FileInputStream(getFilename());
		AudioFileFormat	audioFileFormat = getAudioFileReader().getAudioFileFormat(inputStream);
		checkAudioFileFormat(audioFileFormat);
	}



	private void checkAudioFileFormat(AudioFileFormat audioFileFormat)
		throws Exception
	{
		assertTrue(audioFileFormat.getType().equals(getType()));
		assertTrue(audioFileFormat.getByteLength() == getByteLength());
		assertTrue(audioFileFormat.getFormat().getEncoding().equals(getEncoding()));
		assertTrue(audioFileFormat.getFormat().getSampleRate() == getSampleRate());
		assertTrue(audioFileFormat.getFormat().getSampleSizeInBits() == getSampleSizeInBits());
		assertTrue(audioFileFormat.getFormat().getChannels() == getChannels());
		assertTrue(audioFileFormat.getFormat().getFrameSize() == getFrameSize());
		assertTrue(audioFileFormat.getFormat().getFrameRate() == getFrameRate());
		assertTrue(audioFileFormat.getFormat().isBigEndian() == getBigEndian());
		assertTrue(audioFileFormat.getFrameLength() == getFrameLength());
	}



	public void testAudioInputStreamFile()
		throws Exception
	{
		// TODO:
	}



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



	private String getClassName()
	{
		String	strClassName = getResourceString(getResourcePrefix() + ".class");
		return strClassName;
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

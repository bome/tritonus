/*
 *	BaseProviderTestCase.java
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

package org.tritonus.test;

import	java.util.MissingResourceException;
import	java.util.ResourceBundle;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;

import org.junit.jupiter.api.BeforeEach;
import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.Encodings;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BaseProviderTestCase
{
	private static final boolean	DEBUG = true;

	/**	Precision for float comparisons.
	 */
	protected static final float	DELTA = 0.1F;


	private ResourceBundle		m_resourceBundle;
	private String			m_strResourcePrefix;
	private Object			m_provider;
	private boolean			m_bCheckRealLengths;



	public BaseProviderTestCase(String strResourceBasename)
	{
		m_resourceBundle = loadResourceBundle(strResourceBasename);
	}



	protected void setResourcePrefix(String strResourcePrefix)
	{
		m_strResourcePrefix = strResourcePrefix;
	}



	protected String getResourcePrefix()
	{
		return m_strResourcePrefix;
	}


	@BeforeEach
	protected void setUp()
		throws Exception
	{
		if (getTestProvider())
		{
			String	strClassName = getClassName();
			Class	cls = Class.forName(strClassName);
			m_provider = cls.newInstance();
		}
	}



	protected Object getProvider()
	{
		return m_provider;
	}



	protected boolean getTestProvider()
	{
		return true;
	}



	protected boolean getTestAudioSystem()
	{
		return true;
	}

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



	protected String getResourceString(String strKey)
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



/*** BaseProviderTestCase.java ***/

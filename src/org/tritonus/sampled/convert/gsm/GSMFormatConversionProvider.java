/*
 *	GSMFormatConversionProvider.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
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
 *
 */


package	org.tritonus.sampled.convert.gsm;


import	java.io.InputStream;
import	java.io.IOException;

import	java.util.Arrays;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.gsm.InvalidGSMFrameException;
import	org.tritonus.lowlevel.gsm.GSMDecoder;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.convert.TAsynchronousFilteredAudioInputStream;
import	org.tritonus.sampled.convert.TEncodingFormatConversionProvider;
import	org.tritonus.sampled.file.gsm.GSMEncoding;
import	org.tritonus.util.TCircularBuffer;



/**
 * ConversionProvider for GSM files.
 *
 * @author Matthias Pfisterer
 */
public class GSMFormatConversionProvider
	extends		TEncodingFormatConversionProvider
{
	private static final AudioFormat[]	INPUT_FORMATS =
	{
		new AudioFormat(GSMEncoding.GSM0610, 8000.0F, -1, 1, 33, 50.0F, false),
		new AudioFormat(GSMEncoding.GSM0610, 8000.0F, -1, 1, 33, 50.0F, true),
	};


	private static final AudioFormat[]	OUTPUT_FORMATS =
	{
		new AudioFormat(8000.0F, 16, 1, true, false),
		new AudioFormat(8000.0F, 16, 1, true, true),
	};

	/**	This is the size of the circular buffer.
	 *	This value is in bytes. It is
	 *	chosen so that one (decoded) GSM frame fits into the buffer.
	 *	GSM frames contain 160 samples.
	 */
	private static final int	BUFFER_SIZE = 320;

	private static final int	ENCODED_GSM_FRAME_SIZE = 33;



	/**	Constructor.
	 */
	public GSMFormatConversionProvider()
	{
		super(Arrays.asList(INPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS));
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("GSMFormatConversionProvider.<init>(): called");
		}
	}



	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream audioInputStream)
	{
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("GSMFormatConversionProvider.getAudioInputStream(): called");
		}
		if (isConversionSupported(targetFormat,
					  audioInputStream.getFormat()))
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("GSMFormatConversionProvider.getAudioInputStream(): conversion supported; trying to create DecodedGSMAudioInputStream");
			}
			return new DecodedGSMAudioInputStream(
				targetFormat,
				audioInputStream);
		}
		else
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("GSMFormatConversionProvider.getAudioInputStream(): conversion not supported; throwing IllegalArgumentException");
			}
			throw new IllegalArgumentException("conversion not supported");
		}
	}



	public static class DecodedGSMAudioInputStream
		extends		TAsynchronousFilteredAudioInputStream
	{
		private InputStream		m_encodedStream;
		private GSMDecoder		m_decoder;

		/*
		 *	Holds one encoded GSM frame.
		 */
		private byte[]			m_abFrameBuffer;
		private byte[]			m_abBuffer;



		public DecodedGSMAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream)
		{
			// TODO: try to find out length (possible?)
			super(outputFormat, -1);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("DecodedGSMAudioInputStream.<init>(): called");
			}
			m_encodedStream = inputStream;
			m_decoder = new GSMDecoder();
			m_abFrameBuffer = new byte[ENCODED_GSM_FRAME_SIZE];
			m_abBuffer = new byte[BUFFER_SIZE];
		}



		public void execute()
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("DecodedGSMAudioInputStream.execute(): called");
			}
			try
			{
				int	nRead = m_encodedStream.read(m_abFrameBuffer);
				/*
				 *	Currently, we take all kinds of errors
				 *	as end of stream.
				 */
				if (nRead != ENCODED_GSM_FRAME_SIZE)
				{
					if (TDebug.TraceAudioConverter)
					{
						TDebug.out("DecodedGSMAudioInputStream.execute(): not read whole GSM frame (" + nRead + ")");
					}
					m_circularBuffer.close();
					return;
				}
			}
			catch (IOException e)
			{
				TDebug.out(e);
				m_circularBuffer.close();
				return;
			}
			int[]	anDecodedData = null;
			try
			{
				anDecodedData = m_decoder.decode(m_abFrameBuffer);
			}
			catch (InvalidGSMFrameException e)
			{
				TDebug.out(e);
				m_circularBuffer.close();
				return;
			}
			for (int i = 0; i < 160; i++)
			{
				short	sSample = (short) (anDecodedData[i] & 0xFFFF);
				if (isBigEndian())
				{
					TConversionTool.convertShortToBigEndianBytes(sSample, m_abBuffer, i * 2);
				}
				else
				{
					TConversionTool.convertShortToLittleEndianBytes(sSample, m_abBuffer, i * 2);
				}
			}
			m_circularBuffer.write(m_abBuffer);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("DecodedGSMAudioInputStream.execute(): decoded GSM frame written");
			}
		}



		private boolean isBigEndian()
		{
			return getFormat().isBigEndian();
		}



		public void close()
			throws	IOException
		{
			super.close();
			m_encodedStream.close();
		}
	}
}



/*** GSMFormatConversionProvider.java ***/

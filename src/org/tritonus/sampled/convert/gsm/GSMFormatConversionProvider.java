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
import	javax.sound.sampled.AudioSystem;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.gsm.InvalidGSMFrameException;
import	org.tritonus.lowlevel.gsm.GSMDecoder;
import	org.tritonus.lowlevel.gsm.Encoder;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.convert.TAsynchronousFilteredAudioInputStream;
import	org.tritonus.sampled.convert.TEncodingFormatConversionProvider;
import	org.tritonus.sampled.convert.TSimpleFormatConversionProvider;
import	org.tritonus.sampled.Encodings;
import	org.tritonus.util.TCircularBuffer;



/**
 * ConversionProvider for GSM files.
 *
 * @author Matthias Pfisterer
 */
public class GSMFormatConversionProvider
	extends		TSimpleFormatConversionProvider
// extends		TEncodingFormatConversionProvider
{
	private static final AudioFormat[]	FORMATS1 =
	{
		new AudioFormat(Encodings.getEncoding("GSM0610"), 8000.0F, -1, 1, 33, 50.0F, false),
		new AudioFormat(Encodings.getEncoding("GSM0610"), 8000.0F, -1, 1, 33, 50.0F, true),
		// temporary only
		new AudioFormat(Encodings.getEncoding("PCM_SIGNED"), 8000.0F, 16, 1, 2, 8000.0F, false),
		new AudioFormat(Encodings.getEncoding("PCM_SIGNED"), 8000.0F, 16, 1, 2, 8000.0F, true),
	};

/*
  private static final AudioFormat[]	FORMATS2 =
  {
  new AudioFormat(8000.0F, 16, 1, true, false),
  new AudioFormat(8000.0F, 16, 1, true, true),
  };
*/
	private static final AudioFormat[]	FORMATS2 = FORMATS1;



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
		super(Arrays.asList(FORMATS1),
		      Arrays.asList(FORMATS2));
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("GSMFormatConversionProvider.<init>(): called");
		}
	}



	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream audioInputStream)
	{
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("GSMFormatConversionProvider.getAudioInputStream():");
			TDebug.out("checking if conversion supported");
			TDebug.out("from: " + audioInputStream.getFormat());
			TDebug.out("to: " + targetFormat);
		}
		if (isConversionSupported(targetFormat,
					  audioInputStream.getFormat()))
		{
			if (targetFormat.getEncoding().equals(Encodings.getEncoding("PCM_SIGNED")))
			{
				if (TDebug.TraceAudioConverter)
				{
					TDebug.out("GSMFormatConversionProvider.getAudioInputStream():");
					TDebug.out("conversion supported; trying to create DecodedGSMAudioInputStream");
				}
				return new DecodedGSMAudioInputStream(
					targetFormat,
					audioInputStream);
			}
			else
			{
				if (TDebug.TraceAudioConverter)
				{
					TDebug.out("GSMFormatConversionProvider.getAudioInputStream():");
					TDebug.out("conversion supported; trying to create EncodedGSMAudioInputStream");
				}
				return new EncodedGSMAudioInputStream(
					targetFormat,
					audioInputStream);
			}
		}
		else
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("GSMFormatConversionProvider.getAudioInputStream():");
				TDebug.out("conversion not supported; throwing IllegalArgumentException");
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
			super(outputFormat,
			      inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED ? AudioSystem.NOT_SPECIFIED : inputStream.getFrameLength() / 33 * 160);
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
			        //$$fb 2000-08-13: adapted to new TConversionTool functions
			        TConversionTool.intToBytes16(anDecodedData[i], m_abBuffer, i * 2, isBigEndian());
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



	public static class EncodedGSMAudioInputStream
		extends		TAsynchronousFilteredAudioInputStream
	{
		private AudioInputStream	m_decodedStream;
		private Encoder			m_encoder;

		/*
		 *	Holds one block of decoded data.
		 */
		private byte[]			m_abBuffer;

		/*
		 *	Holds one block of decoded data.
		 */
		private short[]			m_asBuffer;

		/*
		 *	Holds one encoded GSM frame.
		 */
		private byte[]			m_abFrameBuffer;



		public EncodedGSMAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream)
		{
			super(outputFormat,
			      inputStream.getFrameLength() == AudioSystem.NOT_SPECIFIED ? AudioSystem.NOT_SPECIFIED : inputStream.getFrameLength() / 160 * 33);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("EncodedGSMAudioInputStream.<init>(): called");
			}
			m_decodedStream = inputStream;
			m_encoder = new Encoder();
			m_abBuffer = new byte[BUFFER_SIZE];
			m_asBuffer = new short[160];
			m_abFrameBuffer = new byte[ENCODED_GSM_FRAME_SIZE];
		}



		public void execute()
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("EncodedGSMAudioInputStream.execute(): called");
			}
			try
			{
				int	nRead = m_decodedStream.read(m_abBuffer);
				/*
				 *	Currently, we take all kinds of errors
				 *	as end of stream.
				 */
				if (nRead != m_abBuffer.length)
				{
					if (TDebug.TraceAudioConverter)
					{
						TDebug.out("EncodedGSMAudioInputStream.execute(): not read whole 160 sample block (" + nRead + ")");
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
			for (int i = 0; i < 160; i++)
			{
			        //$$fb 2000-08-13: adapted to new TConversionTool functions
			        m_asBuffer[i] = TConversionTool.bytesToShort16(m_abBuffer, i * 2, isBigEndian());
			}
			m_encoder.encode(m_asBuffer, m_abFrameBuffer);
			m_circularBuffer.write(m_abFrameBuffer);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("EncodedGSMAudioInputStream.execute(): encoded GSM frame written");
			}
		}



		private boolean isBigEndian()
		{
			return m_decodedStream.getFormat().isBigEndian();
		}



		public void close()
			throws	IOException
		{
			super.close();
			m_decodedStream.close();
		}
	}
}



/*** GSMFormatConversionProvider.java ***/

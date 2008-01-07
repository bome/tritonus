/*
 *	MpegFormatConversionProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.sampled.convert.javalayer;

import java.io.InputStream;
import java.io.IOException;

import java.util.Arrays;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.TConversionTool;
import org.tritonus.share.sampled.convert.TEncodingFormatConversionProvider;
import org.tritonus.share.sampled.convert.TAsynchronousFilteredAudioInputStream;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.Obuffer;

/**
 * ConversionProvider for decoding of mp3 files.
 *
 * @author Matthias Pfisterer
 */


public class MpegFormatConversionProvider
extends TEncodingFormatConversionProvider
{
	// only used as abbreviation
	public static final AudioFormat.Encoding	MPEG1L1 = new AudioFormat.Encoding("MPEG1L1");
	public static final AudioFormat.Encoding	MPEG1L2 = new AudioFormat.Encoding("MPEG1L2");
	public static final AudioFormat.Encoding	MPEG1L3 = new AudioFormat.Encoding("MPEG1L3");
	public static final AudioFormat.Encoding	MPEG2L1 = new AudioFormat.Encoding("MPEG2L1");
	public static final AudioFormat.Encoding	MPEG2L2 = new AudioFormat.Encoding("MPEG2L2");
	public static final AudioFormat.Encoding	MPEG2L3 = new AudioFormat.Encoding("MPEG2L3");
	public static final AudioFormat.Encoding	MPEG2DOT5L1 = new AudioFormat.Encoding("MPEG2DOT5L1");
	public static final AudioFormat.Encoding	MPEG2DOT5L2 = new AudioFormat.Encoding("MPEG2DOT5L2");
	public static final AudioFormat.Encoding	MPEG2DOT5L3 = new AudioFormat.Encoding("MPEG2DOT5L3");

	private static final AudioFormat.Encoding	PCM_SIGNED = AudioFormat.Encoding.PCM_SIGNED;


	/* TODO: mechanism to make the double specification with
	   different endianess obsolete. */
	private static final AudioFormat[]	INPUT_FORMATS =
	{
		// mono
		new AudioFormat(MPEG1L1, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG1L1, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG1L1, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG1L1, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG1L2, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG1L2, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG1L2, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG1L2, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG1L3, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG1L3, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG1L3, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG1L3, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2L1, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2L1, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2L1, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2L1, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2L2, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2L2, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2L2, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2L2, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2L3, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2L3, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2L3, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2L3, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2DOT5L1, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L1, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2DOT5L1, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L1, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2DOT5L2, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L2, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2DOT5L2, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L2, -1.0F, -1, 2, -1, -1.0F, true),

		// mono
		new AudioFormat(MPEG2DOT5L3, -1.0F, -1, 1, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L3, -1.0F, -1, 1, -1, -1.0F, true),
		// stereo
		new AudioFormat(MPEG2DOT5L3, -1.0F, -1, 2, -1, -1.0F, false),
		new AudioFormat(MPEG2DOT5L3, -1.0F, -1, 2, -1, -1.0F, true),
	};


	private static final AudioFormat[]	OUTPUT_FORMATS =
	{
		// mono, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 1, 2, -1.0F, true),

		// stereo, 16 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, true),

/*	24 and 32 bit not yet possible
		// mono, 24 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 24, 1, 3, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 24, 1, 3, -1.0F, true),

		// stereo, 24 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 24, 2, 6, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 24, 2, 6, -1.0F, true),

		// mono, 32 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 32, 1, 4, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 32, 1, 4, -1.0F, true),

		// stereo, 32 bit signed
		new AudioFormat(PCM_SIGNED, -1.0F, 32, 2, 8, -1.0F, false),
		new AudioFormat(PCM_SIGNED, -1.0F, 32, 2, 8, -1.0F, true),
*/
	};


// 	private static final boolean	t = true;
// 	private static final boolean	f = false;


	/**	Constructor.
	 */
	public MpegFormatConversionProvider()
	{
		super(Arrays.asList(INPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS)/*,
						     true, // new behaviour
						     false*/);
		// bidirectional .. constants UNIDIR../BIDIR..?
		if (TDebug.TraceAudioConverter) { TDebug.out("MpegFormatConversionProvider.<init>(): begin"); }
		if (TDebug.TraceAudioConverter) { TDebug.out("MpegFormatConversionProvider.<init>(): end"); }
	}



	@Override
	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream audioInputStream)
	{
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("MpegFormatConversionProvider.getAudioInputStream(AudioFormat, AudioInputStream):");
			TDebug.out("trying to convert");
			TDebug.out("\tfrom: " + audioInputStream.getFormat());
			TDebug.out("\tto: " + targetFormat);
		}
		AudioFormat	matchingFormat = getMatchingFormat(
			targetFormat,
			audioInputStream.getFormat());
// 		if (isConversionSupported(targetFormat,
// 					  audioInputStream.getFormat()))
		if (matchingFormat != null)
		{
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("MpegFormatConversionProvider.getAudioInputStream(AudioFormat, AudioInputStream):");
				TDebug.out("\tisConversionSupported() accepted it; now setting up the conversion");
			}
			targetFormat = setUnspecifiedFieldsFromProto(targetFormat, matchingFormat);
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("MpegFormatConversionProvider.getAudioInputStream(AudioFormat, AudioInputStream):");
				TDebug.out("\tcompleted target format (1. stage): " + targetFormat);
			}
			targetFormat = setUnspecifiedFieldsFromProto(targetFormat, audioInputStream.getFormat());
			if (TDebug.TraceAudioConverter)
			{
				TDebug.out("MpegFormatConversionProvider.getAudioInputStream(AudioFormat, AudioInputStream):");
				TDebug.out("\tcompleted target format (2. stage): " + targetFormat);
			}
			return new DecodedMpegAudioInputStream(
				targetFormat,
				audioInputStream);
		}
		else
		{
			throw new IllegalArgumentException("conversion not supported");
		}
	}


	// TODO: ask Florian if these methods are of general interest
	private static AudioFormat setUnspecifiedFieldsFromProto(
		AudioFormat incomplete,
		AudioFormat prototype)
	{
		AudioFormat	format = new AudioFormat(
			incomplete.getEncoding(),
			getSpecificValue(incomplete.getSampleRate(), prototype.getSampleRate()),
			getSpecificValue(incomplete.getSampleSizeInBits(), prototype.getSampleSizeInBits()),
			getSpecificValue(incomplete.getChannels(), prototype.getChannels()),
			getSpecificValue(incomplete.getFrameSize(), prototype.getFrameSize()),
			getSpecificValue(incomplete.getFrameRate(), prototype.getFrameRate()),
			incomplete.isBigEndian());
		return format;
	}



	private static float getSpecificValue(float fIncomplete, float fProto)
	{
		return (fIncomplete == AudioSystem.NOT_SPECIFIED) ? fProto : fIncomplete;
	}


	private static int getSpecificValue(int nIncomplete, int nProto)
	{
		return (nIncomplete == AudioSystem.NOT_SPECIFIED) ? nProto : nIncomplete;
	}



	public static class DecodedMpegAudioInputStream
	extends TAsynchronousFilteredAudioInputStream
	{
		private InputStream		m_encodedStream;
		private Bitstream		m_bitstream;
		private Decoder			m_decoder;
		private DMAISObuffer		m_oBuffer;



		public DecodedMpegAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream)
		{
			// TODO: try to find out length (possible?)
			super(outputFormat, AudioSystem.NOT_SPECIFIED);
			m_encodedStream = inputStream;
			m_bitstream = new Bitstream(inputStream);
			m_decoder = new Decoder(null);
			m_oBuffer = new DMAISObuffer(outputFormat.getChannels());
			m_decoder.setOutputBuffer(m_oBuffer);
		}



		public void execute()
		{
			try
			{
				Header	header = m_bitstream.readFrame();
				if (header == null)
				{
					if (TDebug.TraceAudioConverter)
					{
						TDebug.out("header is null (end of mpeg stream)");
					}
					getCircularBuffer().close();
					return;
				}
				//$$fb decodeOutput not needed
				/*Obuffer	decoderOutput =*/ m_decoder.decodeFrame(header, m_bitstream);
				m_bitstream.closeFrame();
				getCircularBuffer().write(m_oBuffer.getBuffer(), 0, m_oBuffer.getCurrentBufferSize());
				m_oBuffer.reset();
			}
			catch (BitstreamException e)
			{
				if (TDebug.TraceAudioConverter || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
			catch (DecoderException e)
			{
				if (TDebug.TraceAudioConverter || TDebug.TraceAllExceptions)
				{
					TDebug.out(e);
				}
			}
		}



		protected boolean isBigEndian()
		{
			return getFormat().isBigEndian();
		}



		@Override
		public void close()
			throws IOException
		{
			super.close();
			m_encodedStream.close();
		}



		private class DMAISObuffer
		extends Obuffer
		{
			private int			m_nChannels;
			private byte[]			m_abBuffer;
			private int[]			m_anBufferPointers;
			private boolean			m_bIsBigEndian;



			public DMAISObuffer(int	nChannels)
			{
				m_nChannels = nChannels;
				m_abBuffer = new byte[OBUFFERSIZE * nChannels];
				m_anBufferPointers = new int[nChannels];
				reset();
				m_bIsBigEndian = DecodedMpegAudioInputStream.this.isBigEndian();
			}



			@Override
			public void append(int nChannel, short sValue)
			{
				// TODO: replace by TConversionTool methods
/*
				byte	bFirstByte;
				byte	bSecondByte;
				if (m_bIsBigEndian)
				{
					bFirstByte = (byte) ((sValue >>> 8) & 0xFF);
					bSecondByte = (byte) (sValue & 0xFF);
				}
				else	// little endian
				{
					bFirstByte = (byte) (sValue & 0xFF);
					bSecondByte = (byte) ((sValue >>> 8) & 0xFF);
				}
				m_abBuffer[m_anBufferPointers[nChannel]] = bFirstByte;
				m_abBuffer[m_anBufferPointers[nChannel] + 1] = bSecondByte;
*/
				TConversionTool.shortToBytes16(sValue, m_abBuffer, m_anBufferPointers[nChannel], m_bIsBigEndian);
				m_anBufferPointers[nChannel] += m_nChannels * 2;
			}


			@Override
			public void set_stop_flag()
			{
			}



			@Override
			public void close()
			{
			}



			@Override
			public void write_buffer(int nValue)
			{
			}



			@Override
			public void clear_buffer()
			{
			}



			public byte[] getBuffer()
			{
				return m_abBuffer;
			}



			public int getCurrentBufferSize()
			{
				return m_anBufferPointers[0];
			}



			public void reset()
			{
				for (int i = 0; i < m_nChannels; i++)
				{
					/*	Points to byte location,
					 *	implicitely assuming 16 bit
					 *	samples.
					 */
					m_anBufferPointers[i] = i * 2;
				}
			}


			
		}
	}
}



/*** MpegFormatConversionProvider.java ***/

/*
 *	UlawFormatConversionProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999,2000 by Florian Bomers <http://www.bomers.de>
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


package org.tritonus.sampled.convert;


import java.util.Arrays;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.AudioFormats;
import org.tritonus.share.sampled.TConversionTool;
import org.tritonus.share.sampled.convert.TEncodingFormatConversionProvider;
import org.tritonus.share.sampled.convert.TSynchronousFilteredAudioInputStream;

/**
 * This provider (currently) supports these conversions:
 * <ul><li>PCM 8 Signed -> ulaw
 * <li>PCM 8 Unsigned -> ulaw
 * <li>PCM 16 signed big endian -> ulaw
 * <li>PCM 16 signed little endian -> ulaw
 * <li>alaw -> ulaw
 * </ul>
 * and vice versa.
 * <p>FrameRate, SampleRate, Channels CANNOT be converted.
 *
 * @author Florian Bomers
 */

public class UlawFormatConversionProvider
extends TEncodingFormatConversionProvider {

	private static final int ALL=AudioSystem.NOT_SPECIFIED;

	public static AudioFormat.Encoding ENC_PCM_SIGNED=new AudioFormat.Encoding("PCM_SIGNED");
	public static AudioFormat.Encoding ENC_PCM_UNSIGNED=new AudioFormat.Encoding("PCM_UNSIGNED");
	public static AudioFormat.Encoding ENC_ULAW=new AudioFormat.Encoding("ULAW");
	public static AudioFormat.Encoding ENC_ALAW=new AudioFormat.Encoding("ALAW");

	// TODO:
	// make a superclass that takes 2 arrays in the constructor
	// then, it checks whether conversion from the first array to the second
	// or vice versa is possible.
	// in the current implementation, isConversionSupported also returns true
	// for e.g. source=ALAW and target=PCM_SIGNED
	// when the converted stream is actually requested, an exception occurs for these cases
	// this new superclass wouldn't distinguish between source and target formats.

	private static final AudioFormat[]	OUTPUT_FORMATS = {
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_UNSIGNED, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_UNSIGNED, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 8, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 8, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_PCM_UNSIGNED, ALL, 8, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_PCM_UNSIGNED, ALL, 8, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 16, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 16, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 16, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_PCM_SIGNED, ALL, 16, ALL, ALL, ALL, true),
	    new AudioFormat(ENC_ALAW, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_ALAW, ALL, 8, ALL, ALL, ALL, true),

	    new AudioFormat(ENC_ULAW, ALL, 8, ALL, ALL, ALL, false),
	    new AudioFormat(ENC_ULAW, ALL, 8, ALL, ALL, ALL, true)
	};

	/** Constructor.
	 */
	public UlawFormatConversionProvider() {
		super(Arrays.asList(OUTPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS));
	}

	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream) {
		AudioFormat sourceFormat=sourceStream.getFormat();
		// the non-conversion case
		// TODO: does this work OK when some fields are AudioSystem.NOT_SPECIFIED ?
		if (AudioFormats.matches(sourceFormat, targetFormat)) {
			return sourceStream;
		}
		if (doMatch(targetFormat.getFrameRate(), sourceFormat.getFrameRate())
		        && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) {
			if (doMatch(targetFormat.getSampleSizeInBits(),8)
			        && targetFormat.getEncoding().equals(ENC_ULAW)) {
				// OK, the targetFormat seems fine, so we convert it to ULAW
				// let the remaining checks be done by ToUlawStream
				return new ToUlawStream(sourceStream);
			} else
				if (doMatch(sourceFormat.getSampleSizeInBits(),8)
				        && sourceFormat.getEncoding().equals(ENC_ULAW)) {
					// convert ULAW to the target format
					return new FromUlawStream(sourceStream, targetFormat);
				}
		}
		throw new IllegalArgumentException("format conversion not supported");
	}

	private static final int UNSIGNED8=1;
	private static final int SIGNED8=2;
	private static final int BIG_ENDIAN16=3;
	private static final int LITTLE_ENDIAN16=4;
	private static final int ALAW=5;

	private boolean isSupportedFormat(AudioFormat format) {
		return getConvertType(format)!=0;
	}

	private int getConvertType(AudioFormat af) {
		int result=0;
		AudioFormat.Encoding encoding=af.getEncoding();
		boolean bigEndian=af.isBigEndian();
		int ssib=af.getSampleSizeInBits();
		// now set up the convert type
		if (encoding.equals(ENC_PCM_SIGNED)) {
			if (ssib==16) {
				if (bigEndian) {
					result=BIG_ENDIAN16;
				} else {
					result=LITTLE_ENDIAN16;
				}
			} else
				if (ssib==8) {
					result=SIGNED8;
				}
		} else
			if (encoding.equals(ENC_PCM_UNSIGNED)) {
				if (ssib==8) {
					result=UNSIGNED8;
				}
			} else
				if (encoding.equals(ENC_ALAW)) {
					result=ALAW;
				}
		return result;
	}

	class ToUlawStream extends TSynchronousFilteredAudioInputStream {
		private int convertType;

		public ToUlawStream(AudioInputStream sourceStream) {
			// transform the targetFormat so that
			// FrameRate, SampleRate, and Channels match the sourceFormat
			// we only retain encoding, samplesize and endian of targetFormat.
			super (sourceStream, new AudioFormat(
			           ENC_ULAW,
			           sourceStream.getFormat().getSampleRate(),
			           8,
			           sourceStream.getFormat().getChannels(),
			           sourceStream.getFormat().getChannels(), // sampleSize
			           sourceStream.getFormat().getFrameRate(),
			           sourceStream.getFormat().isBigEndian()));
			convertType=getConvertType(sourceStream.getFormat());
			if (sourceStream.getFormat().getSampleSizeInBits() == 8) {
				enableConvertInPlace();
			}

			if (convertType==0)
				throw new IllegalArgumentException("format conversion not supported");
		}

		protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
			int sampleCount=inFrameCount*getFormat().getChannels();
			switch (convertType) {
			case UNSIGNED8:
				TConversionTool.pcm82ulaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
				break;
			case SIGNED8:
				TConversionTool.pcm82ulaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
				break;
			case BIG_ENDIAN16:
				TConversionTool.pcm162ulaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
				break;
			case LITTLE_ENDIAN16:
				TConversionTool.pcm162ulaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
				break;
			case ALAW:
				TConversionTool.alaw2ulaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
				break;
			}
			return inFrameCount;
		}

		protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
			int sampleCount=frameCount*getFormat().getChannels();
			switch (convertType) {
			case UNSIGNED8:
				TConversionTool.pcm82ulaw
				(buffer, byteOffset, sampleCount, false);
				break;
			case SIGNED8:
				TConversionTool.pcm82ulaw
				(buffer, byteOffset, sampleCount, true);
				break;
			case ALAW:
				TConversionTool.alaw2ulaw
				(buffer, byteOffset, sampleCount);
				break;
			default:
				throw new RuntimeException
				("ToUlawStream: Call to convertInPlace, but it cannot convert in place.");
			}
		}
	}

	class FromUlawStream extends TSynchronousFilteredAudioInputStream {
		private int convertType;

		public FromUlawStream(AudioInputStream sourceStream, AudioFormat targetFormat) {
			// transform the targetFormat so that
			// FrameRate, SampleRate, and Channels match the sourceFormat
			// we only retain encoding, samplesize and endian of targetFormat.
			super (sourceStream, new AudioFormat(
			           targetFormat.getEncoding(),
			           sourceStream.getFormat().getSampleRate(),
			           targetFormat.getSampleSizeInBits(),
			           sourceStream.getFormat().getChannels(),
			           targetFormat.getSampleSizeInBits()*sourceStream.getFormat().getChannels()/8,
			           sourceStream.getFormat().getFrameRate(),
			           targetFormat.isBigEndian()));
			convertType=getConvertType(getFormat());
			if (targetFormat.getSampleSizeInBits() == 8) {
				enableConvertInPlace();
			}
			if (convertType==0)
				throw new IllegalArgumentException("format conversion not supported");
		}

		protected int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount) {
			int sampleCount=inFrameCount*getFormat().getChannels();
			switch (convertType) {
			case UNSIGNED8:
				TConversionTool.ulaw2pcm8
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
				break;
			case SIGNED8:
				TConversionTool.ulaw2pcm8
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
				break;
			case BIG_ENDIAN16:
				TConversionTool.ulaw2pcm16
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, true);
				break;
			case LITTLE_ENDIAN16:
				TConversionTool.ulaw2pcm16
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount, false);
				break;
			case ALAW:
				TConversionTool.ulaw2alaw
				(inBuffer, 0, outBuffer, outByteOffset, sampleCount);
				break;
			}
			return inFrameCount;
		}

		protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount) {
			int sampleCount=frameCount*format.getChannels();
			switch (convertType) {
			case UNSIGNED8:
				TConversionTool.ulaw2pcm8
				(buffer, byteOffset, sampleCount, false);
				break;
			case SIGNED8:
				TConversionTool.ulaw2pcm8
				(buffer, byteOffset, sampleCount, true);
				break;
			case ALAW:
				TConversionTool.ulaw2alaw
				(buffer, byteOffset, sampleCount);
				break;
			default:
				throw new RuntimeException
				("FromUlawStream: Call to convertInPlace, but it cannot convert in place.");
			}
		}
	}

}

/*** UlawFormatConversionProvider.java ***/

/*
 *	Mp3LameFormatConversionProvider.java
 */

/*
 *  Copyright (c) 2000 by Florian Bomers <florian@bome.com>
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


package	org.tritonus.sampled.convert;


import	java.io.File;
import	java.io.FileOutputStream;
import	java.io.ByteArrayInputStream;
import	java.io.InputStream;
import	java.io.IOException;
import	java.io.OutputStream;

import	java.util.Arrays;
import	java.util.Iterator;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.spi.FormatConversionProvider;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.Encodings;
import	org.tritonus.util.TCircularBuffer;
import	org.tritonus.util.AudioFormatSet;

import org.tritonus.lowlevel.lame.Lame;

/**
 * ConversionProvider for encoding MP3 audio files with the lame lib.
 *
 * @author Florian Bomers
 */

//TODO: - handle other sample rates ?
//      - fill frame rate, frame size. Use frame rate for bit rate ?
//      - don't replace sample size in bits, frame rate and frame size in 
//        returned AudioFormats.
//      - add decoding ? more work on LAME itself...
//      - byte swapping support in LAME ?

public class Mp3LameFormatConversionProvider
	extends	TSimpleFormatConversionProvider {
	
	// estimated maximum size for an mpeg encoded frame
	private static final int ENCODER_MAX_FRAME_SIZE=2048*16;
	private static final int ENCODER_PCM_BUFFER_SIZE=2048*16;

	private static final int ALL=AudioSystem.NOT_SPECIFIED;

	public static final AudioFormat.Encoding MPEG1L3 = Encodings.getEncoding("MPEG1L3");
	// Lame converts automagically to MPEG2 or MPEG2.5, if necessary.
	//public static final AudioFormat.Encoding MPEG2L3 = Encodings.getEncoding("MPEG2L3");
	//public static final AudioFormat.Encoding MPEG2DOT5L3 = Encodings.getEncoding("MPEG2DOT5L3");

	/*
	 * Lame provides these formats:
	 * MPEG1 layer III samplerates(kHz): 32 44.1 48 
	 * bitrates(kbs): 32 40 48 56 64 80 96 112 128 160 192 224 256 320 
	 *
	 * MPEG2 layer III samplerates(kHz): 16 22.05 24 
	 * bitrates(kbs): 8 16 24 32 40 48 56 64 80 96 112 128 144 160 
	 *
	 * MPEG2.5 layer III samplerates(kHz): 8 11.025 12 
	 * bitrates(kbs): 8 16 24 32 40 48 56 64 80 96 112 128 144 160 
	 */

	private static final AudioFormat[] OUTPUT_FORMATS = {
		new AudioFormat(MPEG1L3, 8000, ALL, 1, ALL, ALL, false), //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 8000, ALL, 2, ALL, ALL, false), //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 8000, ALL, 1, ALL, ALL, true),  //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 8000, ALL, 2, ALL, ALL, true),  //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 11025, ALL, 1, ALL, ALL, false),//MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 11025, ALL, 2, ALL, ALL, false),//MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 11025, ALL, 1, ALL, ALL, true), //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 11025, ALL, 2, ALL, ALL, true), //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 12000, ALL, 1, ALL, ALL, false),//MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 12000, ALL, 2, ALL, ALL, false),//MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 12000, ALL, 1, ALL, ALL, true), //MPEG2DOT5L3
		new AudioFormat(MPEG1L3, 12000, ALL, 2, ALL, ALL, true), //MPEG2DOT5L3

		new AudioFormat(MPEG1L3, 16000, ALL, 1, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 16000, ALL, 2, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 16000, ALL, 1, ALL, ALL, true),     //MPEG2L3
		new AudioFormat(MPEG1L3, 16000, ALL, 2, ALL, ALL, true),     //MPEG2L3
		new AudioFormat(MPEG1L3, 22050, ALL, 1, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 22050, ALL, 2, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 22050, ALL, 1, ALL, ALL, true),     //MPEG2L3
		new AudioFormat(MPEG1L3, 22050, ALL, 2, ALL, ALL, true),     //MPEG2L3
		new AudioFormat(MPEG1L3, 24000, ALL, 1, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 24000, ALL, 2, ALL, ALL, false),    //MPEG2L3
		new AudioFormat(MPEG1L3, 24000, ALL, 1, ALL, ALL, true),     //MPEG2L3
		new AudioFormat(MPEG1L3, 24000, ALL, 2, ALL, ALL, true),     //MPEG2L3

		new AudioFormat(MPEG1L3, 32000, ALL, 1, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 32000, ALL, 2, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 32000, ALL, 1, ALL, ALL, true),
		new AudioFormat(MPEG1L3, 32000, ALL, 2, ALL, ALL, true),
		new AudioFormat(MPEG1L3, 44100, ALL, 1, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 44100, ALL, 2, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 44100, ALL, 1, ALL, ALL, true),
		new AudioFormat(MPEG1L3, 44100, ALL, 2, ALL, ALL, true),
		new AudioFormat(MPEG1L3, 48000, ALL, 1, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 48000, ALL, 2, ALL, ALL, false),
		new AudioFormat(MPEG1L3, 48000, ALL, 1, ALL, ALL, true),
		new AudioFormat(MPEG1L3, 48000, ALL, 2, ALL, ALL, true),
	};

	private static final AudioFormat[] INPUT_FORMATS = {
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 11025, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 11025, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 12000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 12000, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 24000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 24000, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 32000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 32000, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, ALL, ALL, ALL, true),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, ALL, ALL, ALL, false),
	    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 48000, 16, ALL, ALL, ALL, true),
	};


	/**
	 * Constructor.
	 */
	public Mp3LameFormatConversionProvider() {
		super(Arrays.asList(INPUT_FORMATS),
		      Arrays.asList(OUTPUT_FORMATS));
		if (!Lame.isLibAvailable()) {
			disable();
			//if (TDebug.TraceAudioConverter) {
			TDebug.out("******* Error initializing LAME mp3 encoder: "+Lame.getLinkError());
			//}
		}
	}


	public AudioInputStream getAudioInputStream(AudioFormat targetFormat, 
						    AudioInputStream audioInputStream) {
		if (isConversionSupported(targetFormat,
		                          audioInputStream.getFormat())) {
			return new EncodedMpegAudioInputStream(
			           targetFormat,
			           audioInputStream);
		} else {
			throw new IllegalArgumentException("conversion not supported");
		}
	}

	public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding,
	                                      AudioFormat sourceFormat) {
		if (TDebug.TraceAudioConverter) {
			TDebug.out(">MP3Lame getTargetFormats(AudioFormat.Encoding, AudioFormat):");
			TDebug.out("checking out possible target formats");
			TDebug.out("from: " + sourceFormat);
			TDebug.out("to  : " + targetEncoding);
		}
		if (isConversionSupported(targetEncoding, sourceFormat)) {
			AudioFormatSet result=new AudioFormatSet();
			Iterator iterator=getCollectionTargetFormats().iterator();
			while (iterator.hasNext()) {
				AudioFormat targetFormat = (AudioFormat) iterator.next();
				//if (TDebug.TraceAudioConverter) {
				//TDebug.out("-checking target format "+targetFormat);
				//}
				if (doMatch(targetFormat.getSampleRate(), sourceFormat.getSampleRate())
				    && targetFormat.getEncoding().equals(targetEncoding)
				    && doMatch(targetFormat.getChannels(), sourceFormat.getChannels())) {
					targetFormat=replaceNotSpecified(sourceFormat, targetFormat);
					//if (TDebug.TraceAudioConverter) {
					//TDebug.out("-yes. added "+targetFormat);
					//}
					result.add(targetFormat);
				} //else {
				//if (TDebug.TraceAudioConverter) {
				//boolean e=targetFormat.getEncoding().equals(targetEncoding);
				//TDebug.out("-no. \""+targetFormat.getEncoding()+"\"==\""+targetEncoding+"\" ? "+e);
				//}
				//}
			}

			if (TDebug.TraceAudioConverter) {
				TDebug.out("<found "+result.size()+" matching formats.");
			}
			return result.toAudioFormatArray();
		} else {
			if (TDebug.TraceAudioConverter) {
				TDebug.out("<returning empty array.");
			}
			return EMPTY_FORMAT_ARRAY;
		}
	}

	public static class EncodedMpegAudioInputStream
		extends TAsynchronousFilteredAudioInputStream {
		private InputStream pcmStream;
		private Lame encoder;

		private byte[] pcmBuffer;
		private byte[] encodedBuffer;

		public EncodedMpegAudioInputStream(AudioFormat outputFormat, AudioInputStream inputStream) {
			super(outputFormat, -1);
			pcmStream = inputStream;
			encoder=new Lame(inputStream.getFormat());
			pcmBuffer=new byte[ENCODER_PCM_BUFFER_SIZE];
			encodedBuffer=new byte[ENCODER_MAX_FRAME_SIZE];
		}

		public void execute() {
			try {
				if (encoder==null) {
					if (TDebug.TraceAudioConverter) {
						TDebug.out("mp3 lame encoder is null (already at end of stream)");
					}
					m_circularBuffer.close();
					return;
				}
				int encodedBytes=0;
				byte[] buffer=null;
				while (encodedBytes==0 && encoder!=null) {
					int readBytes=pcmStream.read(pcmBuffer);
					// what to do in case of readBytes==0 ?
					if (readBytes>0) {
						encodedBytes=encoder.encodeBuffer(pcmBuffer, 0, 
										  readBytes, 
										  encodedBuffer);
						buffer=encodedBuffer;
					} else {
						// take the larger buffer for the remaining frame(s)
						buffer=encodedBuffer.length>pcmBuffer.length?encodedBuffer:pcmBuffer;
						encodedBytes=encoder.encodeFinish(buffer);
						encoder.close();
						encoder=null;
					}
				}
				if (encodedBytes>0) {
					m_circularBuffer.write(buffer, 0, encodedBytes);
				}
				if (encoder==null) {
					m_circularBuffer.close();
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (TDebug.TraceAudioConverter) {
					TDebug.out(e);
				}
			}
			catch (IOException e) {
				if (TDebug.TraceAudioConverter) {
					TDebug.out(e);
				}
			}
		}

		private boolean isBigEndian() {
			return getFormat().isBigEndian();
		}

		public void close() throws IOException {
			super.close();
			pcmStream.close();
			if (encoder!=null) {
				encoder.encodeFinish(null);
				encoder.close();
				encoder=null;
			}
		}
	}
}

/*** Mp3LameFormatConversionProvider.java ***/

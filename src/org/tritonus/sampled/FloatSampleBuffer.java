/*
 * FloatSampleBuffer.java
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

package	org.tritonus.sampled;

import	java.util.ArrayList;
import	java.util.Iterator;
import	java.util.Random;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.spi.AudioFileWriter;

import	org.tritonus.TDebug;

/**
 * A class for small buffers of samples in floating
 * point format. All samples are normalized to the
 * interval [-1.0...1.0].
 *
 * @author Florian Bomers
 */

public class FloatSampleBuffer {

	private ArrayList channels=null; // contains for each channel a float array
	private int sampleCount=0;
	private float sampleRate=0;
	private int originalFormatType=0;

	// dithering
	// discussion about dithering:
	// http://www.iqsoft.com/IQSMagazine/BobsSoapbox/Dithering.htm
	// and http://www.iqsoft.com/IQSMagazine/BobsSoapbox/Dithering2.htm

	/** Constant for setDitherMode: dithering will be enabled if sample size is decreased */
	public static final int DITHER_MODE_AUTOMATIC=0;
	/** Constant for setDitherMode: dithering will be done */
	public static final int DITHER_MODE_ON=1;
	/** Constant for setDitherMode: dithering will not be done */
	public static final int DITHER_MODE_OFF=2;
	private static Random random=null;
	private float ditherBits=0.7f;
	private boolean doDither=false; // set in convertFloatToBytes
	// e.g. the sample rate converter may want to force dithering
	private int ditherMode=DITHER_MODE_AUTOMATIC;

	public FloatSampleBuffer() {
		this(0,0,1);
	}

	/**
	 */
	public FloatSampleBuffer(int channelCount, int sampleCount, float sampleRate) {
		init(channelCount, sampleCount, sampleRate);
	}

	protected void init(int channelCount, int sampleCount, float sampleRate) {
		if (channelCount<0 || sampleCount<0) {
			throw new IllegalArgumentException
			("Invalid parameters in initialization of FloatSampleBuffer.");
		}
		setSampleRate(sampleRate);
		if (getSampleCount()!=sampleCount || getChannelCount()!=channelCount) {
			this.channels=new ArrayList();
			this.sampleCount=sampleCount;
			createChannels(channelCount);
		}
	}

	public FloatSampleBuffer(byte[] buffer, int offset, int byteCount,
	                         AudioFormat format) {
		this(format.getChannels(),
		     byteCount/(format.getSampleSizeInBits()/8*format.getChannels()),
		     format.getSampleRate());
		initFromByteArray(buffer, offset, byteCount, format);
	}

	private void createChannels(int channelCount) {
		channels.clear();
		for (int ch=0; ch<channelCount; ch++) {
			addChannel(false);
		}
	}


	public void initFromByteArray(byte[] buffer, int offset, int byteCount,
	                              AudioFormat format) {
		if (offset+byteCount>buffer.length) {
			throw new IllegalArgumentException
			("FloatSampleBuffer.initFromByteArray: buffer too small.");
		}
		boolean signed=format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
		if (!signed &&
		        !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
			throw new IllegalArgumentException
			("FloatSampleBuffer: only PCM samples are possible.");
		}
		int bytesPerSample=format.getSampleSizeInBits()/8;
		int bytesPerFrame=bytesPerSample*format.getChannels();
		int thisSampleCount=byteCount/bytesPerFrame;
		init(format.getChannels(), thisSampleCount, format.getSampleRate());
		int formatType=getFormatType(format.getSampleSizeInBits(),
		                             signed, format.isBigEndian());
		// save format for automatic dithering mode
		originalFormatType=formatType;
		for (int ch=0; ch<format.getChannels(); ch++) {
			convertByteToFloat(buffer, offset, sampleCount, getChannel(ch),
			                   bytesPerFrame, formatType);
			offset+=bytesPerSample; // next channel
		}

	}

	// sample width (must be in order !)
	private static final int F_8=1;
	private static final int F_16=2;
	private static final int F_24=3;
	private static final int F_32=4;
	private static final int F_SAMPLE_WIDTH_MASK=F_8 | F_16 | F_24 | F_32;
	// format bit-flags
	private static final int F_SIGNED=8;
	private static final int F_BIGENDIAN=16;

	// supported formats
	private static final int CT_8S=F_8 | F_SIGNED;
	private static final int CT_8U=F_8;
	private static final int CT_16SB=F_16 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_16SL=F_16 | F_SIGNED;
	private static final int CT_24SB=F_24 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_24SL=F_24 | F_SIGNED;
	private static final int CT_32SB=F_32 | F_SIGNED | F_BIGENDIAN;
	private static final int CT_32SL=F_32 | F_SIGNED;

	public int getFormatType(int ssib, boolean signed, boolean bigEndian) {
		int bytesPerSample=ssib/8;
		if (bytesPerSample*8!=ssib) {
			throw new IllegalArgumentException
			("FloatSampleBuffer: unsupported sample size of "
			 +ssib+" bits per sample.");
		}
		if (!signed && bytesPerSample>1) {
			throw new IllegalArgumentException
			("FloatSampleBuffer: unsigned samples larger than "
			 +"8 bit are not supported");
		}
		int res=0;
		if (ssib==8) {
			res=F_8;
		} else if (ssib==16) {
			res=F_16;
		} else if (ssib==24) {
			res=F_24;
		} else if (ssib==32) {
			res=F_32;
		}
		if (signed) {
			res|=F_SIGNED;
		}
		if (bigEndian && (ssib!=8)) {
			res|=F_BIGENDIAN;
		}
		return res;
	}

	private static final float twoPower7=128.0f;
	private static final float twoPower15=32768.0f;
	private static final float twoPower23=8388608.0f;
	private static final float twoPower31=2147483648.0f;

	private static final float invTwoPower7=1/twoPower7;
	private static final float invTwoPower15=1/twoPower15;
	private static final float invTwoPower23=1/twoPower23;
	private static final float invTwoPower31=1/twoPower31;

	/*public*/
	static void convertByteToFloat(byte[] input, int offset, int sampleCount,
	                               float[] output, int bytesPerFrame,
	                               int formatType) {
		//if (TDebug.TraceAudioConverter) {
		//    TDebug.out("FloatSampleBuffer.convertByteToFloat, formatType="
		//           +formatType2Str(formatType));
		//}
		int sample;
		for (sample=0; sample<sampleCount; sample++) {
			// do conversion
			switch (formatType) {
			case CT_8S:
				output[sample]=
				    ((float) input[offset])*invTwoPower7;
				break;
			case CT_8U:
				output[sample]=
				    ((float) ((input[offset] & 0xFF)-128))*invTwoPower7;
				break;
			case CT_16SB:
				output[sample]=
				    ((float) ((input[offset]<<8)
				              | (input[offset+1] & 0xFF)))*invTwoPower15;
				break;
			case CT_16SL:
				output[sample]=
				    ((float) ((input[offset+1]<<8)
				              | (input[offset] & 0xFF)))*invTwoPower15;
				break;
			case CT_24SB:
				output[sample]=
				    ((float) ((input[offset]<<16)
				              | ((input[offset+1] & 0xFF)<<8)
				              | (input[offset+2] & 0xFF)))*invTwoPower23;
				break;
			case CT_24SL:
				output[sample]=
				    ((float) ((input[offset+2]<<16)
				              | ((input[offset+1] & 0xFF)<<8)
				              | (input[offset] & 0xFF)))*invTwoPower23;
				break;
			case CT_32SB:
				output[sample]=
				    ((float) ((input[offset]<<24)
				              | ((input[offset+1] & 0xFF)<<16)
				              | ((input[offset+2] & 0xFF)<<8)
				              | (input[offset+3] & 0xFF)))*invTwoPower31;
				break;
			case CT_32SL:
				output[sample]=
				    ((float) ((input[offset+3]<<24)
				              | ((input[offset+2] & 0xFF)<<16)
				              | ((input[offset+1] & 0xFF)<<8)
				              | (input[offset] & 0xFF)))*invTwoPower31;
				break;
			default:
				throw new IllegalArgumentException
				("Unsupported formatType="+formatType);
			}
			offset+=bytesPerFrame;
		}
	}

	public int getByteArrayBufferSize(AudioFormat format) {
		// returns the required size of the buffer
		// when convertToByteArray(..) is called
		if (!format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) &&
		        !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
			throw new IllegalArgumentException
			("FloatSampleBuffer: only PCM samples are possible.");
		}
		int bytesPerSample=format.getSampleSizeInBits()/8;
		int bytesPerFrame=bytesPerSample*format.getChannels();
		return bytesPerFrame*getSampleCount();
	}

	/**
	 * throws exception when buffer is too small or <code>format</code> doesn't match
	 */
	public void convertToByteArray(byte[] buffer, int offset, AudioFormat format) {
		int byteCount=getByteArrayBufferSize(format);
		if (offset+byteCount>buffer.length) {
			throw new IllegalArgumentException
			("FloatSampleBuffer.convertToByteArray: buffer too small.");
		}
		boolean signed=format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
		if (!signed &&
		        !format.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
			throw new IllegalArgumentException
			("FloatSampleBuffer.convertToByteArray: only PCM samples are allowed.");
		}
		if (format.getSampleRate()!=getSampleRate()) {
			throw new IllegalArgumentException
			("FloatSampleBuffer.convertToByteArray: different samplerates.");
		}
		if (format.getChannels()!=getChannelCount()) {
			throw new IllegalArgumentException
			("FloatSampleBuffer.convertToByteArray: different channel count.");
		}
		int bytesPerSample=format.getSampleSizeInBits()/8;
		int bytesPerFrame=bytesPerSample*format.getChannels();
		int formatType=getFormatType(format.getSampleSizeInBits(),
		                             signed, format.isBigEndian());
		for (int ch=0; ch<format.getChannels(); ch++) {
			convertFloatToByte(getChannel(ch), sampleCount,
			                   buffer, offset,
			                   bytesPerFrame, formatType);
			offset+=bytesPerSample; // next channel
		}

	}

	public byte[] convertToByteArray(AudioFormat format) {
		// throws exception when sampleRate doesn't match
		// creates a new byte[] buffer and returns it
		byte[] res=new byte[getByteArrayBufferSize(format)];
		convertToByteArray(res, 0, format);
		return res;
	}

	protected byte quantize8(float sample) {
		if (doDither) {
			sample+=random.nextFloat()*ditherBits;
		}
		if (sample>=127.0f) {
			return (byte) 127;
		} else if (sample<=-128) {
			return (byte) -128;
		} else {
			return (byte) (sample<0?(sample-0.5f):(sample+0.5f));
		}
	}

	protected int quantize16(float sample) {
		if (doDither) {
			sample+=random.nextFloat()*ditherBits;
		}
		if (sample>=32767.0f) {
			return 32767;
		} else if (sample<=-32768.0f) {
			return -32768;
		} else {
			return (int) (sample<0?(sample-0.5f):(sample+0.5f));
		}
	}

	protected int quantize24(float sample) {
		if (doDither) {
			sample+=random.nextFloat()*ditherBits;
		}
		if (sample>=8388607.0f) {
			return 8388607;
		} else if (sample<=-8388608.0f) {
			return -8388608;
		} else {
			return (int) (sample<0?(sample-0.5f):(sample+0.5f));
		}
	}

	protected int quantize32(float sample) {
		if (doDither) {
			sample+=random.nextFloat()*ditherBits;
		}
		if (sample>=2147483647.0f) {
			return 2147483647;
		} else if (sample<=-2147483648.0f) {
			return -2147483648;
		} else {
			return (int) (sample<0?(sample-0.5f):(sample+0.5f));
		}
	}

	// should be static and public, but dithering needs class members
	private void convertFloatToByte(float[] input, int sampleCount,
	                                byte[] output, int offset,
	                                int bytesPerFrame, int formatType) {
		//if (TDebug.TraceAudioConverter) {
		//    TDebug.out("FloatSampleBuffer.convertFloatToByte, formatType="
		//               +"formatType2Str(formatType));
		//}

		// let's see whether dithering is necessary
		switch (ditherMode) {
		case DITHER_MODE_AUTOMATIC:
			doDither=(originalFormatType & F_SAMPLE_WIDTH_MASK)>
			         (formatType & F_SAMPLE_WIDTH_MASK);
			break;
		case DITHER_MODE_ON:
			doDither=true;
			break;
		case DITHER_MODE_OFF:
			doDither=false;
			break;
		}
		if (doDither && random==null) {
			// create the random number generator for dithering
			random=new Random();
		}
		int inIndex;
		int iSample;
		for (inIndex=0; inIndex<sampleCount; inIndex++) {
			// do conversion
			switch (formatType) {
			case CT_8S:
				output[offset]=quantize8(input[inIndex]*twoPower7);
				break;
			case CT_8U:
				output[offset]=(byte) (quantize8(input[inIndex]*twoPower7)+128);
				break;
			case CT_16SB:
				iSample=quantize16(input[inIndex]*twoPower15);
				output[offset]=(byte) (iSample >> 8);
				output[offset+1]=(byte) (iSample & 0xFF);
				break;
			case CT_16SL:
				iSample=quantize16(input[inIndex]*twoPower15);
				output[offset+1]=(byte) (iSample >> 8);
				output[offset]=(byte) (iSample & 0xFF);
				break;
			case CT_24SB:
				iSample=quantize24(input[inIndex]*twoPower23);
				output[offset]=(byte) (iSample >> 16);
				output[offset+1]=(byte) ((iSample >>> 8) & 0xFF);
				output[offset+2]=(byte) (iSample & 0xFF);
				break;
			case CT_24SL:
				iSample=quantize24(input[inIndex]*twoPower23);
				output[offset+2]=(byte) (iSample >> 16);
				output[offset+1]=(byte) ((iSample >>> 8) & 0xFF);
				output[offset]=(byte) (iSample & 0xFF);
				break;
			case CT_32SB:
				iSample=quantize32(input[inIndex]*twoPower31);
				output[offset]=(byte) (iSample >> 24);
				output[offset+1]=(byte) ((iSample >>> 16) & 0xFF);
				output[offset+2]=(byte) ((iSample >>> 8) & 0xFF);
				output[offset+3]=(byte) (iSample & 0xFF);
				break;
			case CT_32SL:
				iSample=quantize32(input[inIndex]*twoPower31);
				output[offset+3]=(byte) (iSample >> 24);
				output[offset+2]=(byte) ((iSample >>> 16) & 0xFF);
				output[offset+1]=(byte) ((iSample >>> 8) & 0xFF);
				output[offset]=(byte) (iSample & 0xFF);
				break;
			default:
				throw new IllegalArgumentException
				("Unsupported formatType="+formatType);
			}
			offset+=bytesPerFrame;
		}
	}

	public void initFromFloatSampleBuffer(FloatSampleBuffer source) {
		init(source.getChannelCount(), source.getSampleCount(), source.getSampleRate());
		for (int ch=0; ch<getChannelCount(); ch++) {
			System.arraycopy(source.getChannel(ch), 0, getChannel(ch), 0, sampleCount);
		}
	}

	public int getChannelCount() {
		return channels!=null?channels.size():0;
	}

	public int getSampleCount() {
		return sampleCount;
	}

	public float getSampleRate() {
		return sampleRate;
	}

	/**
	 * Sets the sample rate of this buffer.
	 * NOTE: no conversion is done. The samples are only re-interpreted.
	 */
	public void setSampleRate(float sampleRate) {
		if (sampleRate<=0) {
			throw new IllegalArgumentException
			("Invalid samplerate for FloatSampleBuffer.");
		}
		this.sampleRate=sampleRate;
	}

	/**
	 * NOTE: the returned array may be larger than sampleCount. In this case, 
	 * sampleCount is to be respected.
	 */
	public float[] getChannel(int channel) {
		if (channel<0 || channel>=getChannelCount()) {
			throw new IllegalArgumentException
			("FloatSampleBuffer: invalid channel number.");
		}
		return (float[]) channels.get(channel);
	}

	/**
	 * deletes all channels, frees memory...
	 */
	public void reset() {
		init(0,0,1);
	}

	/**
	 * destroys any existing data and creates new channels
	 */
	public void reset(int channels, int sampleCount, float sampleRate) {
		init(channels, sampleCount, sampleRate);
	}

	/**
	 * Resizes this buffer.
	 * <p>If <code>keepOldSamples</code> is true, as much as possible samples are
	 * retained. If the buffer is enlarged, silence is added at the end.
	 * If <code>keepOldSamples</code> is false, existing samples are discarded
	 * and the buffer contains random samples.
	 */
	public void changeSampleCount(int newSampleCount, boolean keepOldSamples) {
		int oldSampleCount=getSampleCount();
		if (getChannelCount()==0 || newSampleCount<=getChannel(0).length) {
			// nothing do do
			sampleCount=newSampleCount;
		} else {
			Iterator oldChannels=null;
			if (keepOldSamples) {
				oldChannels=channels.iterator();
			}
			init(getChannelCount(), newSampleCount, getSampleRate());
			if (keepOldSamples) {
				// copy old channels
				int mini=newSampleCount<oldSampleCount?
				         newSampleCount:oldSampleCount;
				for (int ch=0; ch<getChannelCount(); ch++) {
					float[] oldSamples=(float[]) oldChannels.next();
					System.arraycopy(oldSamples, 0, getChannel(ch), 0, mini);
				}
			}
		}
		if (keepOldSamples && oldSampleCount<getSampleCount()) {
			// silence out new samples
			for (int ch=0; ch<getChannelCount(); ch++) {
				float[] samples=getChannel(ch);
				for (int i=oldSampleCount; i<getSampleCount(); i++) {
					samples[i]=0.0f;
				}
			}
		}
	}

	public void makeSilence() {
		// silence all channels
		if (getChannelCount()>0) {
			makeSilence(0);
			for (int ch=1; ch<getChannelCount(); ch++) {
				duplicateChannel(0, ch);
			}
		}
	}

	public void makeSilence(int channel) {
		float[] samples=getChannel(0);
		for (int i=0; i<getSampleCount(); i++) {
			samples[i]=0.0f;
		}
	}

	public void addChannel(boolean silent) {
		// creates new, silent channel
		insertChannel(getChannelCount(), silent);
	}

	public void insertChannel(int index, boolean silent) {
		// creates new (silent) channel before channel <code>index</code>.
		float[] samples=new float[sampleCount];
		channels.add(index, samples);
		if (silent) {
			makeSilence(index);
		}
	}

	public void removeChannel(int channel) {
		channels.remove(channel);
	}

	public void duplicateChannel(int sourceChannel, int targetChannel) {
		// both source and target channel have to exist. targetChannel
		// will be overwritten
		float[] source=getChannel(sourceChannel);
		float[] target=getChannel(targetChannel);
		System.arraycopy(source, 0, target, 0, getSampleCount());
	}

	/**
	 * Set the number of bits for dithering.
	 * Typically, a value between 0.2 and 0.9 gives best results.
	 * <p>Note: this value is only used, when dithering is actually performed.
	 */
	public void setDitherBits(float ditherBits) {
		if (ditherBits<=0) {
			throw new IllegalArgumentException("DitherBits must be greater than 0");
		}
		this.ditherBits=ditherBits;
	}

	public float getDitherBits() {
		return ditherBits;
	}

	/**
	 * Sets the mode for dithering.
	 * This can be one of:
	 * <ul><li>DITHER_MODE_AUTOMATIC: it is decided automatically,
	 * whether dithering is necessary - in general when sample size is
	 * decreased.
	 * <li>DITHER_MODE_ON: dithering will be forced
	 * <li>DITHER_MODE_OFF: dithering will not be done.
	 * </ul>
	 */
	public void setDitherMode(int mode) {
		if (mode!=DITHER_MODE_AUTOMATIC
		        && mode!=DITHER_MODE_ON
		        && mode!=DITHER_MODE_OFF) {
			throw new IllegalArgumentException("Illegal DitherMode");
		}
		this.ditherMode=mode;
	}

	public int getDitherMode() {
		return ditherMode;
	}


	/**
	 * Debugging function
	 */
	private static String formatType2Str(int formatType) {
		String res=""+formatType+": ";
		switch (formatType &  F_SAMPLE_WIDTH_MASK) {
		case F_8:
			res+="8bit";
			break;
		case F_16:
			res+="16bit";
			break;
		case F_24:
			res+="24bit";
			break;
		case F_32:
			res+="32bit";
			break;
		}
		res+=((formatType & F_SIGNED)==F_SIGNED)?" signed":" unsigned";
		if ((formatType &  F_SAMPLE_WIDTH_MASK)!=F_8) {
			res+=((formatType & F_BIGENDIAN)==F_BIGENDIAN)?
			     " big endian":" little endian";
		}
		return res;
	}

}

/*** FloatSampleBuffer.java ***/

/*
 *	Lame.java
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


package	org.tritonus.lowlevel.lame;

import	java.io.IOException;
import javax.sound.sampled.AudioFormat;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.Encodings;

public class Lame
{

	// frame size=576 for MPEG2 and MPEG2.5
	//           =576*2 for MPEG1
	private static boolean libAvailable=false;
	private static String linkError="";

	public static final int QUALITY_LOWEST = 9; // low mean bitrate in VBR mode
	public static final int QUALITY_LOW = 7;    
	public static final int QUALITY_MIDDLE = 5;
	public static final int QUALITY_HIGH = 2;
	// quality==0 not yet coded in LAME (3.83alpha)
	public static final int QUALITY_HIGHEST = 1; // high mean bitrate in VBR mode

	// channel mode has no influence on mono files.
	public static final int CHANNEL_MODE_STEREO = 0;
	public static final int CHANNEL_MODE_JOINT_STEREO = 1;
	public static final int CHANNEL_MODE_DUAL_CHANNEL = 2;
	public static final int CHANNEL_MODE_MONO = 3;
	public static final int CHANNEL_MODE_AUTO = -1;

	private static int DEFAULT_QUALITY = QUALITY_MIDDLE;
	private static int DEFAULT_BIT_RATE = 128;
	private static int DEFAULT_CHANNEL_MODE = CHANNEL_MODE_AUTO;
	// in VBR mode, bitrate is ignored.
	private static boolean DEFAULT_VBR = false;

	private static final int OUT_OF_MEMORY = -300;
	private static final int NOT_INITIALIZED = -301;

	private static final String PROPERTY_PREFIX = "tritonus.lame.";

	static
	{
		try {
			System.loadLibrary("lametritonus");
			libAvailable=true;
		} catch (UnsatisfiedLinkError e) {
			linkError=e.getMessage();
		}
	}


	/**
	 * Holds lame_global_flags.
	 * This field is long because on 64 bit architectures, the native
	 * size of ints may be 64 bit.
	 */
	private long m_lNativeGlobalFlags;

	// these fields are set upon successful initialization to show effective values.
	private int effQuality;
	private int effBitRate;
	private int effVbr;
	private int effChMode;
	private int effSampleRate;
	private int effEncoding;

	/**
	 * Initializes the decoder with 
	 * DEFAULT_BIT_RATE, DEFAULT_CHANNEL_MODE, DEFAULT_QUALITY, and DEFAULT_VBR
	 * Throws IllegalArgumentException when parameters are not supported
	 * by LAME.
	 */
	public Lame(AudioFormat sourceFormat) {
		initParams(sourceFormat);
	}

	/**
	 * Initializes the decoder.
	 * Throws IllegalArgumentException when parameters are not supported
	 * by LAME.
	 */
	public Lame(AudioFormat sourceFormat, int bitRate, int channelMode, int quality, boolean VBR) {
		initParams(sourceFormat, bitRate, channelMode, quality, VBR);
	}

	private void initParams(AudioFormat sourceFormat) {
		readParameters();
		initParams(sourceFormat, DEFAULT_BIT_RATE, DEFAULT_CHANNEL_MODE, DEFAULT_QUALITY, DEFAULT_VBR);
	}
		
	private void initParams(AudioFormat sourceFormat, int bitRate, int channelMode, int quality, boolean VBR) {
		// simple check that bitrate is not too high for MPEG2 and MPEG2.5
		// todo: exception ?
		if (sourceFormat.getSampleRate()<32000 && bitRate>160) {
			bitRate=160;
		}
		if (TDebug.TraceAudioConverter) {
			TDebug.out("LAME parameters: channels="+sourceFormat.getChannels()
				   +"  sample rate="+((int) Math.round(sourceFormat.getSampleRate())+"Hz")
				   +"  bitrate="+bitRate+"KBit/s");
			TDebug.out("                 channelMode="+chmode2string(channelMode)
				   +"   quality="+quality2string(quality)
				   +"   VBR="+VBR+"  bigEndian="+sourceFormat.isBigEndian());
		}
		int result=nInitParams(sourceFormat.getChannels(), (int) Math.round(sourceFormat.getSampleRate()), 
				       bitRate, channelMode, quality,
				       VBR, sourceFormat.isBigEndian());
		if (result==OUT_OF_MEMORY) {
			close();
			throw new OutOfMemoryError("out of memory");
		} else
		if (result==NOT_INITIALIZED) {
			close();
			throw new RuntimeException("not initialized");
		} else
		if (result<0) {
			close();
			throw new IllegalArgumentException("parameters not supported by LAME (returned "+result+")");
		}
		// provide effective parameters to user-space
		try {
			System.setProperty(PROPERTY_PREFIX + "effective.quality", 
					   quality2string(getEffectiveQuality()));
			System.setProperty(PROPERTY_PREFIX + "effective.bitrate", 
					   String.valueOf(getEffectiveBitRate()));
			System.setProperty(PROPERTY_PREFIX + "effective.chmode", 
					   chmode2string(getEffectiveChannelMode()));
			System.setProperty(PROPERTY_PREFIX + "effective.vbr", 
					   String.valueOf(getEffectiveVBR()));
			System.setProperty(PROPERTY_PREFIX + "effective.samplerate", 
					   String.valueOf(getEffectiveSampleRate()));
			System.setProperty(PROPERTY_PREFIX + "effective.encoding", 
					   getEffectiveEncoding().toString());
		} catch (Throwable t) {
		}
	}

	/**
	 * Initializes the lame encoder.
	 * Throws IllegalArgumentException when parameters are not supported
	 * by LAME.
	 */	
	private native int nInitParams(int channels, int sampleRate, 
				       int bitrate, int mode, int quality,
				       boolean VBR, boolean bigEndian);

	public native int nEncodeBuffer(byte[] pcm, int offset, 
					int length, byte[] encoded);

	/**
	 * Encode a block of data. Throws IllegalArgumentException when parameters 
	 * are wrong.
	 * When the <code>encoded</code> array is too small, 
	 * an ArrayIndexOutOfBoundsException is thrown.
	 * @return the number of bytes written to <code>encoded</code>. May be 0.
	 */
	public int encodeBuffer(byte[] pcm, int offset, int length, byte[] encoded)
		throws ArrayIndexOutOfBoundsException {
		if (length<0 || (offset+length)>pcm.length) {
			throw new IllegalArgumentException("inconsistent parameters");
		}
		int result=nEncodeBuffer(pcm, offset, length, encoded);
		if (result<0) {
			if (result==OUT_OF_MEMORY) {
				throw new OutOfMemoryError("out of memory");
			} 
			else if (result==NOT_INITIALIZED) {
				throw new RuntimeException("not initialized");
			}
			if (result==-1) {
				throw new ArrayIndexOutOfBoundsException("Encode buffer too small");
			}
			throw new RuntimeException("crucial error in encodeBuffer.");
		}
		return result;
	}

	/**
	 * Has to be called to finish encoding. <code>encoded</code> may be null.
	 *
	 * @return the number of bytes written to <code>encoded</code>
	 */
	public native int encodeFinish(byte[] encoded);

	/*
	 * Deallocates resources used by the native library.
	 * *MUST* be called !
	 */
	public native void close();

	/*
	 * Returns whether the libraries are installed correctly.
	 */
	public static boolean isLibAvailable() {
		return libAvailable;
	}

	public static String getLinkError() {
		return linkError;
	}

	public int getEffectiveQuality() {
		if (effQuality>=QUALITY_LOWEST) {
			return QUALITY_LOWEST;
		}
		else if (effQuality>=QUALITY_LOW) {
			return QUALITY_LOW;
		}
		else if (effQuality>=QUALITY_MIDDLE) {
			return QUALITY_MIDDLE;
		}
		else if (effQuality>=QUALITY_HIGH) {
			return QUALITY_HIGH;
		}
		return QUALITY_HIGHEST;
	}
		
	public int getEffectiveBitRate() {
		return effBitRate;
	}

	public int getEffectiveChannelMode() {
		return effChMode;
	}
	
	public boolean getEffectiveVBR() {
		return effVbr!=0;
	}
	
	public int getEffectiveSampleRate() {
		return effSampleRate;
	}
	
	public AudioFormat.Encoding getEffectiveEncoding() {
		if (effEncoding==0) {
			if (getEffectiveSampleRate()<16000) {
				return Encodings.getEncoding("MPEG2DOT5L3");
			}
			return Encodings.getEncoding("MPEG2L3");
		}
		return Encodings.getEncoding("MPEG1L3");
	}

	/**
	 * workaround for missing paramtrization possibilities 
	 * for FormatConversionProviders
	 */
	private void readParameters() {
		String v=getStringProperty("quality", quality2string(DEFAULT_QUALITY));
		DEFAULT_QUALITY=string2quality(v.toLowerCase(), DEFAULT_QUALITY);
		DEFAULT_BIT_RATE=getIntProperty("bitrate", DEFAULT_BIT_RATE);
		v=getStringProperty("chmode", chmode2string(DEFAULT_CHANNEL_MODE));
		DEFAULT_CHANNEL_MODE=string2chmode(v.toLowerCase(), DEFAULT_CHANNEL_MODE);
		DEFAULT_VBR = getBooleanProperty("vbr", DEFAULT_VBR);
		// set the parameters back so that user program can verify them
		try {
			System.setProperty(PROPERTY_PREFIX + "quality", quality2string(DEFAULT_QUALITY));
			System.setProperty(PROPERTY_PREFIX + "bitrate", String.valueOf(DEFAULT_BIT_RATE));
			System.setProperty(PROPERTY_PREFIX + "chmode", chmode2string(DEFAULT_CHANNEL_MODE));
			System.setProperty(PROPERTY_PREFIX + "vbr", String.valueOf(DEFAULT_VBR));
		} catch (Throwable t) {
		}
	}

	private String quality2string(int quality) {
		if (quality>=QUALITY_LOWEST) {
			return "lowest";
		}
		else if (quality>=QUALITY_LOW) {
			return "low";
		}
		else if (quality>=QUALITY_MIDDLE) {
			return "middle";
		}
		else if (quality>=QUALITY_HIGH) {
			return "high";
		}
		return "highest";
	}

	private int string2quality(String quality, int def) {
		if (quality.equals("lowest")) {
			return QUALITY_LOWEST;
		}
		else if (quality.equals("low")) {
			return QUALITY_LOW;
		}
		else if (quality.equals("middle")) {
			return QUALITY_MIDDLE;
		}
		else if (quality.equals("high")) {
			return QUALITY_HIGH;
		}
		else if (quality.equals("highest")) {
			return QUALITY_HIGHEST;
		}
		return def;
	}

	private String chmode2string(int chmode) {
		if (chmode==CHANNEL_MODE_STEREO) {
			return "stereo";
		}
		else if (chmode==CHANNEL_MODE_JOINT_STEREO) {
			return "jointstereo";
		}
		else if (chmode==CHANNEL_MODE_DUAL_CHANNEL) {
			return "dual";
		}
		else if (chmode==CHANNEL_MODE_MONO) {
			return "mono";
		}
		else if (chmode==CHANNEL_MODE_AUTO) {
			return "auto";
		}
		return "auto";
	}

	private int string2chmode(String chmode, int def) {
		if (chmode.equals("stereo")) {
			return CHANNEL_MODE_STEREO;
		}
		else if (chmode.equals("jointstereo")) {
			return CHANNEL_MODE_JOINT_STEREO;
		}
		else if (chmode.equals("dual")) {
			return CHANNEL_MODE_DUAL_CHANNEL;
		}
		else if (chmode.equals("mono")) {
			return CHANNEL_MODE_MONO;
		}
		else if (chmode.equals("auto")) {
			return CHANNEL_MODE_AUTO;
		}
		return def;
	}

	private static boolean getBooleanProperty(String strName, boolean def) {
		String	strPropertyName = PROPERTY_PREFIX + strName;
		String	strValue = def ? "true":"false";
		try {
			strValue = System.getProperty(strPropertyName, strValue);
		}
		catch (Exception e) {
		}
		strValue=strValue.toLowerCase();
		boolean	bValue=false;
		if (strValue.length()>0) {
			if (def) {
				bValue=(strValue.charAt(0)!='f') // false
					&& (strValue.charAt(0)!='n') // no
					&& (!strValue.equals("off"));
			} else {
				bValue=(strValue.charAt(0)=='t') // true
					|| (strValue.charAt(0)=='y') // yes
					|| (strValue.equals("on"));
			}
		}
		return bValue;
	}

	private static String getStringProperty(String strName, String def)	{
		String	strPropertyName = PROPERTY_PREFIX + strName;
		String	strValue = def;
		try {
			strValue = System.getProperty(strPropertyName, def);
		}
		catch (Exception e) {
		}
		return strValue;
	}

	private static int getIntProperty(String strName, int def)	{
		String	strPropertyName = PROPERTY_PREFIX + strName;
		int	value = def;
		try {
			String strValue = System.getProperty(strPropertyName, String.valueOf(def));
			value=new Integer(strValue).intValue();
		}
		catch (Throwable e) {
		}
		return value;
	}

}


/*** Lame.java ***/
/*
 *	TEncodingFormatConversionProvider.java
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


import	java.util.Collection;
import	java.util.Iterator;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	org.tritonus.util.ArraySet;

// this class depends on handling of AudioSystem.NOT_SPECIFIED in AudioFormat.matches()

/**
 * This is a base class for FormatConversionProviders that only
 * change the encoding, i.e. they never 
 * <ul>
 * <li> change the sample size in bits without changing the encoding
 * <li> change the sample rate
 * <li> change the number of channels
 * </ul>
 * <p>It is assumed that each source format can be encoded to all
 * target formats.
 * <p>In the sourceFormats and targetFormats collections that are passed to
 * the constructor of this class, fields may be set to AudioSystem.NOT_SPECIFIED.
 * This means that it handles all values of that field, but cannot change it.
 * <p>This class prevents that a conversion is done (e.g. for sample rates),
 * because the overriding class specified AudioSystem.NOT_SPECIFIED as sample rate,
 * meaning it handles all sample rates.
 * <p>Btw, JDK1.3RC1 doesn't handle this case properly, sample rate conversions
 * are done without error, but the converted stream has only the AudioFormat field
 * changed, the sample rate is not actually converted.
 * <p>Overriding classes must implement at least
 * <code>AudioInputStream getAudioInputStream(AudioFormat targetFormat, AudioInputStream sourceStream)</code>
 * and provide a constructor that calls the protected constructor of this class.
 *
 * @author Florian Bomers
 */
public abstract class TEncodingFormatConversionProvider
	extends		TSimpleFormatConversionProvider
{

	protected TEncodingFormatConversionProvider(
		Collection sourceFormats,
		Collection targetFormats)
	{
		super(sourceFormats, targetFormats);
	}



	/**
	 * This implementation assumes that the converter can convert
	 * from each of its source formats to each of its target
	 * formats. If this is not the case, the converter has to
	 * override this method.
	 * <p>When conversion is supported, for every target encoding, 
	 * the fields sample size in bits, channels and sample rate are checked:
	 * <ul>
	 * <li>When a field in both the source and target format is AudioSystem.NOT_SPECIFIED,
	 *     one instance of that targetFormat is returned with this field set to AudioSystem.NOT_SPECIFIED.
	 * <li>When a field in sourceFormat is set and it is AudioSystem.NOT_SPECIFIED in the target format,
	 *     the value of the field of source format is set in the returned format.
	 * <li>The same applies for the other way round.
	 * </ul>
	 * <p>Attention: when a target field is AudioSystem.NOT_SPECIFIED and is replaced by
	 * the value in sourceFormat, the FrameSize is newly calculated !
	 */
	public AudioFormat[] getTargetFormats(AudioFormat.Encoding targetEncoding, AudioFormat sourceFormat)
	{
		if (isConversionSupported(targetEncoding, sourceFormat))
		{
			// TODO: check that no duplicates may occur...
			ArraySet	result=new ArraySet();
			Iterator	iterator = getCollectionTargetFormats().iterator();
			while (iterator.hasNext())
			{
				AudioFormat	format = (AudioFormat) iterator.next();
				boolean bSetSampleSize=false;
				boolean bSetChannels=false;
				boolean bSetSampleRate=false;
				boolean bSetFrameRate=false;
				if (format.getSampleSizeInBits()==AudioSystem.NOT_SPECIFIED 
						&& sourceFormat.getSampleSizeInBits()!=AudioSystem.NOT_SPECIFIED) {
					bSetSampleSize=true;
				}
				if (format.getChannels()==AudioSystem.NOT_SPECIFIED 
						&& sourceFormat.getChannels()!=AudioSystem.NOT_SPECIFIED) {
					bSetChannels=true;
				}
				if (format.getSampleRate()==AudioSystem.NOT_SPECIFIED 
						&& sourceFormat.getSampleRate()!=AudioSystem.NOT_SPECIFIED) {
					bSetSampleRate=true;
				}
				if (format.getFrameRate()==AudioSystem.NOT_SPECIFIED 
						&& sourceFormat.getFrameRate()!=AudioSystem.NOT_SPECIFIED) {
					bSetFrameRate=true;
				}
				if (bSetSampleSize || bSetChannels || bSetSampleRate || bSetFrameRate) {
					// create new format in place of the original target format
					float sampleRate=bSetSampleRate?sourceFormat.getSampleRate():format.getSampleRate();
					float frameRate=bSetFrameRate?sourceFormat.getFrameRate():format.getFrameRate();
					int sampleSize=bSetSampleSize?sourceFormat.getSampleSizeInBits():format.getSampleSizeInBits();
					int channels=bSetChannels?sourceFormat.getChannels():format.getChannels();
					
					format = new AudioFormat(
						format.getEncoding(),
						sampleRate,
						sampleSize,
						channels,
						(sampleSize==AudioSystem.NOT_SPECIFIED || channels==AudioSystem.NOT_SPECIFIED)?AudioSystem.NOT_SPECIFIED:(sampleSize*channels/8),
						frameRate,
						format.isBigEndian());
				}
				result.add(format);
			}
			return (AudioFormat[]) result.toArray(EMPTY_FORMAT_ARRAY);
		}
		else
		{
			return EMPTY_FORMAT_ARRAY;
		}
	}

	/**
	 * Utility method to check whether these to values match, taking into account AudioSystem.NOT_SPECIFIED.
	 * @return true if any of the values is AudioSystem.NOT_SPECIFIED or both values have the same value.
	 */
	protected boolean doMatch(int i1, int i2) {
		return i1==AudioSystem.NOT_SPECIFIED
			|| i2==AudioSystem.NOT_SPECIFIED
			|| i1==i2;
	}
    
	/**
	 * @see #doMatch(int,int)
	 */
	protected boolean doMatch(float f1, float f2) {
		return f1==AudioSystem.NOT_SPECIFIED
			|| f2==AudioSystem.NOT_SPECIFIED
			|| Math.abs(f1 - f2) < 1.0e-9;
	}

}

/*** TEncodingFormatConversionProvider.java ***/

/*
 *	TFormatConversionProvider.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.spi.FormatConversionProvider;


/**
 * Base class for all conversion providers.
 *
 * @author Matthias Pfisterer
 */

public abstract class TFormatConversionProvider
	extends		FormatConversionProvider
{
	protected static final AudioFormat.Encoding[]	EMPTY_ENCODING_ARRAY = new AudioFormat.Encoding[0];
	protected static final AudioFormat[]		EMPTY_FORMAT_ARRAY = new AudioFormat[0];



	// TODO: find a better solution; move out of TFormatConversionProvider
	// very primitive, not too useful
	// perhaps use some overwritable method getDefaultAudioFormat(Encoding)
	public AudioInputStream getAudioInputStream(AudioFormat.Encoding targetEncoding, AudioInputStream audioInputStream)
	{
		AudioFormat	sourceFormat = audioInputStream.getFormat();
		AudioFormat	targetFormat = new AudioFormat(
			targetEncoding,
			sourceFormat.getSampleRate(),
			sourceFormat.getSampleSizeInBits(),
			sourceFormat.getChannels(),
			sourceFormat.getFrameSize(),
			sourceFormat.getFrameRate(),
			sourceFormat.isBigEndian());
		return getAudioInputStream(targetFormat, audioInputStream);
	}
}



/*** TFormatConversionProvider.java ***/

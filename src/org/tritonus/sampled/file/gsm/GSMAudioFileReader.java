/*
 *	GSMAudioFileReader.java
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


package	org.tritonus.sampled.file.gsm;


import	java.io.DataInputStream;
import	java.io.File;
import	java.io.InputStream;
import	java.io.BufferedInputStream;
import	java.io.IOException;
import	java.io.EOFException;
import	java.io.SequenceInputStream;
import	java.io.ByteArrayInputStream;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.spi.AudioFileReader;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.file.TAudioFileFormat;
import	org.tritonus.sampled.file.TAudioFileReader;
import	org.tritonus.sampled.Encodings;
import	org.tritonus.sampled.AudioFileTypes;



/*
 * @author Matthias Pfisterer
 */
public class GSMAudioFileReader
	extends	TAudioFileReader
{
	private static final byte		GSM_MAGIC = 0x0D;
	private static final byte		GSM_MAGIC_MASK = 0x0F;
	private static final int		GSM_MAGIC_SHIFT = 4;



	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		return getAudioFileFormat(inputStream, null);
	}



	private AudioFileFormat getAudioFileFormat(InputStream inputStream, byte[] abHeader)
		throws	UnsupportedAudioFileException, IOException
	{
/*
		int	b0 = inputStream.read();
		if (b0 < 0)
		{
			throw new EOFException();
		}
		abHeader[0] = (byte) b0;
*/
		/*
		 *	Check for magic number.
		 */
/*
		if (((abHeader[0] >> GSM_MAGIC_SHIFT) & GSM_MAGIC_MASK) != GSM_MAGIC)
		{
			throw new UnsupportedAudioFileException("not a GSM stream: wrong magic number");
		}
*/

		AudioFormat	format = new AudioFormat(
			Encodings.getEncoding("GSM0610"),
			8000.0F,
			AudioSystem.NOT_SPECIFIED /*???*/,
			1,
			33,
			50.0F,
			true);
		return new TAudioFileFormat(AudioFileTypes.getType("GSM","gsm"), 
					    format, 
					    AudioSystem.NOT_SPECIFIED, 
					    AudioSystem.NOT_SPECIFIED);
	}



	public AudioInputStream getAudioInputStream(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		// TDebug.out("GSMAudioFileReader.getAudioInputStream()");
		byte[]	abHeader = new byte[1];
		AudioFileFormat	audioFileFormat = getAudioFileFormat(inputStream, abHeader);
		SequenceInputStream	sequenceInputStream = new SequenceInputStream(new ByteArrayInputStream(abHeader), inputStream);
		// return new AudioInputStream(sequenceInputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
		return new AudioInputStream(inputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
	}



}



/*** GSMAudioFileReader.java ***/


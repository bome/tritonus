/*
 *	GSMAudioFileReader.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *  Copyright (c) 2001 by Florian Bomers <florian@bome.com>
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


// import	java.io.DataInputStream;
// import	java.io.File;
import	java.io.InputStream;
// import	java.io.BufferedInputStream;
import	java.io.IOException;
import	java.io.EOFException;
// import	java.io.SequenceInputStream;
// import	java.io.ByteArrayInputStream;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.spi.AudioFileReader;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.Encodings;
import	org.tritonus.share.sampled.AudioFileTypes;
import	org.tritonus.share.sampled.file.TAudioFileFormat;
import	org.tritonus.share.sampled.file.TRereadingAudioFileReader;



/*
 * @author Matthias Pfisterer
 */
public class GSMAudioFileReader
	extends	TRereadingAudioFileReader
{
	private static final int	GSM_MAGIC = 0xD0;
	private static final int	GSM_MAGIC_MASK = 0xF0;

	private static final int	BUFFERING_AMOUNT = 1;



	public GSMAudioFileReader()
	{
		super(BUFFERING_AMOUNT);
	}



	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader) { TDebug.out("GSMAudioFileReader.getAudioFileFormat(): begin"); }
		int	b0 = inputStream.read();
		if (b0 < 0)
		{
			throw new EOFException();
		}

		/*
		 *	Check for magic number.
		 */
		if ((b0 & GSM_MAGIC_MASK) != GSM_MAGIC)
		{
			throw new UnsupportedAudioFileException("not a GSM stream: wrong magic number");
		}

		// calculate frame size
		// not specifying it causes Sun's Wave file writer to write rubbish
		int nByteSize=AudioSystem.NOT_SPECIFIED;
		int nFrameSize=AudioSystem.NOT_SPECIFIED;

		if (getFileLengthInBytes()!=AudioSystem.NOT_SPECIFIED) {
			long lByteSize=getFileLengthInBytes();
			long lFrameSize=lByteSize/33;
			// need to handle overflow
			if (lByteSize>Integer.MAX_VALUE) {
				nByteSize=Integer.MAX_VALUE;
			} else {
				nByteSize=(int) lByteSize;
			}
			if (lFrameSize>Integer.MAX_VALUE) {
				nFrameSize=Integer.MAX_VALUE;
			} else {
				nFrameSize=(int) lFrameSize;
			}
		}

		AudioFormat	format = new AudioFormat(
			Encodings.getEncoding("GSM0610"),
			8000.0F,
			AudioSystem.NOT_SPECIFIED /*???*/,
			1,
			33,
			50.0F,
			true);	// this value is chosen arbitrarily
		AudioFileFormat	audioFileFormat =
			new TAudioFileFormat(
				AudioFileTypes.getType("GSM","gsm"), 
				format, 
				nFrameSize, 
				nByteSize);
		if (TDebug.TraceAudioFileReader) { TDebug.out("GSMAudioFileReader.getAudioFileFormat(): end"); }
		return audioFileFormat;
	}
}



/*** GSMAudioFileReader.java ***/


/*
 *	MpegAudioFileReader.java
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


package	org.tritonus.sampled.file;


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
import	org.tritonus.sampled.Encodings;
import	org.tritonus.sampled.AudioFileTypes;
import	org.tritonus.sampled.file.TAudioFileFormat;


/*
 * @author Matthias Pfisterer
 */

public class MpegAudioFileReader
	extends	TAudioFileReader
{
	private static final int	SYNC = 0xFFE00000;
	private static final AudioFormat.Encoding[][]	sm_aEncodings =
	{ 
		{Encodings.getEncoding("MPEG2DOT5L3"), 
		 Encodings.getEncoding("MPEG2DOT5L2"), 
		 Encodings.getEncoding("MPEG2DOT5L1")},
		{Encodings.getEncoding("MPEG2DOT5L3"), 
		 Encodings.getEncoding("MPEG2DOT5L2"), 
		 Encodings.getEncoding("MPEG2DOT5L1")},	/* reserved */
		{Encodings.getEncoding("MPEG2L3"), 
		 Encodings.getEncoding("MPEG2L2"), 
		 Encodings.getEncoding("MPEG2L1")},
		{Encodings.getEncoding("MPEG1L3"), 
		 Encodings.getEncoding("MPEG1L2"), 
		 Encodings.getEncoding("MPEG1L1")},
	};
	private static final float[][]	sm_afSamplingRates =
	{
		{11025.0F, 12000.0F, 8000.0F},
		{0.0F, 0.0F, 0.0F},	/* reserved */
		{22050.0F, 24000.0F, 16000.0F},
		{44100.0F, 48000.0F, 32000.0F},
	};



	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		return getAudioFileFormat(inputStream, null);
	}



	private AudioFileFormat getAudioFileFormat(InputStream inputStream, byte[] abHeader)
		throws	UnsupportedAudioFileException, IOException
	{
		int	b0 = inputStream.read();
		int	b1 = inputStream.read();
		int	b2 = inputStream.read();
		int	b3 = inputStream.read();
		if ((b0 | b1 | b2 | b3) < 0)
		{
			throw new EOFException();
		}
		int	nHeader = (b0 << 24) + (b1 << 16) + (b2 << 8) + (b3 << 0);
		if (abHeader != null)
		{
			abHeader[0] = (byte) b0;
			abHeader[1] = (byte) b1;
			abHeader[2] = (byte) b2;
			abHeader[3] = (byte) b3;
		}
		/*
		 *	We check for the sync bits. If they are present, we
		 *	assume that we have an MPEG bitstream.
		 */
		if ((nHeader & SYNC) != SYNC)
		{
			throw new UnsupportedAudioFileException("not a MPEG stream: no sync bits");
		}
		int	nVersion = (nHeader >> 19) & 0x3;
		if (nVersion == 1)
		{
			throw new UnsupportedAudioFileException("not a MPEG stream: wrong version");
		}
		int nLayer = (nHeader >> 17) & 0x3;
		if (nLayer == 0)
		{
			throw new UnsupportedAudioFileException("not a MPEG stream: wrong layer");
		}
		AudioFormat.Encoding	encoding = sm_aEncodings[nVersion][nLayer - 1];
		// TODO: bit rate, protection
		int	nSFIndex = (nHeader >> 10) & 0x3;
		if (nSFIndex == 3)
		{
			throw new UnsupportedAudioFileException("not a MPEG stream: wrong sampling rate");
		}
		float	fSamplingRate = sm_afSamplingRates[nVersion][nSFIndex];
		int	nMode = (nHeader >> 6) & 0x3;
		int	nChannels = nMode == 3 ? 1 : 2;

		AudioFormat	format = new AudioFormat(
			encoding,
			fSamplingRate,
			AudioSystem.NOT_SPECIFIED /*???*/,
			nChannels,
			AudioSystem.NOT_SPECIFIED /*????*/,
			AudioSystem.NOT_SPECIFIED /*????*/,
			true);
		//$$fb 2000-08-15: workaround for the fixed extension problem in AudioFileFormat.Type
		// see org.tritonus.sampled.AudioFileTypes.java
		AudioFileFormat.Type type=AudioFileTypes.getType("MPEG", "mpeg");
		if (encoding.equals(Encodings.getEncoding("MPEG1L3"))) {
			type=AudioFileTypes.getType("MP3", "mp3");
		}
		return new TAudioFileFormat(type, 
					    format, 
					    AudioSystem.NOT_SPECIFIED, 
					    AudioSystem.NOT_SPECIFIED);
	}


	public AudioInputStream getAudioInputStream(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		// TDebug.out("MpegAudioFileReader.getAudioInputStream()");
		byte[]	abHeader = new byte[4];
		AudioFileFormat	audioFileFormat = getAudioFileFormat(inputStream, abHeader);
		SequenceInputStream	sequenceInputStream = new SequenceInputStream(new ByteArrayInputStream(abHeader), inputStream);
		return new AudioInputStream(sequenceInputStream, audioFileFormat.getFormat(), audioFileFormat.getFrameLength());
	}



}



/*** MpegAudioFileReader.java ***/


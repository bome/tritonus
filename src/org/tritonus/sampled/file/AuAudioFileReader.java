/*
 *	AuAudioFileReader.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *  Copyright (c) 1999,2000 by Florian Bomers <florian@bome.com>
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
import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.spi.AudioFileReader;

import	org.tritonus.TDebug;

/**
 * Class for reading Sun/Next AU files.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class AuAudioFileReader extends TAudioFileReader {

	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
	throws	UnsupportedAudioFileException, IOException {
		DataInputStream	dataInputStream = new DataInputStream(inputStream);
		int	nMagic = dataInputStream.readInt();
		if (nMagic != AuTool.AU_HEADER_MAGIC) {
			throw new UnsupportedAudioFileException("not an AU file: wrong header magic");
		}
		int nDataOffset = dataInputStream.readInt();
		if (TDebug.TraceAudioFileReader) {
			TDebug.out("AuAudioFileReader.getAudioFileFormat(): data offset: " + nDataOffset);
		}
		if (nDataOffset < AuTool.DATA_OFFSET) {
			throw new UnsupportedAudioFileException("not an AU file: data offset must be 24 or greater");
		}
		int nDataLength = dataInputStream.readInt();
		if (TDebug.TraceAudioFileReader) {
			TDebug.out("AuAudioFileReader.getAudioFileFormat(): data length: " + nDataLength);
		}
		if (nDataLength < -1) {
			throw new UnsupportedAudioFileException("not an AU file: data length must be positive, 0 or -1 for unknown");
		}
		AudioFormat.Encoding encoding = null;
		int nSampleSize = 0;
		int nEncoding = dataInputStream.readInt();
		switch (nEncoding) {
		case AuTool.SND_FORMAT_MULAW_8:		// 8-bit uLaw G.711

			encoding = AuTool.ULAW;
			nSampleSize = 8;
			break;

		case AuTool.SND_FORMAT_LINEAR_8:
			encoding = AuTool.PCM;
			nSampleSize = 8;
			break;

		case AuTool.SND_FORMAT_LINEAR_16:
			encoding = AuTool.PCM;
			nSampleSize = 16;
			break;

		case AuTool.SND_FORMAT_LINEAR_24:
			encoding = AuTool.PCM;
			nSampleSize = 24;
			break;

		case AuTool.SND_FORMAT_LINEAR_32:
			encoding = AuTool.PCM;
			nSampleSize = 32;
			break;

		case AuTool.SND_FORMAT_ALAW_8:	// 8-bit aLaw G.711

			encoding = AuTool.ALAW;
			nSampleSize = 8;
			break;
		}
		if (nSampleSize == 0) {
			throw new UnsupportedAudioFileException("unsupported AU file: unknown encoding " + nEncoding);
		}
		int nSampleRate = dataInputStream.readInt();
		if (nSampleRate <= 0) {
			throw new UnsupportedAudioFileException("corrupt AU file: sample rate must be positive");
		}
		int nNumChannels = dataInputStream.readInt();
		if (nNumChannels <= 0) {
			throw new UnsupportedAudioFileException("corrupt AU file: number of channels must be positive");
		}
		// skip  header information field
		inputStream.skip(nDataOffset - AuTool.DATA_OFFSET);
		AudioFormat format = new AudioFormat(encoding,
		                                     (float) nSampleRate,
		                                     nSampleSize,
		                                     nNumChannels,
		                                     (nSampleSize * nNumChannels) / 8,
		                                     (float) nSampleRate,
		                                     true);
		return new TAudioFileFormat(AuTool.AU,
		                            format,
		                            nDataLength / format.getFrameSize(),
		                            nDataLength + nDataOffset);
	}
}

/*** AuAudioFileReader.java ***/


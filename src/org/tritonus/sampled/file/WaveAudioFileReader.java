/*
 *	WaveAudioFileReader.java
 */

/*
 *  Copyright (c) 1999,2000 by Florian Bomers <florian@bome.com>
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
import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.UnsupportedAudioFileException;
import	javax.sound.sampled.spi.AudioFileReader;

import	org.tritonus.TDebug;


/**
 * Class for reading wave files.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */

public class WaveAudioFileReader extends TAudioFileReader {

	// $$fb 2000-03-30: added this function
	protected void advanceChunk(DataInputStream dis, int prevLength, int prevRead) 
			throws IOException {
		if (prevLength>0) {
			dis.skip(((prevLength+1) & 0xFFFFFFFE)-prevRead);
		}
	}
		

	// $$fb 1999-12-18: added this function
	protected int findChunk(DataInputStream dis, int key) 
			throws UnsupportedAudioFileException, IOException {
		// $$fb 1999-12-18: we should take care that we don't exceed
		// the mark of this stream. When we exceeded the mark and
		// we notice that we don't support this wave file,
		// other potential wave file readers have no chance.
		int thisKey;
		int chunkLength=0;
		do {
			advanceChunk(dis, chunkLength, 0);
			try {
				thisKey = dis.readInt();
			} catch (IOException e) {
				// $$fb: when we come here, we skipped past the end of the wave file
				// without finding the chunk.
				// IMHO, this is not an IOException, as there are incarnations
				// of WAVE files which store data in different chunks.
				// maybe we can find a nice description of the "required chunk" ?
				throw new UnsupportedAudioFileException("unsupported WAVE file: required chunk not found.");
			}
			chunkLength = readLittleEndianInt(dis);
		} while (thisKey != key);
		return chunkLength;
	}

	// $$fb 1999-12-18: added this function
	protected AudioFormat readFormatChunk(DataInputStream dis,
			int chunkLength) throws UnsupportedAudioFileException, IOException {
		
		int read=WaveTool.MIN_FMT_CHUNK_LENGTH;
		
		if (chunkLength<WaveTool.MIN_FMT_CHUNK_LENGTH)
			throw new UnsupportedAudioFileException("corrupt WAVE file: format chunk is too small");

		short sEncoding=readLittleEndianShort(dis);
		short sNumChannels = readLittleEndianShort(dis);
		if (sNumChannels <= 0)
		{
			throw new UnsupportedAudioFileException("corrupt WAVE file: number of channels must be positive");
		}
		
		int	nSampleRate = readLittleEndianInt(dis);
		if (nSampleRate <= 0)
		{
			throw new UnsupportedAudioFileException("corrupt WAVE file: sample rate must be positive");
		}
		
		// avg. bytes per second; ignored
		readLittleEndianInt(dis);
		
		// block align
		// $$fb let's ignore that, too. I prefer that we calculate it.
		readLittleEndianShort(dis);
		
		AudioFormat.Encoding encoding;
		int nSampleSize;
		
		switch (sEncoding) {
		case WaveTool.WAVE_FORMAT_PCM:
			if (chunkLength<WaveTool.MIN_FMT_CHUNK_LENGTH+2)
				throw new UnsupportedAudioFileException("corrupt WAVE file: format chunk is too small");
			read+=2;
			nSampleSize = readLittleEndianShort(dis);
			if (nSampleSize <= 0)
			{
				throw new UnsupportedAudioFileException("corrupt WAVE file: sample size must be positive");
			}
			encoding = (nSampleSize <= 8) ? AudioFormat.Encoding.PCM_UNSIGNED : AudioFormat.Encoding.PCM_SIGNED;
			break;

		case WaveTool.WAVE_FORMAT_ALAW:
			nSampleSize = 8;
			encoding = AudioFormat.Encoding.ALAW;
			break;
		case WaveTool.WAVE_FORMAT_ULAW:
			nSampleSize = 8;
			encoding = AudioFormat.Encoding.ULAW;
			break;
		default:
			throw new UnsupportedAudioFileException("unsupported WAVE file: unknown format code "+sEncoding);
		}
		// go to next chunk
		advanceChunk(dis, chunkLength, read);
		return new AudioFormat(encoding, (float) nSampleRate, 
			nSampleSize, sNumChannels, (nSampleSize * sNumChannels) / 8, 
			(float) nSampleRate, false);
	}

	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		DataInputStream	dataInputStream = new DataInputStream(inputStream);
		int	nMagic = dataInputStream.readInt();
		if (nMagic != WaveTool.WAVE_RIFF_MAGIC)
		{
			throw new UnsupportedAudioFileException("not a WAVE file: wrong header magic");
		}
		int nTotalLength = readLittleEndianInt(dataInputStream);
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("WaveAudioFileReader.getAudioFileFormat(): total length: " + nTotalLength);
		}
		if (nTotalLength < 0)
		{
			//$$fb size is DWORD -> this file, if really WAVE, is > 2GB
			throw new UnsupportedAudioFileException("not a WAVE file: total length must be zero or greater");
		}
		nMagic = dataInputStream.readInt();
		if (nMagic != WaveTool.WAVE_WAVE_MAGIC)
		{
			throw new UnsupportedAudioFileException("not an WAVE file: wrong header magic");
		}
		// search for "fmt " chunk
		int nChunkLength = findChunk(dataInputStream, WaveTool.WAVE_FMT_MAGIC);
		AudioFormat	format = readFormatChunk(dataInputStream, nChunkLength);

		// search for "data" chunk
		int	nDataChunkLength = findChunk(dataInputStream, WaveTool.WAVE_DATA_MAGIC);

		return new TAudioFileFormat(AudioFileFormat.Type.WAVE, format, nDataChunkLength / format.getFrameSize(), nTotalLength + 8);
	}
}

/*** WaveAudioFileReader.java ***/


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

import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.file.TAudioFileFormat;
import	org.tritonus.share.sampled.file.TAudioFileReader;
import	org.tritonus.share.sampled.Encodings;
import	org.tritonus.share.sampled.AudioFileTypes;



/*
 * @author Matthias Pfisterer
 */
public class GSMAudioFileReader
	extends	TAudioFileReader
{
	private static final int		GSM_MAGIC = 0xD0;
	private static final int		GSM_MAGIC_MASK = 0xF0;



	public AudioFileFormat getAudioFileFormat(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioFileFormat(InputStream): begin");
		}
		AudioFileFormat	audioFileFormat = getAudioFileFormatImpl(inputStream /*, null*/);
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioFileFormat(InputStream): end");
		}
		return audioFileFormat;
	}



	private AudioFileFormat getAudioFileFormatImpl(InputStream inputStream /*, byte[] abHeader*/)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioFileFormatImpl(InputStream /*, byte[]*/): begin");
		}
		int	b0 = inputStream.read();
		if (b0 < 0)
		{
			throw new EOFException();
		}
		/*if (abHeader != null)
		{
			abHeader[0] = (byte) b0;
		}*/

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
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioFileFormatImpl(InputStream /*, byte[]*/): end");
		}
		return audioFileFormat;
	}


	public AudioInputStream getAudioInputStream(InputStream inputStream)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioInputStream(): begin");
		}
		//$$fb 2001-04-16: Using this approach with SequenceInputStream may seem
		// nice, but it doesn't work well. In one call to read(byte[]), 
		// SequenceInputStream only reads from the current stream and may
		// return less bytes than are actually available. I consider this as
		// a bug in SequenceInputstream.
		// Now, you say, this doesn't cause a problem, as you use readFully
		// in the implementation of TCircularBuffer. I explain how I had the 
		// problem (and needed some hours to find out where the actual problem was!):
		//
		// I tried to decode a gsm file to wav. The frame size is 33 bytes.
		// The sequence stream has 1 byte in the first stream and the rest
		// in the second stream.
		// AudioSystem.getAudioInputStream returns an AudioInputStream based
		// on the sequence stream.
		// Now, TCircularBuffer creates a DataInputStream on this AudioInputStream
		// and calls readFully(33) [meaning a byte array, requesting 33 bytes].
		// what happens: readFully calls read(33) on the AudioInputStream. The underlying
		// sequence stream returns 1 byte - this is from the first stream.
		// Now, readFully wants to read the remaining bytes of the AudioInputStream:
		// read(32) and this leads to a nice exception in AudioInputStream, because
		// the requested data is not an integral frame size.
		// 2 solutions:
		// - increase this ByteArrayInputStream's size to 33 bytes
		// - use a BufferedInputStream
		// I prefer the latter. It also only uses 1 byte of memory.
		//
		// Anyway, I also updated our implementation of AudioInputStream to deal
		// with such problems. But it doesn't help much in other implementations.
		//
		/*byte[]	abHeader = new byte[1];
		AudioFileFormat	audioFileFormat =
			getAudioFileFormat(
				inputStream,
				abHeader);
		SequenceInputStream	sequenceInputStream =
			new SequenceInputStream(
				new ByteArrayInputStream(abHeader),
				inputStream);
		AudioInputStream	audioInputStream =
			new AudioInputStream(
				sequenceInputStream,
				audioFileFormat.getFormat(),
				audioFileFormat.getFrameLength());*/
		BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream, 1);
		bufferedInputStream.mark(1);
		AudioFileFormat	audioFileFormat = getAudioFileFormatImpl(bufferedInputStream);
		bufferedInputStream.reset();
		AudioInputStream	audioInputStream =
			new AudioInputStream(
				bufferedInputStream,
				audioFileFormat.getFormat(),
				audioFileFormat.getFrameLength());
		
		if (TDebug.TraceAudioFileReader)
		{
			TDebug.out("GSMAudioFileReader.getAudioInputStream(): end");
		}
		return audioInputStream;
	}



}



/*** GSMAudioFileReader.java ***/


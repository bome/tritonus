/*
 *	TRereadingAudioFileReader.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *  Copyright (c) 2001 by Florian Bomers <florian@bome.com>
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
 */

package	org.tritonus.share.sampled.file;


import	java.io.InputStream;
import	java.io.BufferedInputStream;
import	java.io.IOException;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.UnsupportedAudioFileException;

import	org.tritonus.share.TDebug;



/**	Base class for some compressed file formats.
	This class buffers the input stream before
	detecting the AudioFileFormat and resets it
	afterwards, before forming the AudioInputStream.
	The effect is that the complete content of the
	stream goes to the AudioInputStream. Headers are
	only read to report some properties in the
	AudioFileFormat. Typically, this behaviour is
	needed for compression schemas where the decoder
	(in Java Sound: the FormatConversionProvider)
	needs to read the complete bitstream including
	all headers.

	@author Matthias Pfisterer
	@author Florian Bomers
 */
public abstract class TRereadingAudioFileReader
	extends	TAudioFileReader
{
	/**	The amount of buffering in bytes.
		This value is used in constructing the BufferedInputStream.
		It should be big enough to hold the entire header of
		the audio file format.

		@see #TRereadingAudioFileReader(int)
		@see #getBufferingAmount()
	 */
	private int	m_nBufferingAmount;



	/**	Constructor.
		@param nBufferingAmount the amount of buffering in bytes that
		should be used in this instance.
		@see #m_nBufferingAmount
		@see #getBufferingAmount()
	 */
	protected TRereadingAudioFileReader(int nBufferingAmount)
	{
		if (TDebug.TraceAudioFileReader) {TDebug.out("TRereadingAudioFileReader.<init>(): begin"); }
		m_nBufferingAmount = nBufferingAmount;
		if (TDebug.TraceAudioFileReader) {TDebug.out("TRereadingAudioFileReader.<init>(): end"); }
	}



	/**	Get the amount of buffering in bytes.
		@return the number of bytes to be used for buffering.
		@see #m_nBufferingAmount
		@see #TRereadingAudioFileReader(int)
	 */
	private int getBufferingAmount()
	{
		return m_nBufferingAmount;
	}



	/**	Get an AudioInputStream.
		This implementation creates a BufferedInputStream on top of
		the stream passed in with a buffer size retrieved by
		getBufferingAmount(). It calls
		getAudioFileFormat(InputStream, long) with this buffered
		stream. After this call, the buffered stream is reset,
		before using it for the construction of the AudioInputStream.
		In other words, the whole content of the passed InputStream,
		including headers and such, goes to the AudioInputStream.

		@param inputStream	The InputStream to read from.
		@param lFileLengthInBytes	The size of the originating
		file, if known. If it isn't known, AudioSystem.NOT_SPECIFIED
		should be passed. This value may be used for byteLength in
		AudioFileFormat, if this value can't be derived from the
		informmation in the file header.
	*/
	protected AudioInputStream getAudioInputStream(InputStream inputStream, long lFileSizeInBytes)
		throws	UnsupportedAudioFileException, IOException
	{
		if (TDebug.TraceAudioFileReader) { TDebug.out("TRereadingAudioFileReader.getAudioInputStream(): begin"); }
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
		BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream, getBufferingAmount());
		bufferedInputStream.mark(getBufferingAmount());
		AudioFileFormat	audioFileFormat = getAudioFileFormat(bufferedInputStream, lFileSizeInBytes);
		bufferedInputStream.reset();
		AudioInputStream	audioInputStream =
			new AudioInputStream(
				bufferedInputStream,
				audioFileFormat.getFormat(),
				audioFileFormat.getFrameLength());
		if (TDebug.TraceAudioFileReader) { TDebug.out("TRereadingAudioFileReader.getAudioInputStream(): end"); }
		return audioInputStream;
	}
}



/*** TRereadingAudioFileReader.java ***/


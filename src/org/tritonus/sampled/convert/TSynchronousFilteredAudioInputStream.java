/*
 *	TSynchronousFilteredAudioInputStream.java
 */

/*
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


package	org.tritonus.sampled.convert;


import java.io.IOException;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioInputStream;
import	javax.sound.sampled.spi.FormatConversionProvider;

import	org.tritonus.TDebug;



// TODO: verify that this class works with sample rate converters

/** 
 * base class for any type of audio filter/converter.
 * It provides all the transformation of lengths and sizes.
 * **Note**
 * Currently, the pos field is not maintained ! Overriding
 * classes won't get happy when acessing this field -> it will
 * always be 0.
 *
 * @author Florian Bomers
 */
public abstract class TSynchronousFilteredAudioInputStream
	extends AudioInputStream
{
	private int	length;

	private AudioInputStream originalStream;
	private AudioFormat originalFormat;
	private int originalFrameSize;
		
	/**
	 * this is the factor by which frame indexes in original stream 
	 * have to be multiplied
	 * to obtain the frame index of this stream.
	 * It can be seen as new frames per original frame
	 * = (converted frame rate) / (original frame rate);
	 */
	// protected float frameRateFactor;
		
	/**
	 * = (converted frame size) / (original frame size);
	 */
	private float frameSizeFactor;
	private float inverseFrameSizeFactor;
		
	/**
	 * The intermediate buffer used during convert actions 
	 * (if not convertInPlace is used).
	 * It remains until this audioStream is closed or destroyed
	 * and grows with the time - it always has the size of the
	 * largest intermediate buffer ever needed.
	 */
	protected byte[] buffer=null;
		
	/**
	 * For use of the more efficient method convertInPlace.
	 * it will be set to true when (frameSizeFactor==1)
	 */
	private boolean	m_bConvertInPlace = false;



		
	public TSynchronousFilteredAudioInputStream(AudioInputStream audioInputStream, AudioFormat newFormat)
	{
		// the super class will do nothing... we override everything
		super(null, newFormat, audioInputStream.getFrameLength());
		length = (int) audioInputStream.getFrameLength();
		originalStream=audioInputStream;
		originalFormat=audioInputStream.getFormat();
		originalFrameSize=originalFormat.getFrameSize();
		if (TDebug.TraceAudioConverter)
		{
			System.out.println("TSynchronousFilteredAudioInputStream: original format ="+originalFormat);
			System.out.println("TSynchronousFilteredAudioInputStream: converted format="+ getFormat());
		}
		if (originalFrameSize == AudioSystem.NOT_SPECIFIED)
		{
			originalFrameSize = 1;
		}
		// frameRateFactor = (((float)getFormat().getFrameRate())/originalFormat.getFrameRate());
		frameSizeFactor = (((float)getFormat().getFrameSize())/originalFormat.getFrameSize());
		inverseFrameSizeFactor = ((float) getFormat().getFrameSize() / originalFormat.getFrameSize());
		if (getFormat().getFrameSize() == originalFormat.getFrameSize())
		{
			m_bConvertInPlace = true;
		}

		if (TDebug.TraceAudioConverter)
		{
			System.out.println("TSynchronousFilteredAudioInputStream: frameSizeFactor="+frameSizeFactor);
		}
/*
		if (length != AudioSystem.NOT_SPECIFIED)
		{
			// TODO: a bit cleaner, please!
			length = (int) (frameRateFactor*length);
		}
*/
		if (TDebug.TraceAudioConverter)
		{
			System.out.println("AudioStreamWrapper: originalLength="+audioInputStream.getFrameLength()+" this length="+length);
		}
	}


		
	/**
	 * Override this method to do the actual conversion.
	 * inBuffer starts always at index 0 (it is an internal buffer)
	 * You should always override this.
	 * inFrameCount is the number of frames in inBuffer. These
	 * frames are of the format originalFormat.
	 * @return the resulting number of <B>frames</B> converted and put into
	 * outBuffer. The return value is in the format of this stream.
	 */
	protected abstract int convert(byte[] inBuffer, byte[] outBuffer, int outByteOffset, int inFrameCount);



	/**
	 * Override this method to provide in-place conversion of samples.
	 * You should override it when canConvertInPlace is true.
	 * This method must always convert frameCount frames.
	 */
	protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount)
	{
		convert(buffer, buffer, byteOffset, frameCount);
	}


	
	public int read()
		throws IOException
	{
		if (getFormat().getFrameSize() != 1)
		{
			throw new IOException("frame size must be 1 to read a single byte");
		}
		// very ugly, but efficient. Who uses this method anyway ?
		// TODO: use an instance variable
		byte[] temp = new byte[1];
		int result = read(temp);
		if (result == -1)
		{
			return -1;
		}
		if (result == 0)
		{
				// what in this case ??? Let's hope it never occurs.
			return -1;
		}
		return temp[0];
	}



	private void clearBuffer()
	{
		buffer = null;
	}



	protected void calcPos()
	{
		//pos=(int) originalStream.pos/**frameRateFactor*/;
	}

	public AudioInputStream getOriginalStream() {
		return originalStream;
	}

	public AudioFormat getOriginalFormat() {
		return originalFormat;
	}

	/**
	 * Read nLength bytes that will be the converted samples
	 * of the original inputStream.
	 * When nLength*frameRateFactor is not a natural number,
	 * this method may read less than nLength frames.
	 */
	public int read(byte[] abData, int nOffset, int nLength)
		throws	IOException
	{
		int nFrameLength = nLength/getFormat().getFrameSize();
		if (TDebug.TraceAudioConverter)
		{
			System.out.println("converter.read(buffer["+abData.length+"], "+nOffset+" ,"+nLength+" bytes ^="+nFrameLength+" frames)");
		}
		int nFramesConverted = 0;
		if (m_bConvertInPlace)
		{
				// the trivial case: read directly
			int nBytesRead = originalStream.read(abData, nOffset, nFrameLength * getFormat().getFrameSize());
			if (nBytesRead == -1)
			{
				clearBuffer();
				return -1;
			}
			nFramesConverted = nBytesRead / originalFrameSize;
			convertInPlace(abData, nOffset, nFramesConverted);
		}
		else
		{
				// tricky...
				// this is the number of frames that we have to read from the 
				// underlying stream.
			int	originalFrameLength = (int) (nFrameLength/** inverseFrameRateFactor*/);
				// this is the number of bytes that we need to read from underlying
				// stream.
			int	originalBytes = originalFrameLength * originalFrameSize;
				// assert that the buffer fits
			if (buffer == null || buffer.length < originalBytes)
			{
				buffer = new byte[originalBytes];
			}
			if (TDebug.TraceAudioConverter)
			{
				System.out.println("converter: original.read(buffer["+buffer.length+"], 0, "+originalBytes+" bytes ^= "+originalFrameLength+" frames)");
			}
			int	nBytesRead = originalStream.read(buffer, 0, originalBytes);
			if (nBytesRead == -1)
			{
				clearBuffer();
				return -1;
			}
			int	originalFramesRead = nBytesRead / originalFrameSize;
			if (TDebug.TraceAudioConverter)
			{
				System.out.println("converter: original.read returned "+nBytesRead+" bytes ^="+originalFramesRead+" frames");
			}
				// finally convert it.
			nFramesConverted = convert(buffer, abData, nOffset, originalFramesRead);
			if (TDebug.TraceAudioConverter)
			{
				System.out.println("converter: convert converted "+nFramesConverted+" frames");
			}
		}
		//pos += nFramesConverted;
		calcPos();
		return nFramesConverted * getFormat().getFrameSize();
	}



	public long skip(long nSkip)
		throws	IOException
	{
		long skipFrames = nSkip / getFormat().getFrameSize();
		int originalSkipFrames = (int) (skipFrames/*/frameRateFactor*/);
		long originalSkippedBytes = originalStream.skip(originalSkipFrames*originalFrameSize);
		long originalSkippedFrames = originalSkippedBytes/originalFrameSize;
		
		long nSkippedFrames = (int) (originalSkippedFrames/**frameRateFactor*/);
		//pos += nSkippedFrames;
		calcPos();
		return nSkippedFrames * getFormat().getFrameSize();
	}



	public int available()
		throws IOException
	{
		int avail = originalStream.available();
		// TODO: validate this
		return (int) (avail * /*frameRateFactor**/ frameSizeFactor);
	}



	public void close()
		throws IOException
	{
		originalStream.close();
		buffer = null;
	}



	/*
	  public void mark(int readlimit)
	  {
	  originalStream.mark(readlimit);
	  }*/



	public void reset()
		throws IOException
	{
		originalStream.reset();
		calcPos();
	}



	/*
	  public boolean markSupported()
	  {
	  return originalStream.markSupported();
	  }


	  private int getFrameSize()
	  {
	  return getFormat().getFrameSize();
	  }
	*/

}


/*** TSynchronousFilteredAudioInputStream.java ***/

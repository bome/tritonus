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
import	org.tritonus.sampled.AudioUtils;



/** 
 * base class for types of audio filter/converter that translate one frame to another frame.
 * It provides all the transformation of lengths and sizes.
 *
 * @author Florian Bomers
 * $$fb 2000-07-18: extensive clean up
 */
public abstract class TSynchronousFilteredAudioInputStream
	extends AudioInputStream
{

	private AudioInputStream originalStream;
	private AudioFormat originalFormat;
	private int originalFrameSize;
		
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
	    super(audioInputStream /*null*/, newFormat, audioInputStream.getFrameLength());
		originalStream=audioInputStream;
		originalFormat=audioInputStream.getFormat();
		originalFrameSize=originalFormat.getFrameSize();
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("TSynchronousFilteredAudioInputStream: original format ="
				   +AudioUtils.format2ShortStr(originalFormat));
			TDebug.out("TSynchronousFilteredAudioInputStream: converted format="
				   +AudioUtils.format2ShortStr(getFormat()));
		}
		if (originalFrameSize == AudioSystem.NOT_SPECIFIED)
		{
			originalFrameSize = 1;
		}
		frameSizeFactor = (((float)getFormat().getFrameSize())/originalFormat.getFrameSize());
		inverseFrameSizeFactor = ((float) getFormat().getFrameSize() / originalFormat.getFrameSize());
		//$$fb 2000-07-17: convert in place has to be enabled explicitly with "enableConvertInPlace"
		/*if (getFormat().getFrameSize() == originalFormat.getFrameSize()) {
		  m_bConvertInPlace = true;
		}*/
		m_bConvertInPlace = false;
		
		if (TDebug.TraceAudioConverter)
		{
			TDebug.out("TSynchronousFilteredAudioInputStream: frameSizeFactor="+frameSizeFactor);
		}
		
	}
    
    protected boolean enableConvertInPlace() {
	if (getFormat().getFrameSize() == originalFormat.getFrameSize()) {
	    m_bConvertInPlace = true;
	}
	return m_bConvertInPlace;
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
	 * To use it, call "enableConvertInPlace()". It will only be used when
	 * input bytes per frame = output bytes per frame.
	 * This method must always convert frameCount frames, so no return value is necessary.
	 */
	protected void convertInPlace(byte[] buffer, int byteOffset, int frameCount)
	{
	    //convert(buffer, buffer, byteOffset, frameCount);
	    throw new RuntimeException("Illegal call to convertInPlace");
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
	    throws	IOException {
	    // number of frames that we have to read from the underlying stream.
	    int	nFrameLength = nLength/getFormat().getFrameSize();
	    
	    // number of bytes that we need to read from underlying stream.
	    int	originalBytes = nFrameLength * originalFrameSize;

	    if (TDebug.TraceAudioConverter) {
		//TDebug.out("converter.read(buffer["+abData.length+"], "
		//		   +nOffset+" ,"+nLength+" bytes ^="+nFrameLength+" frames)");
	    }
	    int nFramesConverted = 0;
	    
	    // set up buffer to read
	    byte readBuffer[];
	    int readOffset;
	    if (m_bConvertInPlace) {
		readBuffer=abData;
		readOffset=nOffset;
	    } else {
		// assert that the buffer fits
		if (buffer == null || buffer.length < originalBytes) {
		    buffer = new byte[originalBytes];
		}
		readBuffer=buffer;
		readOffset=0;
	    }
	    int nBytesRead = originalStream.read(readBuffer, readOffset, originalBytes);
	    if (nBytesRead == -1) {
		clearBuffer();
		return -1;
	    }
	    int nFramesRead = nBytesRead / originalFrameSize;
	    if (TDebug.TraceAudioConverter) {
		//TDebug.out("converter: original.read returned "
		//		   +nBytesRead+" bytes ^="+nFramesRead+" frames");
	    }
	    if (m_bConvertInPlace) {
		convertInPlace(abData, nOffset, nFramesRead);
		nFramesConverted=nFramesRead;
	    } else {
		nFramesConverted = convert(buffer, abData, nOffset, nFramesRead);
	    }
	    if (TDebug.TraceAudioConverter) {
		//TDebug.out("converter: convert converted "+nFramesConverted+" frames");
	    }
	    return nFramesConverted * getFormat().getFrameSize();
	}

	
    public long skip(long nSkip)
	throws	IOException
    {
	// only returns integral frames
	long skipFrames = nSkip / getFormat().getFrameSize();
	long originalSkippedBytes = originalStream.skip(skipFrames*originalFrameSize);
	long nSkippedFrames = (int) (originalSkippedBytes/originalFrameSize);
	return nSkippedFrames * getFormat().getFrameSize();
    }


    public int available()
	throws IOException
    {
	int avail = originalStream.available();
	if (avail>0) {
	    avail=(int) (avail * frameSizeFactor);
	}
	return avail;
    }


    public void close()
	throws IOException
    {
	originalStream.close();
	clearBuffer();
    }



    public void mark(int readlimit)
    {
	originalStream.mark((int) (readlimit*inverseFrameSizeFactor));
    }



    public void reset()
	throws IOException
    {
	originalStream.reset();
    }


    public boolean markSupported()
    {
	return originalStream.markSupported();
    }


    private int getFrameSize()
    {
	return getFormat().getFrameSize();
    }

}


/*** TSynchronousFilteredAudioInputStream.java ***/

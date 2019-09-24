/*
 *	TClip.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share.sampled.mixer;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;

import org.tritonus.share.TDebug;
import org.tritonus.share.sampled.mixer.TDataLine;



public class TClip
extends	TDataLine
implements Clip
{
	//$$fb the following fields are never used
	//private static final Class[]	CONTROL_CLASSES = {/*GainControl.class*/};
	//private static final int	BUFFER_FRAMES = 16384;


	public TClip(DataLine.Info info)
	{
		super(null,	// TMixer
		      info);
	}



	public TClip(DataLine.Info info,
		     Collection<Control> controls)
	{
		super(null,	// TMixer
		      info,
		      controls);
	}



	public void open(AudioFormat audioFormat,
			 byte[] abData,
			 int nOffset,
			 int nLength)
		throws LineUnavailableException
	{
		// int	nBufferLength = nNumFrames * audioFormat.getFrameSize();
		// TODO: check if nOffset + nBufferLength <= abData.length
		// perhaps truncate automatically
		ByteArrayInputStream	bais = new ByteArrayInputStream(abData, nOffset, nLength);
		AudioInputStream	audioInputStream = new AudioInputStream(bais, audioFormat, AudioSystem.NOT_SPECIFIED);
		try
		{
			open(audioInputStream);
		}
		catch (IOException e)
		{
			if (TDebug.TraceAllExceptions)
			{
				TDebug.out(e);
			}
			throw new LineUnavailableException("IOException occured");
		}
	}



	public void open(AudioInputStream audioInputStream)
		throws LineUnavailableException, IOException
	{
		AudioFormat	audioFormat = audioInputStream.getFormat();
		// TOOD:
		DataLine.Info	info = new DataLine.Info(Clip.class,
							 audioFormat, -1/*nBufferSize*/);
  setLineInfo(info);
  /*
  int	nFrameSize = audioFormat.getFrameSize();
  long	lTotalLength = audioInputStream.getFrameLength() * nFrameSize;
  int	nFormat = Esd.ESD_STREAM | Esd.ESD_PLAY | EsdUtils.getEsdFormat(audioFormat);
  if (TDebug.TraceClip)
  {
  TDebug.out("format: " + nFormat);
  TDebug.out("sample rate: " + audioFormat.getSampleRate());
  }
  // m_esdSample.open(nFormat, (int) audioFormat.getSampleRate(), (int) lTotalLength);
  if (TDebug.TraceClip)
  {
  TDebug.out("size in esd: " + audioInputStream.getFrameLength() * nFrameSize);
  }
  int	nBufferLength = BUFFER_FRAMES * nFrameSize;
  byte[]	abData = new byte[nBufferLength];
  int	nBytesRead = 0;
  int	nTotalBytes = 0;
  while (nBytesRead != -1)
  {
  try
  {
  nBytesRead = audioInputStream.read(abData, 0, abData.length);
  }
  catch (IOException e)
  {
  if (TDebug.TraceClip || TDebug.TraceAllExceptions)
  {
  TDebug.out(e);
  }
  }
  if (nBytesRead >= 0)
  {
  nTotalBytes += nBytesRead;
  if (TDebug.TraceClip)
  {
  TDebug.out("TClip.open(): total bytes: " + nTotalBytes);
  TDebug.out("TClip.open(): Trying to write: " + nBytesRead);
  }
  int	nBytesWritten = 0; //m_esdSample.write(abData, 0, nBytesRead);
  if (TDebug.TraceClip)
  {
  TDebug.out("TClip.open(): Written: " + nBytesWritten);
  }
  }
  }
  // to trigger the events
  // open();
  */
	}



	public int getFrameLength()
	{
		// TODO:
		return -1;
	}



	public long getMicrosecondLength()
	{
		// TODO:
		return -1;
	}



	public void setFramePosition(int nPosition)
	{
		// TOOD:
	}



	public void setMicrosecondPosition(long lPosition)
	{
		// TOOD:
	}



	public int getFramePosition()
	{
		// TOOD:
		return -1;
	}



	public long getMicrosecondPosition()
	{
		// TOOD:
		return -1;
	}



	public void setLoopPoints(int nStart, int nEnd)
	{
		// TOOD:
	}



	public void loop(int nCount)
	{
		if (TDebug.TraceClip)
		{
			TDebug.out("TClip.loop(int): called; count = " + nCount);
		}
		if (false/*isStarted()*/)
		{
			/*
			 *	only allow zero count to stop the looping
			 *	at the end of an iteration.
			 */
			if (nCount == 0)
			{
				if (TDebug.TraceClip)
				{
					TDebug.out("TClip.loop(int): stopping sample");
				}
				// m_esdSample.stop();
			}
		}
		else
		{
			if (nCount == 0)
			{
				if (TDebug.TraceClip)
				{
					TDebug.out("TClip.loop(int): starting sample (once)");
				}
				// m_esdSample.play();
			}
			else
			{
				/*
				 *	we're ignoring the count, because esd
				 *	cannot loop for a fixed number of
				 *	times.
				 */
				// TDebug.out("hallo");
				if (TDebug.TraceClip)
				{
					TDebug.out("TClip.loop(int): starting sample (forever)");
				}
				// m_esdSample.loop();
			}
		}
		// TOOD:
	}



	public void flush()
	{
		// TOOD:
	}



	public void drain()
	{
		// TOOD:
	}



	public void close()
	{
		// m_esdSample.free();
		// m_esdSample.close();
		// TOOD:
	}




	public void open()
	{
		// TODO:
	}



	public void start()
	{
		if (TDebug.TraceClip)
		{
			TDebug.out("TClip.start(): called");
		}
		/*
		 *	This is a hack. What start() really should do is
		 *	start playing at the position playback was stopped.
		 */
		if (TDebug.TraceClip)
		{
			TDebug.out("TClip.start(): calling 'loop(0)' [hack]");
		}
		loop(0);
	}



	public void stop()
	{
		// TODO:
		// m_esdSample.kill();
	}



	/*
	 *	This method is enforced by DataLine, but doesn't make any
	 *	sense for Clips.
	 */
	public int available()
	{
		return -1;
	}
}



/*** TClip.java ***/


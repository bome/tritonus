/*
 *	JEsdSourceDataLine.java
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


package	org.tritonus.sampled.mixer.jesd;


import	java.io.IOException;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.Mixer;

import	org.tritonus.TDebug;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.mixer.TMixer;
import	org.tritonus.sampled.mixer.TSourceTargetDataLine;

import	com.jcraft.jesd.JEsd;
import	com.jcraft.jesd.JEsdException;



public class JEsdSourceDataLine
	extends		TSourceTargetDataLine
	implements	SourceDataLine
{
	// private static final Class[]	CONTROL_CLASSES = {GainControl.class};


	private JEsd			m_esdStream;
	private boolean			m_bSwapBytes;
	private byte[]			m_abSwapBuffer;

	/*
	 *	Only used if m_bSwapBytes is true.
	 */
	private int			m_nBytesPerSample;





	// TODO: has info object to change if format or buffer size are changed later?
	// no, but it has to represent the mixer's capabilities. So a fixed info per mixer.
	public JEsdSourceDataLine(TMixer mixer, AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		super(mixer,
		      new DataLine.Info(SourceDataLine.class,
					format,
					nBufferSize));
/*
  if (TDebug.TraceSourceDataLine)
  {
  TDebug.out("JEsdSourceDataLine.<init>(): buffer size: " + nBufferSize);
  }
*/
	}



	protected void openImpl()
		throws	LineUnavailableException
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("JEsdSourceDataLine.openImpl(): called.");
		}
		/*
		 *	Checks that a format is set.
		 *	Sets the buffer size to a default value if not
		 *	already set.
		 */
		checkOpen();
		AudioFormat	format = getFormat();
		AudioFormat.Encoding	encoding  = format.getEncoding();
		boolean			bBigEndian = format.isBigEndian();
		m_bSwapBytes = false;
		if (format.getSampleSizeInBits() == 16 && !bBigEndian)
		{
			m_bSwapBytes = true;
			bBigEndian = true;	// the desired value
		}
		else if (format.getSampleSizeInBits() == 8 &&
			 encoding.equals(AudioFormat.Encoding.PCM_SIGNED))
		{
			m_bSwapBytes = true;
			encoding = AudioFormat.Encoding.PCM_UNSIGNED;
		}
		if (m_bSwapBytes)
		{
			format = new AudioFormat(encoding,
						 format.getSampleRate(),
						 format.getSampleSizeInBits(),
						 format.getChannels(),
						 format.getFrameSize(),
						 format.getFrameRate(),
						 bBigEndian);
			m_nBytesPerSample = format.getFrameSize() / format.getChannels();
		}
		int	nOutFormat = JEsd.ESD_STREAM | JEsd.ESD_PLAY | JEsdUtils.getJEsdFormat(format);
		try
		{
			m_esdStream = JEsd.play_stream_fallback(
				nOutFormat, 
				(int) format.getSampleRate(),
				"localhost", 
				"");
		}
		catch (JEsdException e)
		{
			throw new LineUnavailableException(/*TODO: */);
		}
	}



	protected void closeImpl()
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("JEsdSourceDataLine.closeImpl(): called");
		}
		m_esdStream.close();
	}



/*
	public void start()
	{
		setStarted(true);
		setActive(true);
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("JEsdSourceDataLine.start(): channel started.");
		}
	}



	public void stop()
	{
		setStarted(false);
	}
*/


	public int available()
	{
		// TODO:
		return -1;
		// return m_nAvailable;
	}




	// TODO: check if should block
	public int write(byte[] abData, int nOffset, int nLength)
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("JEsdSourceDataLine.write(): called.");
		}
		if (m_bSwapBytes)
		{
			if (m_abSwapBuffer == null || m_abSwapBuffer.length < nOffset + nLength)
			{
				m_abSwapBuffer = new byte[nOffset + nLength];
			}
			TConversionTool.changeOrderOrSign(
				abData, nOffset,
				m_abSwapBuffer, nOffset,
				nLength, m_nBytesPerSample);
			abData = m_abSwapBuffer;
		}
		if (nLength > 0 && !isActive())
		{
			start();
		}
		int		nRemaining = nLength;
		while (nRemaining > 0 && isOpen())
		{
			synchronized (this)
			{
				/*
				  while ((availableWrite() == 0 || isPaused()) && isOpen())
				  {
				  try
				  {
				  wait();
				  }
				  catch (InterruptedException e)
				  {
				  }
				  }
				*/
				if (!isOpen())
				{
					return nLength - nRemaining;
				}
				// TODO: check return 
				int nWritten = m_esdStream.write(abData, nOffset, nRemaining);
				nOffset += nWritten;
				nRemaining -= nWritten;
			}
		}
		return nLength;
	}



	public void drain()
	{
		// TODO:
	}



	public void flush()
	{
		// TODO:
	}



	/**
	 *	dGain is logarithmic!!
	 */
	private void setGain(float dGain)
	{
	}



	// IDEA: move inner classes to TSourceTargetDataLine
	public class JEsdSourceDataLineGainControl
		extends		FloatControl
	{
		/*
		 *	These variables should be static. However, Java 1.1
		 *	doesn't allow this. So they aren't.
		 */
		private /*static*/ final float	MAX_GAIN = 90.0F;
		private /*static*/ final float	MIN_GAIN = -96.0F;

		// TODO: recheck this value
		private /*static*/ final int	GAIN_INCREMENTS = 1000;

		// private float		m_fGain;
		// private boolean		m_bMuted;



		/*package*/ JEsdSourceDataLineGainControl()
		{
			super(FloatControl.Type.VOLUME,	// or MASTER_GAIN ?
			      -96.0F,	// MIN_GAIN,
			      24.0F,	// MAX_GAIN,
			      0.01F,	// precision
			      0,	// update period?
			      0.0F,	// initial value
			      "dB",
			      "-96.0",
			      "",
			      "+24.0");
			// m_bMuted = false;	// should be included in a compund control?
		}



		public void setValue(float fGain)
		{
			fGain = Math.max(Math.min(fGain, getMaximum()), getMinimum());
			if (Math.abs(fGain - getValue()) > 1.0E9)
			{
				super.setValue(fGain);
				// if (!getMute())
				// {
				JEsdSourceDataLine.this.setGain(getValue());
				// }
			}
		}


/*
  public float getMaximum()
  {
  return MAX_GAIN;
  }



  public float getMinimum()
  {
  return MIN_GAIN;
  }



  public int getIncrements()
  {
  // TODO: check this value
  return GAIN_INCREMENTS;
  }



  public void fade(float fInitialGain, float fFinalGain, int nFrames)
  {
  // TODO:
  }



  public int getFadePrecision()
  {
  //TODO:
  return -1;
  }



  public boolean getMute()
  {
  return m_bMuted;
  }



  public void setMute(boolean bMuted)
  {
  if (bMuted != getMute())
  {
  m_bMuted = bMuted;
  if (getMute())
  {
  JEsdSourceDataLine.this.setGain(getMinimum());
  }
  else
  {
  JEsdSourceDataLine.this.setGain(getGain());
  }
  }
  }
*/


	}
}



/*** JEsdSourceDataLine.java ***/

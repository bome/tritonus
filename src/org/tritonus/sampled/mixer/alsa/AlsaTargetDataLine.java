/*
 *	AlsaTargetDataLine.java
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


package	org.tritonus.sampled.mixer.alsa;


import	java.io.IOException;

import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.TargetDataLine;
import	javax.sound.sampled.Mixer;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.Alsa;
import	org.tritonus.lowlevel.alsa.AlsaPcm;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.mixer.TMixer;
import	org.tritonus.sampled.mixer.TSourceTargetDataLine;



public class AlsaTargetDataLine
	extends		TSourceTargetDataLine
	implements	TargetDataLine
{
	private AlsaPcm			m_alsaPcm;
	private boolean			m_bSwapBytes;
	private byte[]			m_abSwapBuffer;

	/*
	 *	Only used if m_bSwapBytes is true.
	 */
	private int			m_nBytesPerSample;

	private byte[]			m_abFragmentBuffer;
	private int			m_nFragmentBufferUsedBytes;
	private int			m_nFragmentSize = 1024;



	public AlsaTargetDataLine(TMixer mixer, AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		// TODO: use an info object that represents the mixer's capabilities (all possible formats for the line)
		super(mixer,
		      new DataLine.Info(TargetDataLine.class,
					format,
					nBufferSize)/*,
						      // TODO: has info object to change if format or buffer size are changed later?
						      format, nBufferSize*/);
	}



	protected void openImpl()
		throws	LineUnavailableException
	{
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.openImpl(): called.");
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
		if (format.getSampleSizeInBits() == 16 && bBigEndian)
		{
			m_bSwapBytes = true;
			bBigEndian = false;
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
		int	nOutFormat = AlsaUtils.getAlsaFormat(format);

		try
		{
			m_alsaPcm = new AlsaPcm(0, 0, AlsaPcm.SND_PCM_OPEN_CAPTURE);
		}
		catch (Exception e)
		{
			throw new LineUnavailableException();
		}
		int	nReturn;
		// outputStatus();
		nReturn = m_alsaPcm.setChannelParams(
			AlsaPcm.SND_PCM_CHANNEL_CAPTURE,
			AlsaPcm.SND_PCM_MODE_BLOCK,
			true,
			nOutFormat,
			(int) format.getSampleRate(),
			format.getChannels(),
			0,
			AlsaPcm.SND_PCM_START_DATA,
			AlsaPcm.SND_PCM_STOP_ROLLOVER,
			false,
			false,
			m_nFragmentSize,
			2,
			1000);
		if (nReturn != 0)
		{
			TDebug.out("setChannelParams: " + Alsa.getStringError(nReturn));
		}
		outputStatus();
		nReturn = m_alsaPcm.prepareChannel(AlsaPcm.SND_PCM_CHANNEL_CAPTURE);
		if (nReturn != 0)
		{
			TDebug.out("prepareChannel: " + Alsa.getStringError(nReturn));
		}
		outputStatus();
		if (m_abFragmentBuffer == null)
		{
			m_abFragmentBuffer = new byte[m_nFragmentSize];
		}
		m_nFragmentBufferUsedBytes = 0;
	}



	protected void closeImpl()
	{
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.closeImpl(): called.");
		}
		m_alsaPcm.close();
	}



/*
  public void start()
  {
  // m_alsaPcm.goCapture();
  setStarted(true);
  setActive(true);
  if (TDebug.TraceSourceDataLine)
  {
  TDebug.out("AlsaTargetDataLine.start(): channel started.");
  }
  }
*/

	private void outputStatus()
	{
		int	nReturn;
		int[]	anValues = new int[9];
		long[]	alValues = new long[2];

		nReturn = m_alsaPcm.getChannelStatus(
			AlsaPcm.SND_PCM_CHANNEL_CAPTURE,
			anValues,
			alValues);
		if (nReturn != 0)
		{
			TDebug.out("getChannelStatus: " + Alsa.getStringError(nReturn));
		}
		TDebug.out("Mode: " + anValues[0]);
		TDebug.out("Status: " + anValues[1]);
	}



	protected void stopImpl()
	{
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.stopImpl(): called.");
		}
		int	nReturn = m_alsaPcm.flushChannel(AlsaPcm.SND_PCM_CHANNEL_CAPTURE);
		if (nReturn != 0)
		{
			TDebug.out("flushChannel: " + Alsa.getStringError(nReturn));
		}
	}



	public int available()
	{
		// TODO:
		return -1;
	}




	// TODO: check if should block
	public int read(byte[] abData, int nOffset, int nLength)
	{
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.read(): called.");
			TDebug.out("AlsaTargetDataLine.read(): wanted length: " + nLength);
		}
		outputStatus();
		int	nOriginalOffset = nOffset;
		if (nLength > 0 && !isActive())
		{
			start();
		}
		if (!isOpen())
		{
			if (TDebug.TraceTargetDataLine)
			{
				TDebug.out("AlsaTargetDataLine.read(): stream closed");
			}
		}
		int	nBytesRead = readImpl(abData, nOffset, nLength);
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.read(): read (bytes): " + nBytesRead);
		}
		if (m_bSwapBytes && nBytesRead > 0)
		{
			TConversionTool.swapOrder16(abData, nOriginalOffset, nBytesRead / 2);
		}
		return nBytesRead;
	}



	public int readImpl(byte[] abData, int nOffset, int nLength)
	{
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.readImpl(): length: " + nLength);
		}
		// TODO: recheck semantics of start()/active
		if (nLength > 0 && !isActive())
		{
			start();
		}
		int		nRemainingLength = nLength;
		if (m_nFragmentBufferUsedBytes > 0)
		{
			if (TDebug.TraceTargetDataLine)
			{
				TDebug.out("AlsaTargetDataLine.readImpl(): current used fragment buffer length: " + m_nFragmentBufferUsedBytes);
			}
			/*
			 *	Try to use buffer
			 */
			int	nCopyLength = Math.min(m_nFragmentBufferUsedBytes, nRemainingLength);
			System.arraycopy(m_abFragmentBuffer, nCopyLength, abData, nOffset, nCopyLength);
			// TODO: move data in fragment buffer!!
			m_nFragmentBufferUsedBytes -= nCopyLength;
			nOffset += nCopyLength;
			nRemainingLength -= nCopyLength;
		}
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.readImpl(): remaining length: " + nRemainingLength);
		}
		int	nDirectLength = nRemainingLength - (nRemainingLength % m_nFragmentSize);
		if (TDebug.TraceTargetDataLine)
		{
			TDebug.out("AlsaTargetDataLine.readImpl(): direct length: " + nDirectLength);
		}
		if (nDirectLength > 0)
		{
			int	nRead = m_alsaPcm.read(abData, nOffset, /*m_nFragmentSize*/nDirectLength);
			if (nRead < 0)
			{
				TDebug.out("AlsaTargetDataLine.readImpl(): " + Alsa.getStringError(nRead));
			}
			// TODO: check if nRead == nDirectLength
			nOffset += nDirectLength;
			nRemainingLength -= nDirectLength;
		}
		if (nRemainingLength > 0)
		{
			if (TDebug.TraceTargetDataLine)
			{
				TDebug.out("AlsaTargetDataLine.readImpl(): remaining length: " + nRemainingLength);
			}
			int	nRead = m_alsaPcm.read(m_abFragmentBuffer, 0, m_abFragmentBuffer.length);
			if (nRead < 0)
			{
				TDebug.out("AlsaTargetDataLine.read(): " + Alsa.getStringError(nRead));
			}
			m_nFragmentBufferUsedBytes = m_abFragmentBuffer.length;
			System.arraycopy(m_abFragmentBuffer, 0, abData, nOffset, nRemainingLength);
		}
/*
  if (m_abFragmentBuffer.length == m_nFragmentBufferUsedBytes)
  {
  int	nWritten = m_alsaPcm.read(m_abFragmentBuffer, 0, m_abFragmentBuffer.length);
  if (nWritten < 0)
  {
  TDebug.out("AlsaTargetDataLine.read(): " + Alsa.getStringError(nWritten));
  }
  m_nFragmentBufferUsedBytes = 0;
  }
*/
		return nLength;
	}


/*	// TODO: check if should block
	public int readImpl(byte[] abData, int nOffset, int nLength)
	{
	if (TDebug.TraceTargetDataLine)
	{
	TDebug.out("AlsaTargetDataLine.readImpl(): called.");
	TDebug.out("AlsaTargetDataLine.readImpl(): wanted length: " + nLength);
	}
	int	nOriginalOffset = nOffset;
	if (nLength > 0 && !isActive())
	{
	start();
	}
	if (!isOpen())
	{
	if (TDebug.TraceTargetDataLine)
	{
	TDebug.out("AlsaTargetDataLine.readImpl(): stream closed");
	}
	}
	int	nBytesRead = m_alsaPcm.read(abData, nOffset, nLength);
	if (nBytesRead < 0)
	{
	TDebug.out("AlsaTargetDataLine.redaImpl(): " + Alsa.getStringError(nBytesRead));
	}
	if (TDebug.TraceTargetDataLine)
	{
	TDebug.out("AlsaTargetDataLine.readImpl(): read (bytes): " + nBytesRead);
	}
	return nBytesRead;
	}
*/



	public void drain()
	{
		// TODO:
	}



	public void flush()
	{
		// TODO:
	}



	public long getPosition()
	{
		// TODO:
		return 0;
	}



	/**
	 *	fGain is logarithmic!!
	 */
	private void setGain(float fGain)
	{
	}



	public class AlsaTargetDataLineGainControl
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



		/*package*/ AlsaTargetDataLineGainControl()
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
				AlsaTargetDataLine.this.setGain(getValue());
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
  AlsaTargetDataLine.this.setGain(getMinimum());
  }
  else
  {
  AlsaTargetDataLine.this.setGain(getGain());
  }
  }
  }
*/


	}
}



/*** AlsaTargetDataLine.java ***/

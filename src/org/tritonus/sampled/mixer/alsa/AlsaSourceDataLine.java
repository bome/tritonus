/*
 *	AlsaSourceDataLine.java
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

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.Mixer;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.Alsa;
import	org.tritonus.lowlevel.alsa.AlsaPcm;
import	org.tritonus.sampled.TConversionTool;
import	org.tritonus.sampled.mixer.TMixer;
import	org.tritonus.sampled.mixer.TSourceTargetDataLine;




public class AlsaSourceDataLine
	extends		TSourceTargetDataLine
	implements	SourceDataLine
{
	// private static final Class[]	CONTROL_CLASSES = {GainControl.class};


	private int			m_nCard;
	private AlsaPcm			m_alsaPcm;
	private boolean			m_bSwapBytes;
	private byte[]			m_abSwapBuffer;

	/*
	 *	Only used if m_bSwapBytes is true.
	 */
	private int			m_nBytesPerSample;

	private byte[]			m_abFragmentBuffer;
	private int			m_nFragmentBufferUsedBytes;
	private int			m_nFragmentSize = 128;



	// TODO: has info object to change if format or buffer size are changed later?
	// no, but it has to represent the mixer's capabilities. So a fixed info per mixer.
	public AlsaSourceDataLine(AlsaMixer mixer, AudioFormat format, int nBufferSize)
		throws	LineUnavailableException
	{
		super(mixer,
		      new DataLine.Info(SourceDataLine.class,
					format,
					nBufferSize));
		m_nCard = mixer.getCard();
/*
  if (TDebug.TraceSourceDataLine)
  {
  TDebug.out("AlsaSourceDataLine.<init>(): buffer size: " + nBufferSize);
  }
*/
	}



	protected void openImpl()
		throws	LineUnavailableException
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.openImpl(): called.");
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
			m_alsaPcm = new AlsaPcm(m_nCard, 0, AlsaPcm.SND_PCM_OPEN_PLAYBACK);
		}
		catch (Exception e)
		{
			throw new LineUnavailableException();
		}
		int	nReturn;
/*
  nReturn = m_alsaPcm.flushChannel(AlsaPcm.SND_PCM_CHANNEL_PLAYBACK);
  if (nReturn != 0)
  {
  TDebug.out("flushChannel: " + Alsa.getStringError(nReturn));
  }
*/
		nReturn = m_alsaPcm.setChannelParams(
			AlsaPcm.SND_PCM_CHANNEL_PLAYBACK,
			AlsaPcm.SND_PCM_MODE_BLOCK,
			true,
			nOutFormat,
			(int) format.getSampleRate(),
			format.getChannels(),
			0,
			AlsaPcm.SND_PCM_START_FULL,
			AlsaPcm.SND_PCM_STOP_ROLLOVER,
			false,
			false,
			m_nFragmentSize,
			2,
			-1);
		if (nReturn != 0)
		{
			TDebug.out("setChannelParams: " + Alsa.getStringError(nReturn));
		}
		nReturn = m_alsaPcm.prepareChannel(AlsaPcm.SND_PCM_CHANNEL_PLAYBACK);
		if (nReturn != 0)
		{
			TDebug.out("prepareChannel: " + Alsa.getStringError(nReturn));
		}
		if (m_abFragmentBuffer == null)
		{
			m_abFragmentBuffer = new byte[m_nFragmentSize];
		}
		m_nFragmentBufferUsedBytes = 0;
	}



	protected void closeImpl()
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.closeImpl(): called");
		}
		m_alsaPcm.close();
	}



/*
	public void start()
	{
		setStarted(true);
		setActive(true);
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.start(): channel started.");
		}
	}
*/


	protected void stopImpl()
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.stopImpl(): called");
		}
		int	nReturn = m_alsaPcm.flushChannel(AlsaPcm.SND_PCM_CHANNEL_PLAYBACK);
		if (nReturn != 0)
		{
			TDebug.out("flushChannel: " + Alsa.getStringError(nReturn));
		}
		// setStarted(false);
	}



	public int available()
	{
		// TODO:
		return -1;
	}




	// TODO: check if should block
	public int write(byte[] abData, int nOffset, int nLength)
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.write(): called.");
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
		return writeImpl(abData, nOffset, nLength);
	}



	// TODO: check if should block
	public int writeImpl(byte[] abData, int nOffset, int nLength)
	{
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.writeImpl(): length: " + nLength);
		}
		// TODO: recheck semantics of start()/active
		if (nLength > 0 && !isActive())
		{
			start();
		}
		int		nRemainingLength = nLength;
		if (m_nFragmentBufferUsedBytes > 0)
		{
			if (TDebug.TraceSourceDataLine)
			{
				TDebug.out("AlsaSourceDataLine.writeImpl(): current used fragment buffer length: " + m_nFragmentBufferUsedBytes);
			}
			/*
			 *	Try to fill buffer; write if full
			 */
			int	nCopyLength = Math.min(m_abFragmentBuffer.length - m_nFragmentBufferUsedBytes, nRemainingLength);
			System.arraycopy(abData, nOffset, m_abFragmentBuffer, m_nFragmentBufferUsedBytes, nCopyLength);
			m_nFragmentBufferUsedBytes += nCopyLength;
			nOffset += nCopyLength;
			nRemainingLength -= nCopyLength;
			if (m_abFragmentBuffer.length == m_nFragmentBufferUsedBytes)
			{
				int	nWritten = m_alsaPcm.write(m_abFragmentBuffer, 0, m_abFragmentBuffer.length);
				if (nWritten < 0)
				{
				TDebug.out("AlsaSourceDataLine.write(): " + Alsa.getStringError(nWritten));
				}
				m_nFragmentBufferUsedBytes = 0;
			}
		}
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.writeImpl(): remaining length: " + nRemainingLength);
			// TDebug.out("AlsaSourceDataLine.writeImpl(): frag size: " + m_nFragmentSize);
			// TDebug.out("AlsaSourceDataLine.writeImpl(): rem % frag size: " + nRemainingLength % m_nFragmentSize);
		}
		int	nDirectLength = nRemainingLength - (nRemainingLength % m_nFragmentSize);
		if (TDebug.TraceSourceDataLine)
		{
			TDebug.out("AlsaSourceDataLine.writeImpl(): direct length: " + nDirectLength);
		}
		if (nDirectLength > 0)
		{
			int	nWritten = m_alsaPcm.write(abData, nOffset, nDirectLength);
			if (nWritten < 0)
			{
				TDebug.out("AlsaSourceDataLine.write(): " + Alsa.getStringError(nWritten));
			}
			nOffset += nDirectLength;
			nRemainingLength -= nDirectLength;
		}
		if (nRemainingLength > 0)
		{
			if (TDebug.TraceSourceDataLine)
			{
				TDebug.out("AlsaSourceDataLine.writeImpl(): remaining length: " + nRemainingLength);
			}
			System.arraycopy(abData, nOffset, m_abFragmentBuffer, 0, nRemainingLength);
			m_nFragmentBufferUsedBytes = nRemainingLength;
		}
		return nLength;
	}


/*
  // TODO: check if should block
  public int writeImpl(byte[] abData, int nOffset, int nLength)
  {
  if (TDebug.TraceSourceDataLine)
  {
  TDebug.out("AlsaSourceDataLine.write(): called.");
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
  if (!isOpen())
  {
  return nLength - nRemaining;
  }
				// TODO: check return 
				if (TDebug.TraceSourceDataLine)
				{
				TDebug.out("AlsaSourceDataLine.write(): trying to write (bytes): " + nRemaining);
				}
				int nWritten = m_alsaPcm.write(abData, nOffset, nRemaining);
				if (nWritten < 0)
				{
				TDebug.out("AlsaSourceDataLine.write(): " + Alsa.getStringError(nWritten));
				}
				if (TDebug.TraceSourceDataLine)
				{
				TDebug.out("AlsaSourceDataLine.write(): written (bytes): " + nWritten);
				}
				nOffset += nWritten;
				nRemaining -= nWritten;
				}
				}
				return nLength;
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



	/**
	 *	dGain is logarithmic!!
	 */
	private void setGain(float dGain)
	{
	}



	// IDEA: move inner classes to TSourceTargetDataLine
	public class AlsaSourceDataLineGainControl
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



		/*package*/ AlsaSourceDataLineGainControl()
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
				AlsaSourceDataLine.this.setGain(getValue());
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
  AlsaSourceDataLine.this.setGain(getMinimum());
  }
  else
  {
  AlsaSourceDataLine.this.setGain(getGain());
  }
  }
  }
*/


	}
}



/*** AlsaSourceDataLine.java ***/

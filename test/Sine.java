/*
 *	Sine.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.SourceDataLine;



public class Sine
{



	public static void main(String[] args)
	{
		byte[]		abData;
		AudioFormat		m_format;
		int	nSignalType = 1;	// 1 = sine, 2 = square
		int	nAmplitude = 20000;	// [0..32767]
		int	nSampleFrequency = 44100;
		int	nSignalFrequency = 1000;
		float	fMaximumBufferLengthInSeconds = 1.0F;

		int	nMaximumBufferLengthInFrames = (int) (fMaximumBufferLengthInSeconds * nSampleFrequency);
		// length of one period in frames
		int nPeriodLengthInFrames = nSampleFrequency / nSignalFrequency;
		// make even
		if ((nPeriodLengthInFrames % 2) != 0)
		{
			nPeriodLengthInFrames++;
		}
		int nNumPeriodsInBuffer = nMaximumBufferLengthInFrames / nPeriodLengthInFrames;
		int nNumFramesInBuffer = nNumPeriodsInBuffer * nPeriodLengthInFrames;
		// 2 bytes/sample (16 bit), 2 samples/frame (stereo)
		int nBufferLength = nNumFramesInBuffer * 4;
		m_format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					   nSampleFrequency, 16, 2, 4, nSampleFrequency, false);
		abData = new byte[nBufferLength];
		for (int nPeriod = 0; nPeriod < nNumPeriodsInBuffer; nPeriod++)
		{
			for (int nFrame = 0; nFrame < nPeriodLengthInFrames; nFrame++)
			{
				int nValue = 0;
				switch (nSignalType)
				{
				case 1:	// sine
					nValue = (int) (Math.sin(((double)nFrame / (double) nPeriodLengthInFrames) * 2.0 * Math.PI) * nAmplitude);
					break;
				case 2:	// square
					if (nFrame < nPeriodLengthInFrames / 2)
					{
						nValue = nAmplitude;
					}
					else
					{
						nValue = -nAmplitude;
					}
				}
				int nBaseAddr = (nPeriod * nPeriodLengthInFrames + nFrame) * 4;
				abData[nBaseAddr + 0] = (byte) (nValue & 0xFF);
				abData[nBaseAddr + 1] = (byte) ((nValue >>> 8) & 0xFF);
				abData[nBaseAddr + 2] = (byte) (nValue & 0xFF);
				abData[nBaseAddr + 3] = (byte) ((nValue >>> 8) & 0xFF);
			}
		}
		SourceDataLine	line = null;
		DataLine.Info	info = new DataLine.Info(SourceDataLine.class, m_format, AudioSystem.NOT_SPECIFIED);
		try
		{
			line = (SourceDataLine) AudioSystem.getLine(info);
			// line.addLineListener(this);
			line.open(m_format, line.getBufferSize());
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		line.start();


		while (true)
		{
			System.out.println("Sine.main(): in loop, trying to write: " + abData.length / m_format.getFrameSize());
			int	nWritten = line.write(abData, 0, abData.length / m_format.getFrameSize());
			System.out.println("Sine.main(): written: " + nWritten);
		}
	}



}



/*** Sine.java ***/

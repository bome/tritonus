/*
 *	AudioOutputStreamOutput.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
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
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.saol.engine;


import java.io.IOException;

/*
 *      Tritonus classes.
 *      Using these makes the program not portable to other
 *      Java Sound implementations.
 */
import  org.tritonus.share.TDebug;
import org.tritonus.share.sampled.TConversionTool;
import  org.tritonus.share.sampled.file.AudioOutputStream;



public class AudioOutputStreamOutput
extends Bus
implements SystemOutput
{
	private AudioOutputStream	m_audioOutputStream;
	private byte[]			m_abBuffer;



	public AudioOutputStreamOutput(AudioOutputStream audioOutputStream)
	{
		super(audioOutputStream.getFormat().getChannels());
		m_audioOutputStream = audioOutputStream;
		m_abBuffer = new byte[audioOutputStream.getFormat().getFrameSize()];
	}




	public void emit()
		throws IOException
	{
		float[]	afValues = getValues();
		boolean	bBigEndian = m_audioOutputStream.getFormat().isBigEndian();
		int	nOffset = 0;
		for (int i = 0; i < afValues.length; i++)
		{
			float	fOutput = Math.max(Math.min(afValues[i], 1.0F), -1.0F);
			// assumes 16 bit linear
			int	nOutput = (int) (fOutput * 32767.0F);
			TConversionTool.shortToBytes16((short) nOutput, m_abBuffer, nOffset, bBigEndian);
			nOffset += 2;
		}
		m_audioOutputStream.write(m_abBuffer, 0, m_abBuffer.length);
	}



	public void close()
		throws IOException
	{
		m_audioOutputStream.close();
	}
}



/*** AudioOutputStreamOutput.java ***/

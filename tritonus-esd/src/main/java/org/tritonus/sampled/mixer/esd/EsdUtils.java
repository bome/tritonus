/*
 *	EsdUtils.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer
 *
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

package org.tritonus.sampled.mixer.esd;

import javax.sound.sampled.AudioFormat;

import org.tritonus.lowlevel.esd.Esd;



public class EsdUtils
{
	public static int getEsdFormat(AudioFormat audioFormat)
	{
		int	nChannels = audioFormat.getChannels();
		AudioFormat.Encoding	encoding = audioFormat.getEncoding();
		int	nSampleSize = audioFormat.getSampleSizeInBits();
		int	nFormat = 0;

		if (nSampleSize == 8)
		{
			if (! encoding.equals(AudioFormat.Encoding.PCM_UNSIGNED))
			{
				throw new IllegalArgumentException("encoding must be PCM_UNSIGNED for 8 bit data");
			}
			nFormat |= Esd.ESD_BITS8;
		}
		else if (nSampleSize == 16)
		{
			if (! encoding.equals(AudioFormat.Encoding.PCM_SIGNED))
			{
				throw new IllegalArgumentException("encoding must be PCM_SIGNED for 16 bit data");
			}
			nFormat |= Esd.ESD_BITS16;
		}
		else
		{
			throw new IllegalArgumentException("only 8 bit and 16 bit samples are supported");
		}

		if (nChannels == 1)
		{
			nFormat |= Esd.ESD_MONO;
		}
		else if (nChannels == 2)
		{
			nFormat |= Esd.ESD_STEREO;
		}
		else
		{
			throw new IllegalArgumentException("only mono and stereo are supported");
		}

		return nFormat;
	}
}



/*** EsdUtils.java ***/

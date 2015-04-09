/*
 *	AlsaUtils.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

package org.tritonus.sampled.mixer.alsa;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import org.tritonus.share.sampled.AudioFormats;
import org.tritonus.lowlevel.alsa.AlsaPcm;



public class AlsaUtils
{
	private static AudioFormat[]	sm_aFormatTable = new AudioFormat[32];
	static
	{
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S8] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			8,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U8] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			8,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S16_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			16,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S16_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			16,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U16_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			16,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U16_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			16,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S24_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			24,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S24_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			24,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U24_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			24,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U24_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			24,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S32_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			32,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_S32_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_SIGNED,
			AudioSystem.NOT_SPECIFIED,
			32,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U32_LE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			32,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			false);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_U32_BE] = new AudioFormat(
			AudioFormat.Encoding.PCM_UNSIGNED,
			AudioSystem.NOT_SPECIFIED,
			32,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_MU_LAW] = new AudioFormat(
			AudioFormat.Encoding.ULAW,
			AudioSystem.NOT_SPECIFIED,
			8,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
		sm_aFormatTable[AlsaPcm.SND_PCM_FORMAT_A_LAW] = new AudioFormat(
			AudioFormat.Encoding.ALAW,
			AudioSystem.NOT_SPECIFIED,
			8,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			AudioSystem.NOT_SPECIFIED,
			true);
	}



	public static AudioFormat getAlsaFormat(int nAudioFormat)
	{
		return sm_aFormatTable[nAudioFormat];
	}



	public static int getAlsaFormat(AudioFormat audioFormat)
	{
		for (int nFormat = 0; nFormat < sm_aFormatTable.length; nFormat++)
		{
			if (sm_aFormatTable[nFormat] != null && AudioFormats.matches(sm_aFormatTable[nFormat], audioFormat))
			{
				return nFormat;
			}
		}
		return AlsaPcm.SND_PCM_FORMAT_UNKNOWN;
	}
}



/*** AlsaUtils.java ***/

/*
 *	AlsaPortMixerProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 - 2002 by Matthias Pfisterer
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

package org.tritonus.sampled.mixer.alsa;

import org.tritonus.share.TDebug;
import org.tritonus.lowlevel.alsa.Alsa;
import org.tritonus.lowlevel.alsa.AlsaCtl;
import org.tritonus.share.sampled.mixer.TMixerProvider;



public class AlsaPortMixerProvider
extends	TMixerProvider
{
	private static boolean		sm_bInitialized = false;



	public AlsaPortMixerProvider()
	{
		super();
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaPortMixerProvider.<init>(): begin"); }
		if (! sm_bInitialized && ! isDisabled())
		{
			if (! Alsa.isLibraryAvailable())
			{
				disable();
			}
			else
			{
				staticInit();
				sm_bInitialized = true;
			}
		}
		else
		{
			if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): already initialized or disabled"); }
		}
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaPortMixerProvider.<init>(): end"); }
	}



	protected void staticInit()
	{
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaPortMixerProvider.staticInit(): begin"); }
		int[]	anCards = AlsaCtl.getCards();
		if (TDebug.TraceMixerProvider) { System.out.println("AlsaPortMixerProvider.staticInit(): num cards: " + anCards.length); }
		for (int i = 0; i < anCards.length; i++)
		{
			AlsaPortMixer	mixer = new AlsaPortMixer(anCards[i]);
			addMixer(mixer);
		}
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaPortMixerProvider.staticInit(): end"); }
	}
}



/*** AlsaPortMixerProvider.java ***/

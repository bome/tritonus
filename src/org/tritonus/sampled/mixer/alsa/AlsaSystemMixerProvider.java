/*
 *	AlsaSystemMixerProvider.java
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	org.tritonus.share.TDebug;
import	org.tritonus.lowlevel.alsa.AlsaCtl;
import	org.tritonus.share.sampled.mixer.TMixerProvider;



public class AlsaSystemMixerProvider
	extends	TMixerProvider
{
	private static boolean		sm_bInitialized = false;



	public AlsaSystemMixerProvider()
	{
		super();
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaSystemMixerProvider.<init>(): begin"); }
		if (!sm_bInitialized)
		{
			staticInit();
			sm_bInitialized = true;
		}
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaSystemMixerProvider.<init>(): end"); }
	}



	protected void staticInit()
	{
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaSystemMixerProvider.staticInit(): begin"); }
		int[]	anCards = AlsaCtl.getCards();
		if (TDebug.TraceMixerProvider) { System.out.println("AlsaSystemMixerProvider.staticInit(): num cards: " + anCards.length); }
		for (int i = 0; i < anCards.length; i++)
		{
			AlsaSystemMixer	mixer = new AlsaSystemMixer(anCards[i]);
			addMixer(mixer);
		}
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaSystemMixerProvider.staticInit(): end"); }
	}
}



/*** AlsaSystemMixerProvider.java ***/

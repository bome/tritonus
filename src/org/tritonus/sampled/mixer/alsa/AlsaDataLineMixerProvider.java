/*
 *	AlsaDataLineMixerProvider.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.spi.MixerProvider;

import	org.tritonus.share.TDebug;
import	org.tritonus.lowlevel.alsa.Alsa;
import	org.tritonus.lowlevel.alsa.AlsaCtl;
import	org.tritonus.share.sampled.mixer.TMixerProvider;



public class AlsaDataLineMixerProvider
	extends	TMixerProvider
{
	private static boolean	sm_bInitialized = false;



	public AlsaDataLineMixerProvider()
	{
		super();
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): begin"); }
		if (! sm_bInitialized)
		{
			if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): initializing..."); }
			int[]	anCards = AlsaCtl.getCards();
			if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): num cards: " + anCards.length); }
			for (int i = 0; i < anCards.length; i++)
			{
				if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>():card #" + i + ": " + anCards[i]); }
				if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): creating Ctl object..."); }
				String	strPcmName = "hw:" + anCards[i];
				// String	strPcmName = AlsaDataLineMixer.getPcmName(anCards[i]);
				AlsaCtl	ctl = null;
				try
				{
					ctl = new AlsaCtl(strPcmName,
							  0 /* TODO: is this open mode or direction? */);
				}
				catch (Exception e)
				{
					if (TDebug.TraceMixerProvider || TDebug.TraceAllExceptions) { TDebug.out(e); }
					continue;
				}
				if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): calling getCardInfo()..."); }
				int[]	anValues = new int[2];
				String[]	astrValues = new String[6];
				ctl.getCardInfo(anValues, astrValues);
				if (TDebug.TraceMixerProvider)
				{
					TDebug.out("AlsaDataLineMixerProvider.<init>(): ALSA sound card:");
					TDebug.out("AlsaDataLineMixerProvider.<init>(): id: " + astrValues[0]);
					TDebug.out("AlsaDataLineMixerProvider.<init>(): abbreviation: " + astrValues[1]);
					TDebug.out("AlsaDataLineMixerProvider.<init>(): name: " + astrValues[2]);
					TDebug.out("AlsaDataLineMixerProvider.<init>(): long name: " + astrValues[3]);
					TDebug.out("AlsaDataLineMixerProvider.<init>(): mixer id: " + astrValues[4]);
					TDebug.out("AlsaDataLineMixerProvider.<init>(): mixer name: " + astrValues[5]);
				}
				int[]	anDevices = ctl.getPcmDevices();
				if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): num devices: " + anDevices.length); }
				// TODO: combine devices into one AlsaDataLineMixer?
				// pass device number to AlsaDataLineMixer constructor?
				for (int nDevice = 0; nDevice < anDevices.length; nDevice++)
				{
					if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): device #" + nDevice + ": " +  anDevices[nDevice]); }
				}
				// ctl.close();

				/*
				  We do not use strPcmName because the mixer may choose to open as 'plughw', while for ctl, the device name always has to be 'hw'.
				 */
				AlsaDataLineMixer	mixer = new AlsaDataLineMixer(anCards[i]);
				super.addMixer(mixer);
			}
			sm_bInitialized = true;
		}
		else
		{
			if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): already initialized"); }
		}


		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaDataLineMixerProvider.<init>(): end"); }
	}
}



/*** AlsaDataLineMixerProvider.java ***/

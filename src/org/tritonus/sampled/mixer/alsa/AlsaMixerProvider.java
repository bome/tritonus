/*
 *	AlsaMixerProvider.java
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



public class AlsaMixerProvider
	extends	TMixerProvider
{
	private static boolean	sm_bInitialized = false;



	public AlsaMixerProvider()
	{
		super();
		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaMixerProvider.<init>(): begin"); }
		if (! sm_bInitialized)
		{
			int[]	anCards = AlsaCtl.getCards();
			if (TDebug.TraceMixerProvider) { System.out.println("AlsaMixerProvider.<init>(): num cards: " + anCards.length); }
			for (int i = 0; i < anCards.length; i++)
			{
				if (TDebug.TraceMixerProvider)  { System.out.println("AlsaMixerProvider.<init>(): card #" + i + ": " +  anCards[i]); }
				if (TDebug.TraceMixerProvider) { System.out.println("AlsaMixerProvider.<init>(): creating Ctl object..."); }
				String	strPcmName = "hw:" + anCards[i];
				// String	strPcmName = AlsaMixer.getPcmName(anCards[i]);
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
				if (TDebug.TraceMixerProvider) { TDebug.out("AlsaMixerProvider.<init>(): calling getCardInfo()..."); }
				int[]	anValues = new int[2];
				String[]	astrValues = new String[6];
				ctl.getCardInfo(anValues, astrValues);
				if (TDebug.TraceMixerProvider)
				{
					TDebug.out("AlsaMixerProvider.<init>(): ALSA sound card:");
					TDebug.out("AlsaMixerProvider.<init>(): id: " + astrValues[0]);
					TDebug.out("AlsaMixerProvider.<init>(): abbreviation: " + astrValues[1]);
					TDebug.out("AlsaMixerProvider.<init>(): name: " + astrValues[2]);
					TDebug.out("AlsaMixerProvider.<init>(): long name: " + astrValues[3]);
					TDebug.out("AlsaMixerProvider.<init>(): mixer id: " + astrValues[4]);
					TDebug.out("AlsaMixerProvider.<init>(): mixer name: " + astrValues[5]);
				}
				int[]	anDevices = ctl.getPcmDevices();
				if (TDebug.TraceMixerProvider) { System.out.println("AlsaMixerProvider.<init>(): num devices: " + anDevices.length); }
				// TODO: compine devices into one AlsaMixer?
				// pass device number to AlsaMixer constructor?
				for (int nDevice = 0; nDevice < anDevices.length; nDevice++)
				{
					if (TDebug.TraceMixerProvider) { System.out.println("AlsaMixerProvider.<init>(): device #" + nDevice + ": " +  anDevices[nDevice]); }
				}
				// ctl.close();

				/*
				  We do not use strPcmName because the mixer may choose to open as 'plughw', while for ctl, the device name always has to be 'hw'.
				 */
				AlsaMixer	mixer = new AlsaMixer(anCards[i]);
				super.addMixer(mixer);
			}
			sm_bInitialized = true;
		}


		if (TDebug.TraceMixerProvider) { TDebug.out("AlsaMixerProvider.<init>(): end"); }
	}
}



/*** AlsaMixerProvider.java ***/

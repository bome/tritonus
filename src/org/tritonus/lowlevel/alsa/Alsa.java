/*
 *	Alsa.java
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.lowlevel.alsa;


import	org.tritonus.TDebug;


public class Alsa
{
	static
	{
		if (TDebug.TraceAlsaNative)
		{
			System.out.println("Alsa.<clinit>(): loading native library tritonusalsa");
		}
		System.loadLibrary("tritonusalsa");
	}



	public static final int	SND_CARDS = 8;


	public static native String getStringError(int nErrnum);
	public static native int loadCard(int nCard);
	public static native int getCards();
	public static native int getCardsMask();
	public static native int getCardByName(String strName);
	public static native String getCardName(int nCard);
	public static native String getCardLongName(int nCard);
	public static native int getDefaultCard();
	public static native int getDefaultMixerCard();
	public static native int getDefaultMixerDevice();
	public static native int getDefaultPcmCard();
	public static native int getDefaultPcmDevice();
	public static native int getDefaultRawmidiCard();
	public static native int getDefaultRawmidiDevice();
}



/*** Alsa.java ***/

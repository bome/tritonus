/*
 *	AlsaCtl.java

 for the following, see 'man ident'.

$Id$
$Log$
Revision 1.7  2001/07/02 18:41:44  pfisterer
reorganized native library loading and tracing

Revision 1.6  2001/05/30 09:25:03  pfisterer
intermediate development state, mainly related to ALSA

Revision 1.5  2001/05/11 08:53:45  pfisterer
rcs keywords test


 */

/*
 *  Copyright (c) 2000 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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


import	org.tritonus.share.TDebug;


public class AlsaCtl
{
	// contains a pointer to snd_ctl_t
	private long	m_lNativeHandle;



	static
	{
		Alsa.loadNativeLibrary();
		if (TDebug.TraceAlsaCtlNative)
		{
			setTrace(true);
		}
	}



	public static native int loadCard(int nCard);
	// this encapsulates snd_card_next()
	public static native int[] getCards();
	public static native int getCardIndex(String strName);
	public static native String getCardName(int nCard);
	public static native String getCardLongName(int nCard);

	public static native int getDefaultCard();
	public static native int getDefaultMixerCard();
	public static native int getDefaultPcmCard();
	public static native int getDefaultPcmDevice();
	public static native int getDefaultRawmidiCard();
	public static native int getDefaultRawmidiDevice();


	public AlsaCtl(String strName, int nMode)
		throws	Exception
	{
		if (open(strName, nMode) < 0)
		{
			throw new Exception();
		}
	}



	public AlsaCtl(int nCard)
		throws	Exception
	{
		this("hw:" + nCard, 0);	// not yet clear what the zero means
	}



	private native int open(String strName, int nMode);
	public native int close();

	/**
	 *	anValues[0]	card #
	 *	anValues[1]	card type
	 *
	 *	astrValues[0]	id
	 *	astrValues[1]	abbreviation
	 *	astrValues[2]	name
	 *	astrValues[3]	long name
	 *	astrValues[4]	mixer id
	 *	astrValues[5]	mixer name
	 */
	public native int getCardInfo(int[] anValues, String[] astrValues);


	public native int[] getPcmDevices();

	/**
	 *	anValues[0]	device (inout)
	 *	anValues[1]	subdevice (inout)
	 *	anValues[2]	stream (inout)
	 *	anValues[3]	card (out)
	 *	anValues[4]	class (out)
	 *	anValues[5]	subclass (out)
	 *	anValues[6]	subdevice count (out)
	 *	anValues[7]	subdevice available (out)
	 *
	 *	astrValues[0]	id (out)
	 *	astrValues[1]	name (out)
	 *	astrValues[2]	subdevice name (out)
	 */
	public native int getPcmInfo(int[] anValues, String[] astrValues);


	private static native void setTrace(boolean bTrace);
}



/*** AlsaCtl.java ***/

/*
 *	AlsaCtl.java
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


public class AlsaCtl
{
	private long	m_nativeHandle;



	static
	{
		if (TDebug.TraceAlsaNative)
		{
			System.out.println("AlsaCtl.<clinit>(): loading native library tritonusalsa");
		}
		System.loadLibrary("tritonusalsa");
	}



	public AlsaCtl(int nCard)
		throws	Exception
	{
		if (open(nCard) < 0)
		{
			throw new Exception();
		}
	}


	private native int open(int nCard);
	private native int close();

	/**
	 *	anValues[0]	card type
	 *	anValues[1]	hwdep devices
	 *	anValues[2]	pcm devices
	 *	anValues[3]	mixer devices
	 *	anValues[4]	raw midi devices
	 *	anValues[5]	timer devices
	 *
	 *	astrValues[0]	id
	 *	astrValues[1]	abbreviation
	 *	astrValues[2]	name
	 *	astrValues[3]	long name
	 */
	private native int getHWInfo(int[] anValues, String astrValues);


	/**
	 *	anValues[0]	type
	 *	anValues[1]	flags
	 *	anValues[2]	playback subdevices
	 *	anValues[3]	capture subdevices
	 *
	 *	astrValues[0]	id
	 *	astrValues[1]	name
	 */
	private native int getPcmInfo(int nDevice,
				      int[] anValues,
				      String astrValues);


	/**
	 *	anValues[0]	type
	 *	anValues[1]	flags
	 *	anValues[2]	playback subdevices
	 *	anValues[3]	capture subdevices
	 *
	 *	astrValues[0]	id
	 *	astrValues[1]	name
	 */
	private native int getPcmChannelInfo(int nDevice,
					     int nChannel,
					     int nSubDevice,
					     int[] anValues,
					     String astrValues);


}



/*** AlsaCtl.java ***/

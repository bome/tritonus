/*
 *	Alsa.java
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


/**	Common ALSA functions.
	Used only for the functions that do neither belong to the
	ctl sections nor to any specific interface section
	(like pcm, rawmidi, etc.).
	Currently, there is only one function remaining.
 */
public class Alsa
{
	static
	{
		Alsa.loadNativeLibrary();
	}



	public static void loadNativeLibrary()
	{
		if (TDebug.TraceAlsaNative)
		{
			TDebug.out("Alsa.loadNativeLibrary(): loading native library tritonusalsa");
		}
		System.loadLibrary("tritonusalsa");
		if (TDebug.TraceAlsaNative)
		{
			TDebug.out("Alsa.loadNativeLibrary(): loaded");
		}
	}



	public static native String getStringError(int nErrnum);
}



/*** Alsa.java ***/

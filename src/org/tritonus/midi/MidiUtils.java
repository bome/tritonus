/*
 *	MidiUtils.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.midi;


import	org.tritonus.TDebug;



public class MidiUtils
{
	public static int get14bitValue(int nLSB, int nMSB)
	{
		return (nLSB & 0x7F) | ((nMSB & 0x7F) << 7);
	}



	public static int get14bitMSB(int nValue)
	{
		return (nValue >> 7) & 0x7F;
	}



	public static int get14bitLSB(int nValue)
	{
		return nValue & 0x7F;
	}


}



/*** MidiUtils.java ***/

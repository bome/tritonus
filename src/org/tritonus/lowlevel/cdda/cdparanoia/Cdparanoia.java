/*
 *	Cdparanoia.java
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


package	org.tritonus.lowlevel.cdda.cdparanoia;


import	org.tritonus.share.TDebug;



/**	Reading audio CDs using libcdparanoia.
 */
public class Cdparanoia
{
	static
	{
		if (TDebug.TraceCdda) { TDebug.out("Cdparanoia.<clinit>(): loading native library tritonuscdparanoia"); }
		System.loadLibrary("tritonuscdparanoia");
		if (TDebug.TraceCdda) { TDebug.out("Cdparanoia.<clinit>(): loaded"); }
		setTrace(TDebug.TraceCddaNative);
	}



	/*
	 *	This holds a file descriptor for the native code -
	 *	do not touch!
	 */
	private long		m_lNativeHandle;



	// TODO: parameter strDevicename (or something else sensible)
	public Cdparanoia(String strDevice)
	{
		if (TDebug.TraceCdda) { System.out.println("Cdparanoia.<init>: begin"); }
		int	nResult = open(strDevice);
		if (nResult < 0)
		{
			throw new RuntimeException("cannot open " + strDevice);
		}
		if (TDebug.TraceCdda) { System.out.println("Cdparanoia.<init>: end"); }
	}



	/**	OUTDATED!
		Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	private native int open(String strDevice);

	/**	OUTDATED!
		Closes the sequencer.
	 *	Calls snd_seq_close().
	 */
	public native void close();


	/*
	 *	anValues[0]	first track
	 *	anValues[1]	last track
	 *
	 *	anStartTrack[x]	start sector of the track x.
	 *	anType[x]	type of track x.
	 */
	public native int readTOC(int[] anValues,
			   int[] anStartFrame,
			   int[] anLength,
			   int[] anType,
			   boolean[] abCopy,
			   boolean[] abPre,
			   int[] anChannels);



	public native int prepareTrack(int nTrack);


	/**	Reads one or more raw frames from the CD.
		This call reads <CODE>nCount</CODE> frames from
		the track that has been set by
		<CODE>prepareTrack()</CODE>.
		<CODE>abData</CODE>  has to be big enough to hold the
		amount of data requested (<CODE>2352 * nCount</CODE> bytes).
	 */
	public native int readNextFrame(int nCount, byte[] abData);

	private static native void setTrace(boolean bTrace);
}



/*** Cdparanoia.java ***/

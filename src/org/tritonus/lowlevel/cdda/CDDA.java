/*
 *	CDDA.java
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


package	org.tritonus.lowlevel.cdda;

import	javax.sound.midi.MidiEvent;

import	org.tritonus.share.TDebug;



/**	The lowest level of interface to the ALSA sequencer.
 */
public class CDDA
{
	static
	{
		if (TDebug.TraceCDDA)
		{
			TDebug.out("CDDA.<clinit>(): loading native library tritonuscdda");
		}
		System.loadLibrary("tritonuscdda");
		if (TDebug.TraceCDDA)
		{
			TDebug.out("CDDA.<clinit>(): loaded");
		}
		// TODO: ????
		setTrace(true /*TDebug.TraceCDDANative*/);
	}



	/*
	 *	This holds a file descriptor for the native code -
	 *	do not touch!
	 */
	private long		m_lNativeHandle;



	// TODO: parameter strDevicename (or something else sensible)
	public CDDA()
	{
		if (TDebug.TraceCDDA)
		{
			System.out.println("CDDA.<init>: begin");
		}
		int	nResult = open();
		if (nResult < 0)
		{
			throw new RuntimeException("cannot open /dev/cdrom");
		}
		if (TDebug.TraceCDDA)
		{
			System.out.println("CDDA.<init>: end");
		}
	}



	/**	Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	private native int open();

	/**	Closes the sequencer.
	 *	Calls snd_seq_close().
	 */
	public native void close();


	/*
	 *	anValues[0]	first track
	 *	anValues[1]	last track
	 */
	public native int readTOC(int[] anValues, int[] anStartTrack, int[] anType);

	/**	Reads one or more raw frames from the CD.
		This call reads <CODE>nCount</CODE> frames starting at
		lba position <CODE>nFrame</CODE>.
		<CODE>abData</CODE>  has to be big enough to hold the
		amount of data requested (<CODE>2352 * nCount</CODE> bytes).
	 */
	public native int readFrame(int nFrame, int nCount, byte[] abData);

	private static native void setTrace(boolean bTrace);
}



/*** CDDA.java ***/

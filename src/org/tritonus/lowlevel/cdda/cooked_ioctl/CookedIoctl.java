/*
 *	CookedIoctl.java
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


package	org.tritonus.lowlevel.cdda.cooked_ioctl;


import	org.tritonus.lowlevel.cdda.CDDA;
import	org.tritonus.share.TDebug;



/**	Low-level interface to reading CDs
 */
public class CookedIoctl
	implements	CDDA
{
	/**	Size of a cdda frame in bytes.
	 */
	public static final int		FRAME_SIZE = 2352;


	static
	{
		if (TDebug.TraceCDDA) { TDebug.out("CookedIoctl.<clinit>(): loading native library tritonuscdda"); }
		System.loadLibrary("tritonuscdda");
		if (TDebug.TraceCDDA) { TDebug.out("CookedIoctl.<clinit>(): loaded"); }
		// TODO: ????
		setTrace(true /*TDebug.TraceCDDANative*/);
	}



	/*
	 *	This holds a file descriptor for the native code -
	 *	do not touch!
	 */
	private long		m_lNativeHandle;



	// TODO: parameter strDevicename (or something else sensible)
	public CookedIoctl()
	{
		if (TDebug.TraceCDDA) { System.out.println("CookedIoctl.<init>: begin"); }
		int	nResult = open();
		if (nResult < 0)
		{
			throw new RuntimeException("cannot open /dev/cdrom");
		}
		if (TDebug.TraceCDDA) { System.out.println("CookedIoctl.<init>: end"); }
	}



	/**	OUTDATED!
		Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	private native int open();

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



/*** CookedIoctl.java ***/

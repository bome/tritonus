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


//import	org.tritonus.share.TDebug;



/**	Mid-level interface definition for reading CDs
 */
public interface CDDA
{
	/**	Size of a cdda frame in bytes.
	 */
	public static final int		FRAME_SIZE = 2352;




	/**	OUTDATED!
		Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	// to become a public method?
	// private native int open();

	/**	OUTDATED!
		Closes the sequencer.
	 *	Calls snd_seq_close().
	 */
	public void close();


	/*
	 *	anValues[0]	first track
	 *	anValues[1]	last track
	 *
	 *	anStartTrack[x]	start sector of the track x.
	 *	anType[x]	type of track x.
	 */
	// TODO: other information (bits, channels)
	public int readTOC(int[] anValues, int[] anStartTrack, int[] anType);


	/**	Reads one or more raw frames from the CD.
		This call reads <CODE>nCount</CODE> frames starting at
		lba position <CODE>nFrame</CODE>.
		<CODE>abData</CODE>  has to be big enough to hold the
		amount of data requested (<CODE>2352 * nCount</CODE> bytes).
	 */
	public int readFrame(int nFrame, int nCount, byte[] abData);
}



/*** CDDA.java ***/

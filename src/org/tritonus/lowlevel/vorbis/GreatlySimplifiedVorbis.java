/*
 *	GreatlySimplifiedVorbis.java
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer <Matthias.Pfisterer@web.de>
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
 */

package	org.tritonus.lowlevel.vorbis;


import	org.tritonus.share.TDebug;



/**	Encoding to ogg/vorbis.
	This uses an "API an simple as possible" approach.
	@author Matthias Pfisterer
 */
public class GreatlySimplifiedVorbis
{
	static
	{
		if (TDebug.TraceCdda) { TDebug.out("GreatlySimplifiedVorbis.<clinit>(): loading native library tritonusgsvorbis"); }
		System.loadLibrary("tritonusgsvorbis");
		if (TDebug.TraceCdda) { TDebug.out("GreatlySimplifiedVorbis.<clinit>(): loaded"); }
		setTrace(TDebug.TraceCddaNative);
	}



	/*
	 *	This holds a file descriptor for the native code -
	 *	do not touch!
	 */
	private long		m_lNativeHandle;



	public GreatlySimplifiedVorbis(int nSampleRate, int nChannels)
	{
		// TODO: change debugging flag
		if (TDebug.TraceCdda) { System.out.println("GreatlySimplifiedVorbis.<init>: begin"); }
		int	nResult = open(nSampleRate, nChannels);
		if (nResult < 0)
		{
			throw new RuntimeException("cannot open");
		}
		if (TDebug.TraceCdda) { System.out.println("GreatlySimplifiedVorbis.<init>: end"); }
	}



	/**	OUTDATED!
		Opens the sequencer.
	 *	Calls snd_seq_open() and snd_seq_client_id(). Returns the
	 *	client id.
	 */
	private native int open(int nSampleRate, int nChannels);

	/**	OUTDATED!
		Closes the sequencer.
	 *	Calls snd_seq_close().
	 */
	public native void close();



	public native int encode(byte[] abPcm, byte[] abVorbis);



	private static native void setTrace(boolean bTrace);
}



/*** GreatlySimplifiedVorbis.java ***/

/*
 *	StreamState.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2005 by Matthias Pfisterer
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.lowlevel.pogg;

import org.tritonus.share.TDebug;


/** Wrapper for ogg_stream_state.
 */
public class StreamState
{
        static
        {
                Ogg.loadNativeLibrary();
                if (TDebug.TraceOggNative)
                {
                        setTrace(true);
                }
        }


	/**
	 *	Holds the pointer to ogg_stream_state
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	private long	m_lNativeHandle;

	/** The serial number of the stream.
		This is set by init().
	 */
	private int		m_nSerialNo;



	public StreamState()
	{
		if (TDebug.TraceOggNative) { TDebug.out("StreamState.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of ogg_stream_state failed");
		}
		if (TDebug.TraceOggNative) { TDebug.out("StreamState.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();



	/** Calls ogg_stream_init().
	 */
	public int init(int nSerialNo)
	{
		m_nSerialNo = nSerialNo;
		return init_native(nSerialNo);
	}

	/** Calls ogg_stream_init().
	 */
	private native int init_native(int nSerialNo);


	/** Calls ogg_stream_clear().
	 */
	public int clear()
	{
		return clear_native();
	}

	/** Calls ogg_stream_clear().
	 */
	private native int clear_native();


	/** Calls ogg_stream_reset().
	 */
	public int reset()
	{
		return reset_native();
	}

	/** Calls ogg_stream_reset().
	 */
	private native int reset_native();


	/** Calls ogg_stream_destroy().
	 */
	public int destroy()
	{
		return destroy_native();
	}

	/** Calls ogg_stream_destroy().
	 */
	private native int destroy_native();


	/** Calls ogg_stream_eos().
	 */
	public boolean isEOSReached()
	{
		return isEOSReached_native();
	}

	/** Calls ogg_stream_eos().
	 */
	private native boolean isEOSReached_native();



	/** Calls ogg_stream_packetin().
	 */
	public int packetIn(Packet packet)
	{
		return packetIn_native(packet);
	}


	/** Calls ogg_stream_packetin().
	 */
	private native int packetIn_native(Packet packet);


	/** Calls ogg_stream_pageout().
	 */
	public int pageOut(Page page)
	{
		return pageOut_native(page);
	}


	/** Calls ogg_stream_pageout().
	 */
	private native int pageOut_native(Page page);


	/** Calls ogg_stream_flush().
	 */
	public int flush(Page page)
	{
		return flush_native(page);
	}


	/** Calls ogg_stream_flush().
	 */
	private native int flush_native(Page page);


	/** Calls ogg_stream_pagein().
	 */
	public int pageIn(Page page)
	{
		return pageIn_native(page);
	}


	/** Calls ogg_stream_pagein().
	 */
	private native int pageIn_native(Page page);


	/** Calls ogg_stream_packetout().
	 */
	public int packetOut(Packet packet)
	{
		return packetOut_native(packet);
	}


	/** Calls ogg_stream_packetout().
	 */
	private native int packetOut_native(Packet packet);


	/** Calls ogg_stream_packetpeek().
	 */
	public int packetPeek(Packet packet)
	{
		return packetPeek_native(packet);
	}


	/** Calls ogg_stream_packetpeek().
	 */
	private native int packetPeek_native(Packet packet);


	private static native void setTrace(boolean bTrace);
}





/*** StreamState.java ***/

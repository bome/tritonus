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

	private static final int	INITIAL_BODY_DATA_SIZE = 16 * 1024;
	private static final int	INITIAL_LACING_VALUES_SIZE = 1024;

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

	/** Storage for packet bodies.
	 */
	private byte[]	m_abBodyData;

	/** Number of bytes used in te body storage.
	 */
	private int		m_nBodyFill;

	/** Number of bytes aready returned (as pages) from the body storage.
	 */
	private int		m_nBodyReturned;

	/** Lacing values.
	 */
	private int[]	m_anLacingValues;

	/** Granule values.
	 */
	private long[]	m_alGranuleValues;

	private int		m_nLacingFill;
	private int		m_nLacingPacket;
	private int		m_nLacingReturned;

	private byte[]	m_abHeader;

	private int		m_nHeaderFill;

	private boolean	m_bBos;
	private boolean m_bEos;
	private int		m_nPageNo;
	private long	m_lPacketNo;
	private long	m_lGranulePos;



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
		m_abBodyData = new byte[INITIAL_BODY_DATA_SIZE];
		m_nBodyFill = 0;
		m_nBodyReturned = 0;
		m_anLacingValues = new int[INITIAL_LACING_VALUES_SIZE];
		m_alGranuleValues = new long[INITIAL_LACING_VALUES_SIZE];
		m_nLacingFill = 0;
		m_nLacingPacket = 0;
		m_nLacingReturned = 0;

		m_abHeader = new byte[282];
		m_nHeaderFill = 0;

		m_bBos = false;
		m_bEos = false;
		m_nPageNo = 0;
		m_lPacketNo = 0;
		m_lGranulePos = 0;

		// TODO: necessary?
		for(int i = 0; i < m_abBodyData.length; i++)
			m_abBodyData[i] = 0;
		for(int i = 0; i < m_anLacingValues.length; i++)
			m_anLacingValues[i] = 0;
		for(int i = 0; i < m_alGranuleValues.length; i++)
			m_alGranuleValues[i] = 0;

		// TODO: remove return value
		// return 0;
		return init_native(nSerialNo);
	}

	/** Calls ogg_stream_init().
	 */
	private native int init_native(int nSerialNo);


	/** Calls ogg_stream_clear().
	 */
	public int clear()
	{
		m_nSerialNo = 0;
		m_abBodyData = null;
		m_nBodyFill = 0;
		m_nBodyReturned = 0;
		m_anLacingValues = null;
		m_alGranuleValues = null;
		m_nLacingFill = 0;
		m_nLacingPacket = 0;
		m_nLacingReturned = 0;

		m_abHeader = null;
		m_nHeaderFill = 0;

		m_bBos = false;
		m_bEos = false;
		m_nPageNo = 0;
		m_lPacketNo = 0;
		m_lGranulePos = 0;

		// TODO: remove return value
		// return 0;
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
	// TODO: remove calls to this method
	public int destroy()
	{
		clear();
		return destroy_native();
	}

	/** Calls ogg_stream_destroy().
	 */
	private native int destroy_native();


	/** Calls ogg_stream_eos().
	 */
	public boolean isEOSReached()
	{
		// return m_bEos;
		return isEOSReached_native();
	}

	/** Calls ogg_stream_eos().
	 */
	private native boolean isEOSReached_native();



	/** Calls ogg_stream_packetin().
	 */
	/* submit data to the internal buffer of the framing engine */
	public int packetIn(Packet packet)
	{
		int i;
		byte[] abPacketData = packet.getData();
		int lacing_vals = abPacketData.length / 255 + 1;

		if (m_nBodyReturned > 0)
		{
			/* advance packet data according to the body_returned pointer. We
			   had to keep it around to return a pointer into the buffer last
			   call */
			m_nBodyFill -= m_nBodyReturned;
			if (m_nBodyFill > 0)
			{
				System.arraycopy(m_abBodyData, m_nBodyReturned,
								 m_abBodyData, 0, m_nBodyFill);
			}
			m_nBodyReturned = 0;
		}

		/* make sure we have the buffer storage */
		assureBodyDataCapacity(abPacketData.length);
		assureLacingValuesCapacity(lacing_vals);

		/* Copy in the submitted packet.  Yes, the copy is a waste;
		   this is the liability of overly clean abstraction for the
		   time being.  It will actually be fairly easy to eliminate
		   the extra copy in the future */
		System.arraycopy(abPacketData, 0, m_abBodyData, m_nBodyFill,
						 abPacketData.length);
		m_nBodyFill += abPacketData.length;

		/* Store lacing vals for this packet */
		for (i = 0; i < lacing_vals - 1; i++)
		{
			m_anLacingValues[m_nLacingFill + i] = 255;
			m_alGranuleValues[m_nLacingFill + i] = m_lGranulePos;
		}
		m_anLacingValues[m_nLacingFill + i] = abPacketData.length % 255;
		m_alGranuleValues[m_nLacingFill + i] = packet.getGranulePos();
		m_lGranulePos = packet.getGranulePos();

		/* flag the first segment as the beginning of the packet */
		m_anLacingValues[m_nLacingFill] |= 0x100;

		m_nLacingFill += lacing_vals;

		/* for the sake of completeness */
		m_lPacketNo++;

		if(packet.isEos())
			m_bEos = true;
		return 0;
	}



	/** Calls ogg_stream_pageout().
	 */
/* This constructs pages from buffered packet segments.  The pointers
returned are to static buffers; do not free. The returned buffers are
good only until the next call (using the same ogg_stream_state) */
	public int pageOut(Page page)
	{
		if ((m_bEos && m_nLacingFill > 0) ||       /* 'were done, now flush' */
			m_nBodyFill - m_nBodyReturned > 4096 || /* 'page nominal size' */
			m_nLacingFill >= 255 ||                 /* 'segment table full' */
			(m_nLacingFill > 0 && ! m_bBos))        /* 'initial header page' */
		{
			return flush(page);
		}
		/* not enough data to construct a page and not end of stream */
		return 0;
	}



	/** Calls ogg_stream_flush().
	 */
/* This will flush remaining packets into a page (returning nonzero),
   even if there is not enough data to trigger a flush normally
   (undersized page). If there are no packets or partial packets to
   flush, ogg_stream_flush returns 0.  Note that ogg_stream_flush will
   try to flush a normal sized page like ogg_stream_pageout; a call to
   ogg_stream_flush does not guarantee that all packets have flushed.
   Only a return value of 0 from ogg_stream_flush indicates all packet
   data is flushed into pages.

   since ogg_stream_flush will flush the last page in a stream even if
   it's undersized, you almost certainly want to use ogg_stream_pageout
   (and *not* ogg_stream_flush) unless you specifically need to flush 
   an page regardless of size in the middle of a stream.
*/
	public int flush(Page page)
	{
		int i;
		int vals = 0;
		int maxvals = Math.min(m_nLacingFill, 255);
		int bytes = 0;
		int acc = 0;
		long granule_pos = m_alGranuleValues[0];

		if (maxvals == 0)
		{
			return 0;
		}

		/* construct a page */
		/* decide how many segments to include */

		/* If this is the initial header case, the first page must
		   only include the initial header packet */
		if (! m_bBos)
		{  /* 'initial header page' case */
			granule_pos = 0;
			for (vals = 0; vals < maxvals; vals++)
			{
				if ((m_anLacingValues[vals] & 0x0FF) < 255)
				{
					vals++;
					break;
				}
			}
		}
		else
		{
			for (vals = 0; vals < maxvals; vals++)
			{
				if (acc > 4096)
					break;
				acc += (m_anLacingValues[vals] & 0x0FF);
				granule_pos = m_alGranuleValues[vals];
			}
		}
  
		/* construct the header in temp storage */
		m_abHeader[0] = (byte) 'O';
		m_abHeader[1] = (byte) 'g';
		m_abHeader[2] = (byte) 'g';
		m_abHeader[3] = (byte) 'S';
  
		/* stream structure version */
		m_abHeader[4] = 0;
  
		m_abHeader[5] = 0x00;
		/* continued packet flag? */
		if ((m_anLacingValues[0] & 0x100) == 0)
			m_abHeader[5] |= 0x01;
		/* first page flag? */
		if (! m_bBos)
			m_abHeader[5] |= 0x02;
		/* last page flag? */
		if (m_bEos && m_nLacingFill == vals)
			m_abHeader[5] |= 0x04;
		m_bBos = true;

		/* 64 bits of PCM position */
		for (i = 6; i < 14; i++)
		{
			m_abHeader[i] = (byte) (granule_pos & 0xFF);
			granule_pos >>>= 8;
		}

		/* 32 bits of stream serial number */
		int serialno = m_nSerialNo;
		for (i = 14; i < 18; i++)
		{
			m_abHeader[i] = (byte) (serialno & 0xFF);
			serialno >>>= 8;
		}

		/* 32 bits of page counter (we have both counter and page header
		   because this val can roll over) */
		if (m_nPageNo == -1)
		{
			m_nPageNo = 0;	/* because someone called
							   stream_reset; this would be a
							   strange thing to do in an
							   encode stream, but it has
							   plausible uses */
		}
		int pageno = m_nPageNo++;
		for (i = 18; i < 22; i++)
		{
			m_abHeader[i] = (byte) (pageno & 0xFF);
			pageno >>>= 8;
		}

		/* zero for computation; filled in later */
		m_abHeader[22] = 0;
		m_abHeader[23] = 0;
		m_abHeader[24] = 0;
		m_abHeader[25] = 0;
  
		/* segment table */
		m_abHeader[26] = (byte) (vals & 0xFF);
		for (i = 0; i < vals; i++)
		{
			m_abHeader[i + 27] = (byte) (m_anLacingValues[i] & 0xFF);
			bytes += (m_anLacingValues[i] & 0xFF);
		}

		/* set pointers in the ogg_page struct */
		page.setData(m_abHeader, 0, vals + 27,
				   m_abBodyData, m_nBodyReturned, bytes);
		m_nHeaderFill = vals + 27;

		/* advance the lacing data and set the body_returned pointer */

		m_nLacingFill -= vals;
		System.arraycopy(m_anLacingValues, vals, m_anLacingValues, 0,
						 m_nLacingFill);
		System.arraycopy(m_alGranuleValues, vals, m_alGranuleValues, 0,
						 m_nLacingFill);
		m_nBodyReturned += bytes;
  
		/* calculate the checksum */
  
		page.setChecksum();

		/* done */
		return 1;
	}



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


	private void assureBodyDataCapacity(int needed)
	{
		if (m_abBodyData.length <= m_nBodyFill + needed)
		{
			int nNewSize = m_abBodyData.length + needed + 1024;
			byte[] abNewBodyData = new byte[nNewSize];
			System.arraycopy(m_abBodyData, 0, abNewBodyData, 0,
							 m_abBodyData.length);
			m_abBodyData = abNewBodyData;
		}
	}



	private void assureLacingValuesCapacity(int needed)
	{
		if (m_anLacingValues.length <= m_nLacingFill + needed)
		{
			int nNewSize = m_anLacingValues.length + needed + 32;
			int[] anNewLacingValues = new int[nNewSize];
			System.arraycopy(m_anLacingValues, 0, anNewLacingValues, 0,
							 m_anLacingValues.length);
			m_anLacingValues = anNewLacingValues;
			long[] alNewGranuleValues = new long[nNewSize];
			System.arraycopy(m_alGranuleValues, 0, alNewGranuleValues, 0,
							 m_alGranuleValues.length);
			m_alGranuleValues = alNewGranuleValues;
		}
	}



	private static native void setTrace(boolean bTrace);
}





/*** StreamState.java ***/

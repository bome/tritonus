/*
 *	Info.java
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

package org.tritonus.lowlevel.pvorbis;

import org.tritonus.lowlevel.pogg.Ogg;
import org.tritonus.lowlevel.pogg.Packet;
import org.tritonus.lowlevel.pogg.Buffer;
import org.tritonus.share.TDebug;


/** Wrapper for vorbis_info.
 */
public class Info
implements VorbisConstants
{
        static
        {
                Ogg.loadNativeLibrary();
                if (TDebug.TraceVorbisNative)
                {
                        setTrace(true);
                }
        }



	/**
	 *	Holds the pointer to vorbis_info
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	private long	m_lNativeHandle;



	public Info()
	{
		if (TDebug.TraceVorbisNative) { TDebug.out("Info.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of vorbis_info failed");
		}
		if (TDebug.TraceVorbisNative) { TDebug.out("Info.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Calls vorbis_info_init().
	 */
	public void init()
	{
		init_native();
	}


	/** Calls vorbis_info_init().
	 */
	public native void init_native();


	/** Calls vorbis_info_clear().
	 */
	public void clear()
	{
		clear_native();
	}


	/** Calls vorbis_info_clear().
	 */
	public native void clear_native();


// blocksize?

	/** Accesses channels.
	 */
	public int getChannels()
	{
		return getChannels_native();
	}


	/** Accesses channels.
	 */
	public native int getChannels_native();



	/** Accesses rate.
	 */
	public int getRate()
	{
		return getRate_native();
	}


	/** Accesses rate.
	 */
	public native int getRate_native();




	/** Calls vorbis_encode_init().
	 */
	public int encodeInit(
		int nChannels,
		int nRate,
		int nMaxBitrate,
		int nNominalBitrate,
		int nMinBitrate)
	{
		return encodeInit_native(
			nChannels,
			nRate,
			nMaxBitrate,
			nNominalBitrate,
			nMinBitrate);
	}


	/** Calls vorbis_encode_init().
	 */
	public native int encodeInit_native(
		int nChannels,
		int nRate,
		int nMaxBitrate,
		int nNominalBitrate,
		int nMinBitrate);



	/** Calls vorbis_encode_init_vbr().
	 */
	public int encodeInitVBR(
		int nChannels,
		int nRate,
		float fQuality)
	{
		return encodeInitVBR_native(
			nChannels,
			nRate,
			fQuality);
	}


	/** Calls vorbis_encode_init_vbr().
	 */
	public native int encodeInitVBR_native(
		int nChannels,
		int nRate,
		float fQuality);


	/** Calls vorbis_synthesis_headerin().
	 */
	public int headerIn(Comment comment, Packet packet)
	{
		if(packet == null)
		{
			return OV_EBADHEADER;
		}
		Buffer buffer = new Buffer();
		byte[] abData = packet.getData();
		//Buffer.outBuffer(abData);
		buffer.readInit(abData, abData.length);

		/* Which of the three types of header is this? */
		/* Also verify header-ness, vorbis */
		int packtype = buffer.read(8);
		//TDebug.out("packtype: " + packtype);
		String s = buffer.readString(6);
		if(! "vorbis".equals(s))
		{
			/* not a vorbis header */
			buffer.free();
			return OV_ENOTVORBIS;
		}
		int r;
		switch (packtype)
		{
		case 0x01: /* least significant *bit* is read first */
			if(! packet.isBos())
			{
				/* Not the initial packet */
				buffer.free();
				return OV_EBADHEADER;
			}
			if (getRate() != 0)
			{
				/* previously initialized info header */
				buffer.free();
				return OV_EBADHEADER;
			}
			r = headerIn_native(buffer, packtype, packet);
			buffer.free();
			return r;
			//return(_vorbis_unpack_info(vi,&buffer));

		case 0x03: /* least significant *bit* is read first */
			if (getRate() == 0)
			{
				/* um... we didn't get the initial header */
				buffer.free();
				return OV_EBADHEADER;
			}
			r = comment.unpack(buffer);
			buffer.free();
			return r;
			//return(_vorbis_unpack_comment(vc,&buffer));

		case 0x05: /* least significant *bit* is read first */
			if (getRate() == 0 || comment.getVendor() == null)
			{
				/* um... we didn;t get the initial header or comments yet */
				buffer.free();
				return OV_EBADHEADER;
			}
			r = headerIn_native(buffer, packtype, packet);
			buffer.free();
			return r;
			//return(_vorbis_unpack_books(vi,&buffer));

		default:
			/* Not a valid vorbis header type */
			buffer.free();
			return OV_EBADHEADER;
		}
	}


	/** Calls vorbis_synthesis_headerin().
	 */
	public native int headerIn_native(Buffer buffer, int nPacketType,
									  Packet packet);


	private static native void setTrace(boolean bTrace);
}





/*** Info.java ***/

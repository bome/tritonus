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
import org.tritonus.share.TDebug;


/** Wrapper for vorbis_info.
 */
public class Info
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
		return headerIn_native(comment, packet);
	}


	/** Calls vorbis_synthesis_headerin().
	 */
	public native int headerIn_native(Comment comment, Packet packet);


	private static native void setTrace(boolean bTrace);
}





/*** Info.java ***/

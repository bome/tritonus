/*
 *	Info.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2001 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.lowlevel.vorbis;

import org.tritonus.lowlevel.ogg.Ogg;
import org.tritonus.lowlevel.ogg.Packet;
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
	@SuppressWarnings("unused")
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
	public native void init();


	/** Calls vorbis_info_clear().
	 */
	public native void clear();


// blocksize?

	/** Accesses channels.
	 */
	public native int getChannels();



	/** Accesses rate.
	 */
	public native int getRate();




	/** Calls vorbis_encode_init().
	 */
	public native int encodeInit(
		int nChannels,
		int nRate,
		int nMaxBitrate,
		int nNominalBitrate,
		int nMinBitrate);



	/** Calls vorbis_encode_init_vbr().
	 */
	public native int encodeInitVBR(
		int nChannels,
		int nRate,
		float fQuality);


	/** Calls vorbis_synthesis_headerin().
	 */
	public native int headerIn(Comment comment, Packet packet);


	private static native void setTrace(boolean bTrace);
}





/*** Info.java ***/

/*
 *	Packet.java
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

package org.tritonus.lowlevel.ogg;

import org.tritonus.share.TDebug;



/** Wrapper for ogg_packet.
 */
public class Packet
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
	 *	Holds the pointer to ogg_packet
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public Packet()
	{
		if (TDebug.TraceOggNative) { TDebug.out("Packet.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of ogg_packet failed");
		}
		if (TDebug.TraceOggNative) { TDebug.out("Packet.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();



	/** Calls ogg_packet_clear().
	 */
	public native void clear();



	/** Accesses packet and bytes.
	 */
	public native byte[] getData();


	/** Accesses b_o_s.
	 */
	public native boolean isBos();


	/** Accesses e_o_s.
	 */
	public native boolean isEos();


	private static native void setTrace(boolean bTrace);
}





/*** Packet.java ***/

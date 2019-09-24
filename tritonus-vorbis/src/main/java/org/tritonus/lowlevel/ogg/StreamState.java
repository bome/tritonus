/*
 *	StreamState.java
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
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



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
	public native int init(int nSerialNo);

	/** Calls ogg_stream_clear().
	 */
	public native int clear();

	/** Calls ogg_stream_reset().
	 */
	public native int reset();

	/** Calls ogg_stream_destroy().
	 */
	public native int destroy();

	/** Calls ogg_stream_eos().
	 */
	public native boolean isEOSReached();



	/** Calls ogg_stream_packetin().
	 */
	public native int packetIn(Packet packet);


	/** Calls ogg_stream_pageout().
	 */
	public native int pageOut(Page page);


	/** Calls ogg_stream_flush().
	 */
	public native int flush(Page page);


	/** Calls ogg_stream_pagein().
	 */
	public native int pageIn(Page page);


	/** Calls ogg_stream_packetout().
	 */
	public native int packetOut(Packet packet);


	/** Calls ogg_stream_packetpeek().
	 */
	public native int packetPeek(Packet packet);


	private static native void setTrace(boolean bTrace);
}





/*** StreamState.java ***/

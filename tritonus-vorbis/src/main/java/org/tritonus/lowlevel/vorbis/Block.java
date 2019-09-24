/*
 *	Block.java
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


/** Wrapper for vorbis_block.
 */
public class Block
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
	 *	Holds the pointer to vorbis_block
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public Block()
	{
		if (TDebug.TraceVorbisNative) { TDebug.out("Block.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of vorbis_block failed");
		}
		if (TDebug.TraceVorbisNative) { TDebug.out("Block.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Calls vorbis_block_init().
	 */
	public native int init(DspState dspState);


	/** Calls vorbis_bitrate_addblock().
	 */
	public native int addBlock();


	/** Calls vorbis_analysis().
	 */
	public native int analysis(Packet packet);


	/** Calls vorbis_synthesis().
	 */
	public native int synthesis(Packet packet);


	/** Calls vorbis_block_clear().
	 */
	public native int clear();

	private static native void setTrace(boolean bTrace);
}





/*** Block.java ***/

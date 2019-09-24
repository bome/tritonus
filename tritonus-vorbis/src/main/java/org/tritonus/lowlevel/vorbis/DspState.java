/*
 *	DspState.java
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


/** Wrapper for vorbis_dsp_state.
 */
public class DspState
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
	 *	Holds the pointer to vorbis_dsp_state
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public DspState()
	{
		if (TDebug.TraceVorbisNative) { TDebug.out("DspState.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of vorbis_dsp_state failed");
		}
		if (TDebug.TraceVorbisNative) { TDebug.out("DspState.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Initialize for encoding.
	    Calls vorbis_analysis_init().
	 */
	public native int initAnalysis(Info info);


	/** Calls vorbis_analysis_headerout().
	 */
	public native int headerOut(
		Comment comment,
		Packet packet,
		Packet commentPacket,
		Packet codePacket);


	/** Calls vorbis_analysis_buffer() and
	    vorbis_analysis_wrote().
	 */
	public native int write(float[][] values, int nValues);


	/** Calls vorbis_analysis_blockout().
	 */
	public native int blockOut(Block block);


	/** Calls vorbis_bitrate_flushpacket().
	 */
	public native int flushPacket(Packet packet);


	/** Initialize for decoding.
	    Calls vorbis_synthesis_init().
	 */
	public native int initSynthesis(Info info);


	/** Calls vorbis_synthesis_blockin().
	 */
	public native int blockIn(Block block);


	/** Calls vorbis_synthesis_pcmout().
	 */
	public native int pcmOut(float[][] afPcm);


	/** Calls vorbis_synthesis_read().
	 */
	public native int read(int nSamples);


	/** Accesses sequence.
	 */
	public native long getSequence();


	/** Calls vorbis_dsp_clear().
	 */
	public native void clear();


	private static native void setTrace(boolean bTrace);
}





/*** DspState.java ***/

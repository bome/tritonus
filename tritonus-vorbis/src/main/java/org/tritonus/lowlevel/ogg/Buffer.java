/*
 *	Buffer.java
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


/** Wrapper for oggpack_buffer.
 */
public class Buffer
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
	 *	Holds the pointer to oggpack_buffer
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public Buffer()
	{
		if (TDebug.TraceOggNative) { TDebug.out("Buffer.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of ogg_page failed");
		}
		if (TDebug.TraceOggNative) { TDebug.out("Buffer.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Calls oggpack_writeinit().
	 */
	public native void writeInit();


	/** Calls oggpack_writetrunc().
	 */
	public native void writeTrunc(int nBits);


	/** Calls oggpack_writealign().
	 */
	public native void writeAlign();


	/** Calls oggpack_writecopy().
	 */
	public native void writeCopy(byte[] abSource, int nBits);


	/** Calls oggpack_reset().
	 */
	public native void reset();


	/** Calls oggpack_writeclear().
	 */
	public native void writeClear();


	/** Calls oggpack_readinit().
	 */
	public native void readInit(byte[] abBuffer, int nBytes);


	/** Calls oggpack_write().
	 */
	public native void write(int nValue, int nBits);


	/** Calls oggpack_look().
	 */
	public native int look(int nBits);


	/** Calls oggpack_look1().
	 */
	public native int look1();


	/** Calls oggpack_adv().
	 */
	public native void adv(int nBits);


	/** Calls oggpack_adv1().
	 */
	public native void adv1();


	/** Calls oggpack_read().
	 */
	public native int read(int nBits);


	/** Calls oggpack_read1().
	 */
	public native int read1();


	/** Calls oggpack_bytes().
	 */
	public native int bytes();


	/** Calls oggpack_bits().
	 */
	public native int bits();


	/** Calls oggpack_get_buffer().
	 */
	public native byte[] getBuffer();


	private static native void setTrace(boolean bTrace);
}



/*** Buffer.java ***/

/*
 *	Buffer.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2005 by Matthias Pfisterer
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

package org.tritonus.lowlevel.pogg;

import java.io.UnsupportedEncodingException;

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


	/** Writes a string as UTF-8.
		Note: no length coding and no end byte are written,
		just the pure string!
	 */
	public void write(String str)
	{
		write(str, false);
	}


	/** Writes a string as UTF-8, including a length coding.
		In front of the string, the length in bytes is written
		as a 32 bit integer. No end byte is written.
	 */
	public void writeWithLength(String str)
	{
		write(str, true);
	}


	/** Writes a string as UTF-8, with or without a length coding.
		If a length coding is requested, the length in (UTF8-)bytes is written
		as a 32 bit integer in front of the string.
		No end byte is written.
	 */
	private void write(String str, boolean bWithLength)
	{
		byte[] aBytes = null;
		try
		{
			aBytes = str.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			if (TDebug.TraceAllExceptions) TDebug.out(e);
		}
		if (bWithLength)
		{
			write(aBytes.length, 32);
		}
		for (int i = 0; i < aBytes.length; i++)
		{
			write(aBytes[i], 8);
		}
	}


	/** Reads a UTF-8 coded string with length coding.
		It is expected that at the current read position,
		there is a 32 bit integer containing the length in (UTF8-)bytes,
		followed by the specified number of bytes in UTF-8 coding.

		@return the string read from the buffer or null if an error occurs.
	 */
	public String readString()
	{
		int length = read(32);
		if (length < 0)
		{
			return null;
		}
		return readString(length);
	}


	/** Reads a UTF-8 coded string without length coding.
		It is expected that at the current read position,
		there is string in UTF-8 coding.

		@return the string read from the buffer or null if an error occurs.
	 */
	public String readString(int nLength)
	{
		byte[] aBytes = new byte[nLength];
		for (int i = 0; i < nLength; i++)
		{
			aBytes[i] = (byte) read(8);
		}
		String s = null;
		try
		{
			s = new String(aBytes, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			if (TDebug.TraceAllExceptions) TDebug.out(e);
		}
		return s;
	}


	/** Reads a single bit.
	 */
	public boolean readFlag()
	{
		return (read(1) != 0);
	}

	private static native void setTrace(boolean bTrace);

	// for debugging
	public static void outBuffer(byte[] buffer)
	{
		String s = "";
		for (int i = 0; i < buffer.length; i++)
		{
			s += "" + buffer[i] + ", ";
		}
		TDebug.out("buffer: " + s);
	}
}



/*** Buffer.java ***/

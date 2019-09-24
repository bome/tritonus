/*
 *	MidiUtils.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.share.midi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.tritonus.share.TDebug;



/**	Helper methods for reading and writing MIDI files.
 */
public class MidiUtils
{
	public static int getUnsignedInteger(byte b)
	{
		return (b < 0) ? b + 256 : b;
	}



	public static int get14bitValue(int nLSB, int nMSB)
	{
		return (nLSB & 0x7F) | ((nMSB & 0x7F) << 7);
	}



	public static int get14bitMSB(int nValue)
	{
		return (nValue >> 7) & 0x7F;
	}



	public static int get14bitLSB(int nValue)
	{
		return nValue & 0x7F;
	}



	public static byte[] getVariableLengthQuantity(long lValue)
	{
		ByteArrayOutputStream	data = new ByteArrayOutputStream();
		try
		{
			writeVariableLengthQuantity(lValue, data);
		}
		catch (IOException e)
		{
			if (TDebug.TraceAllExceptions) { TDebug.out(e); }
		}
		return data.toByteArray();
	}



	public static int writeVariableLengthQuantity(long lValue, OutputStream outputStream)
		throws IOException
	{
		int	nLength = 0;
		// IDEA: use a loop
		boolean	bWritingStarted = false;
		int	nByte = (int) ((lValue >> 21) & 0x7f);
		if (nByte != 0)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
			bWritingStarted = true;
		}
		nByte = (int) ((lValue >> 14) & 0x7f);
		if (nByte != 0 || bWritingStarted)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
			bWritingStarted = true;
		}
		nByte = (int) ((lValue >> 7) & 0x7f);
		if (nByte != 0 || bWritingStarted)
		{
			if (outputStream != null)
			{
				outputStream.write(nByte | 0x80);
			}
			nLength++;
		}
		nByte = (int) (lValue & 0x7f);
		if (outputStream != null)
		{
			outputStream.write(nByte);
		}
		nLength++;
		return nLength;
	}
}



/*** MidiUtils.java ***/

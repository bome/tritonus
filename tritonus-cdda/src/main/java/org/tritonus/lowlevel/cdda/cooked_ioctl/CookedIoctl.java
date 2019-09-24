/*
 *	CookedIoctl.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer
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

package org.tritonus.lowlevel.cdda.cooked_ioctl;

import org.tritonus.share.TDebug;



/**	Reading audio CDs using the 'cooked ioctl' interface.
 */
public class CookedIoctl
{
	static
	{
		if (TDebug.TraceCdda) { TDebug.out("CookedIoctl.<clinit>(): loading native library tritonuscooked_ioctl"); }
		System.loadLibrary("tritonuscooked_ioctl");
		if (TDebug.TraceCdda) { TDebug.out("CookedIoctl.<clinit>(): loaded"); }
		// TODO: ????
		setTrace(TDebug.TraceCddaNative);
	}



	/*
	 *	This holds a file descriptor for the native code -
	 *	do not touch!
	 */
	@SuppressWarnings("unused")
	private long		m_lNativeHandle;



	// TODO: parameter strDevicename (or something else sensible)
	public CookedIoctl(String strDevice)
	{
		if (TDebug.TraceCdda) { System.out.println("CookedIoctl.<init>: begin"); }
		int	nResult = open(strDevice);
		if (nResult < 0)
		{
			throw new RuntimeException("cannot open" + strDevice);
		}
		if (TDebug.TraceCdda) { System.out.println("CookedIoctl.<init>: end"); }
	}



	/**	Opens the device.
	 */
	private native int open(String strDevice);

	/**	Closes the device.
	 */
	public native void close();


	/*
	 *	anValues[0]	first track
	 *	anValues[1]	last track
	 *
	 *	anStartTrack[x]	start sector of the track x.
	 *	anType[x]	type of track x.
	 */
	public native int readTOC(int[] anValues,
			   int[] anStartFrame,
			   int[] anLength,
			   int[] anType,
			   boolean[] abCopy,
			   boolean[] abPre,
			   int[] anChannels);



	/**	Reads one or more raw frames from the CD.
		This call reads <CODE>nCount</CODE> frames starting at
		lba position <CODE>nFrame</CODE>.
		<CODE>abData</CODE>  has to be big enough to hold the
		amount of data requested (<CODE>2352 * nCount</CODE> bytes).
	 */
	public native int readFrame(int nFrame, int nCount, byte[] abData);

	private static native void setTrace(boolean bTrace);
}



/*** CookedIoctl.java ***/

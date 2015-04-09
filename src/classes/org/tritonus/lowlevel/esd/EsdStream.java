/*
 *	EsdStream.java
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

package org.tritonus.lowlevel.esd;

import org.tritonus.share.TDebug;


public class EsdStream
extends	Esd
{
	/**	Holds socket fd to EsounD.
	 *	This field is long because on 64 bit architectures, the native
	 *	size of ints may be 64 bit.
	 */
	@SuppressWarnings("unused")
	private long			m_lNativeHandle;



	static
	{
		Esd.loadNativeLibrary();
		if (TDebug.TraceEsdStreamNative)
		{
			setTrace(true);
		}
	}



	public EsdStream()
	{
	}



	/**	Opens the connection to esd and initiates a stream.
	 *
	 */	
	public native void open(int nFormat, int nSampleRate);



	/**	Writes a block of data to esd.
	 *	Before using this method, you have to open a connection
	 *	to esd with open(). After being done, call close() to
	 *	release native and server-side resources.
	 *
	 *	@return	the number of bytes written
	 */
	public native int write(byte[] abData, int nOffset, int nLength);



	/**	Closes the connection to esd.
	 *	With this call, all resources inside esd associated with
	 *	this stream are freed.???
	 *	Calls to the write() method are no longer allowed after
	 *	return from this call.
	 */
	public native void close();



	private static native void setTrace(boolean bTrace);
}



/*** EsdStream.java ***/

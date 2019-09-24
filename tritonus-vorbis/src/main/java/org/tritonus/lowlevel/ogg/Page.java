/*
 *	Page.java
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



/** Wrapper for ogg_page.
 */
public class Page
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
	 *	Holds the pointer to ogg_page
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public Page()
	{
		if (TDebug.TraceOggNative) { TDebug.out("Page.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of ogg_page failed");
		}
		if (TDebug.TraceOggNative) { TDebug.out("Page.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	private native int malloc();
	public native void free();


	/** Calls ogg_page_version().
	 */
	public native int getVersion();

	/** Calls ogg_page_continued().
	 */
	public native boolean isContinued();

	/** Calls ogg_page_packets().
	 */
	public native int getPackets();

	/** Calls ogg_page_bos().
	 */
	public native boolean isBos();

	/** Calls ogg_page_eos().
	 */
	public native boolean isEos();

	/** Calls ogg_page_granulepos().
	 */
	public native long getGranulePos();

	/** Calls ogg_page_serialno().
	 */
	public native int getSerialNo();

	/** Calls ogg_page_pageno().
	 */
	public native int getPageNo();

	/** Calls ogg_page_checksum_set().
	 */
	public native void setChecksum();


	public native byte[] getHeader();

	public native byte[] getBody();


	private static native void setTrace(boolean bTrace);
}





/*** Page.java ***/

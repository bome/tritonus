/*
 *	AlsaPcmHWParamsFormatMask.java
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

package org.tritonus.lowlevel.alsa;

import org.tritonus.share.TDebug;



/** TODO:
 */
public class AlsaPcmHWParamsFormatMask
{
	/**
	 *	Holds the pointer to snd_pcm_format_mask_t
	 *	for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	public AlsaPcmHWParamsFormatMask()
	{
		if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcmHWParamsFormatMask.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of format_mask failed");
		}
		if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcmHWParamsFormatMask.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}


	/**
	 *	Calls snd_pcm_format_mask_malloc().
	 */
	private native int malloc();



	/**
	 *	Calls snd_pcm_format_mask_free().
	 */
	public native void free();

	/**
	 *	Calls snd_pcm_format_mask_none().
	 */
	public native void none();

	/**
	 *	Calls snd_pcm_format_mask_any().
	 */
	public native void any();

	/**
	 *	Calls snd_pcm_format_mask_test().
	 */
	public native boolean test(int nFormat);


	/**
	 *	Calls snd_pcm_format_mask_set().
	 */
	public native void set(int nFormat);


	/**
	 *	Calls snd_pcm_format_mask_reset().
	 */
	public native void reset(int nFormat);


}



/*** AlsaPcmHWParamsFormatMask.java ***/

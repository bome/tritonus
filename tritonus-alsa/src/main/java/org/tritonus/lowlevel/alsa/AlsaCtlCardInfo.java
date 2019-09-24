/*
 *	AlsaCtlCardInfo.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 - 2002 by Matthias Pfisterer
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



/**	TODO:
 */
public class AlsaCtlCardInfo
{
	/**	Holds the pointer to snd_ctl_card_info_t.
		for the native code.
		This must be long to be 64bit-clean.
		The access modifier is not private because this
		variable has to be accessed from AlsaCtl.
	*/
	long	m_lNativeHandle;



	public AlsaCtlCardInfo()
	{
		if (TDebug.TraceAlsaCtlNative) { TDebug.out("AlsaPcm.CardInfo.<init>(): begin"); }
		int	nReturn = malloc();
		if (nReturn < 0)
		{
			throw new RuntimeException("malloc of card_info failed");
		}
		if (TDebug.TraceAlsaCtlNative) { TDebug.out("AlsaPcm.CardInfo.<init>(): end"); }
	}



	public void finalize()
	{
		// TODO: call free()
		// call super.finalize() first or last?
		// and introduce a flag if free() has already been called?
	}



	/**	Calls snd_ctl_card_info_malloc().
	 */
	private native int malloc();

	/**	Calls snd_ctl_card_info_free().
	 */
	public native void free();


	/**	Calls snd_ctl_card_info_get_card().
	 */
	public native int getCard();

	/**	Calls snd_ctl_card_info_get_id().
	 */
	public native String getId();

	/**	Calls snd_ctl_card_info_get_driver().
	 */
	public native String getDriver();

	/**	Calls snd_ctl_card_info_get_name().
	 */
	public native String getName();

	/**	Calls snd_ctl_card_info_get_longname().
	 */
	public native String getLongname();

	/**	Calls snd_ctl_card_info_get_mixername().
	 */
	public native String getMixername();

	/**	Calls snd_ctl_card_info_get_components().
	 */
	public native String getComponents();

	private static native void setTrace(boolean bTrace);
}



/*** AlsaCtlCardInfo.java ***/

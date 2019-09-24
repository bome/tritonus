/*
 *	AlsaCtl.java
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



/**	TODO:
 */
public class AlsaCtl
{
	/** Contains a pointer to snd_ctl_t.
	 */
	@SuppressWarnings("unused")
	private long	m_lNativeHandle;



	static
	{
		Alsa.loadNativeLibrary();
		if (TDebug.TraceAlsaCtlNative)
		{
			setTrace(true);
		}
	}



	public static native int loadCard(int nCard);
	// this encapsulates snd_card_next()
	public static native int[] getCards();
	public static native int getCardIndex(String strName);
	public static native String getCardName(int nCard);
	public static native String getCardLongName(int nCard);



	/**	Open a ctl.

		Objects created with this constructor have to be
		closed by calling {@link #close() close()}. This is
		necessary to free native resources.

		@param strName The name of the sound card. For
		instance, "hw:0", or an identifier you gave the
		card ("CARD1").

		@param nMode Special modes for the low-level opening
		like SND_CTL_NONBLOCK, SND_CTL_ASYNC. Normally, set
		this to 0.

	*/
	public AlsaCtl(String strName, int nMode)
		throws Exception
	{
		if (open(strName, nMode) < 0)
		{
			throw new Exception();
		}
	}



	public AlsaCtl(int nCard)
		throws Exception
	{
		this("hw:" + nCard, 0);
	}


	/**	Calls snd_ctl_open().
	 */
	private native int open(String strName, int nMode);

	/**	Calls snd_ctl_close().
	 */
	public native int close();

	/**	Calls snd_ctl_card_info().
	 */
	public native int getCardInfo(AlsaCtlCardInfo cardInfo);


	// TODO: ??
	public native int[] getPcmDevices();

	// TODO: remove
	/**
	 *	anValues[0]	device (inout)
	 *	anValues[1]	subdevice (inout)
	 *	anValues[2]	stream (inout)
	 *	anValues[3]	card (out)
	 *	anValues[4]	class (out)
	 *	anValues[5]	subclass (out)
	 *	anValues[6]	subdevice count (out)
	 *	anValues[7]	subdevice available (out)
	 *
	 *	astrValues[0]	id (out)
	 *	astrValues[1]	name (out)
	 *	astrValues[2]	subdevice name (out)
	 */
	public native int getPcmInfo(int[] anValues, String[] astrValues);


	private static native void setTrace(boolean bTrace);
}



/*** AlsaCtl.java ***/

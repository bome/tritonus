/*
 *	AlsaReceiver.java
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

package org.tritonus.midi.device.alsa;

import javax.sound.midi.Receiver;



/**	A Receiver that is based on an ALSA client.
	This interface is used to facilitate subscriptions between
	Transmitters and Receivers. This subscription is used to
	transfer events directely inside the ALSA sequencer, instead of
	passing them by Java.
 */
public interface AlsaReceiver
extends Receiver
{
	/**	Establish the subscription.
		Calling this method establishes a subscription between a
		AlsaTransmitters' ALSA client and port, which are passed as
		parameters here, and this Receiver's ALSA client and port.
		This method is typically called by an AlsaTransmitter that
		got an AlsaReceiver as its Receiver.

		@param nClient The ALSA client number of the Transmitter that
		a read subscription should established to.

		@param nPort The ALSA port number of the Transmitter that a
		read subscription should established to.

		@return true, if the subscription was established, false
		otherwise.
	 */
	public boolean subscribeTo(int nClient, int nPort);
}



/*** AlsaReceiver.java ***/


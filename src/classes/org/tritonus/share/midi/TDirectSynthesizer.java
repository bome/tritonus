/*
 *	TDirectSynthesizer.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2004 by Matthias Pfisterer
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

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;


/**
 * Base class for Synthesizer implementations.
 * 
 * <p>
 * This base class is for Synthesizer implementations that do not itself operate
 * on MIDI, but instread implement the MidiChannel interface. For these
 * implementations, MIDI behaviour is simulated on top of MidiChannel.
 * </p>
 * 
 * @see javax.sound.midi.MidiChannel
 *
 * @author Matthias Pfisterer
 */
public abstract class TDirectSynthesizer
extends TMidiDevice
implements Synthesizer
{
	/**	Initialize this class.
	 *	This sets the info from the passed one, sets the open status
	 *	to false, the number of Receivers to zero and the collection
	 *	of Transmitters to be empty.
	 *
	 *	@param info	The info object that describes this instance.
	 */
	public TDirectSynthesizer(MidiDevice.Info info)
	{
		// no Transmitters, only Receivers
		super(info, false, true);
	}


	/**
	 * Obtains the MidiChannel with the specified number.
	 *
	 * @param nChannel the requested channel number (0..15)
	 * @return the respective <code>MidiChannel</code> object
	 */
	private MidiChannel getChannel(int nChannel)
	{
		return getChannels()[nChannel];
	}


	/**
	 * Handles MIDI messages coming in from Receivers.
	 * 
	 */
	protected void receive(MidiMessage message, long lTimeStamp)
	{
		if (message instanceof ShortMessage)
		{
			ShortMessage shortMsg = (ShortMessage) message;
			int nChannel = shortMsg.getChannel();
			int nCommand = shortMsg.getCommand();
			int nData1 = shortMsg.getData1();
			int nData2 = shortMsg.getData2();
			switch (nCommand)
			{
			case ShortMessage.NOTE_OFF:
				getChannel(nChannel).noteOff(nData1, nData2);
				break;

			case ShortMessage.NOTE_ON:
				getChannel(nChannel).noteOn(nData1, nData2);
				break;

			case ShortMessage.POLY_PRESSURE:
				getChannel(nChannel).setPolyPressure(nData1, nData2);
				break;

			case ShortMessage.CONTROL_CHANGE:
				getChannel(nChannel).controlChange(nData1, nData2);
				break;

			case ShortMessage.PROGRAM_CHANGE:
				getChannel(nChannel).programChange(nData1);
				break;

			case ShortMessage.CHANNEL_PRESSURE:
				getChannel(nChannel).setChannelPressure(nData1);
				break;

			case ShortMessage.PITCH_BEND:
				getChannel(nChannel).setPitchBend(nData1 | (nData2 << 7));
				break;

			default:
			}
		}
	}
}


/*** TDirectSynthesizer.java ***/

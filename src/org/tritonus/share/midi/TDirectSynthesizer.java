/*
 *	TDirectSynthesizer.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2004 by Matthias Pfisterer
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


package org.tritonus.share.midi;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import org.tritonus.share.TDebug;

/* TODO: implement aquiring of a SourceDataLine, perhaps in a separate
   class TSoftwareSynthesizer.
 */

/**	Base class for Synthesizer implementations.  This base class is
	for Synthesizer implementations that do not itself operate on
	MIDI, but instread implement the MidiChannel interface. For these
	implementations, MIDI behaviour is simulated on top of
	MidiChannel.

	@author Matthias Pfisterer
 */
public abstract class TDirectSynthesizer
extends TMidiDevice
implements Synthesizer
{
	/** Bank value from CC 0/32 per channel.
	 */
	private int[]	m_anBanks;


	/**	Initialize this class.
	 *	This sets the info from the passed one, sets the open status
	 *	to false, the number of Receivers to zero and the collection
	 *	of Transmitters to be empty.
	 *
	 *	@param info	The info object that describes this instance.
	 */
	public TDirectSynthesizer(MidiDevice.Info info)
	{
		// no IN, only OUT
		super(info, false, true);
		m_anBanks = new int[16];
		for (int i = 0; i < m_anBanks.length; i++)
		{
			m_anBanks[i] = -1;
		}
	}



	// TODO: check if this is needed
	/**
	 *	Subclasses have to override this method to be notified of
	 *	opening.
	 */
	protected void openImpl()
		throws MidiUnavailableException
	{
	}



	// TODO: check if this is needed
	/**
	 *	Subclasses have to override this method to be notified of
	 *	closeing.
	 */
	protected void closeImpl()
	{
	}



	/**
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
				switch (nData1)
				{
				case 0: // bank MSB
					m_anBanks[nChannel] = nData2 << 7;
					break;

				case 32: // bank LSB
					m_anBanks[nChannel] |= nData2;
					break;

				case 0x78:
					getChannel(nChannel).allSoundOff();
					break;

				case 0x79:
					getChannel(nChannel).resetAllControllers();
					break;

				case 0x7A:
					getChannel(nChannel).localControl(nData2 == 0x7F);
					break;

				case 0x7B:
					getChannel(nChannel).allNotesOff();
					break;

				case 0x7C: // omni off
					getChannel(nChannel).setOmni(false);
					break;

				case 0x7D: // omni on
					getChannel(nChannel).setOmni(true);
					break;

				case 0x7E: // mono on
					getChannel(nChannel).setMono(true);
					break;

				case 0x7F: // poly on
					getChannel(nChannel).setMono(false);
					break;

				default:
					getChannel(nChannel).controlChange(nData1, nData2);
					break;
				}
				break;

			case ShortMessage.PROGRAM_CHANGE:
				if (m_anBanks[nChannel] != -1)
				{
					getChannel(nChannel).programChange(m_anBanks[nChannel],
													   nData1);
					m_anBanks[nChannel] = -1;
				}
				else
				{
					getChannel(nChannel).programChange(nData1);
				}
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

	private MidiChannel getChannel(int nChannel)
	{
		return getChannels()[nChannel];
	}
}



/*** TDirectSynthesizer.java ***/

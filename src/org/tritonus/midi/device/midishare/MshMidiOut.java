/*
 *	MshMidiOut.java
 */

/*
 *   Copyright © Grame 2000 for the Tritonus project by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 *  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
 *  grame@grame.fr
 *
 */

package	org.tritonus.midi.device.midishare;

import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.InvalidMidiDataException;

import	org.tritonus.TDebug;

import	grame.midishare.Midi;
import	grame.midishare.MidiException;


public class MshMidiOut
{
	/**	The MidiShare application refnum
	 */
	private int	m_refNum = -1;

	public MshMidiOut(int refnum)
	{
		m_refNum = refnum;
		Midi.Connect (m_refNum,0,1);
	}

	protected void enqueueMessage(MidiMessage event, long date_micro)
	{
		try {
			int mshEv = MshEventConverter.decodeMessage(event,date_micro);
		
			if (date_micro == -1)
				Midi.SendIm(m_refNum,mshEv);
			else
				Midi.SendAt(m_refNum,mshEv,(int)(date_micro/1000));
				
		}catch (InvalidMidiDataException e1) { 
		}catch (MidiException e2) {}
		
	}

}


/*** MshMidiOut.java ***/

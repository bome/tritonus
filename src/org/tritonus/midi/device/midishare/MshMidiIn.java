/*
 *	MshMidiIn.java
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

import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.InvalidMidiDataException;

import	org.tritonus.TDebug;

import grame.midishare.*;


public class MshMidiIn
	extends		Thread
{
	private int 				m_refNum  = -1;  // the MidiShare application refnum
	private MshMidiInListener	m_listener;

	public MshMidiIn(int refnum, MshMidiInListener listener)
	{
		m_refNum = refnum;
		m_listener = listener;
		Midi.Connect (0,m_refNum,1); 
	}

	public void run()
	{
		// TODO: recheck interupt mechanism
		int	mshEv;
		
		while (!interrupted())
		{
			try {
				mshEv = Midi.GetEv(m_refNum);
			
				if (mshEv != 0) {
				
					// Convert Note in KeyOn/KeyOff pair
					
					if (Midi.GetType(mshEv) == Midi.typeNote){
						Midi.SetType(mshEv, Midi.typeKeyOn);
						int keyOff = Midi.CopyEv(mshEv);
						if (keyOff != 0) {
							Midi.SetType(keyOff, Midi.typeKeyOff);
							Midi.SendAt(m_refNum+128, keyOff, Midi.GetDate(keyOff) + Midi.GetField(keyOff,2));
						}
					}
					
					m_listener.dequeueEvent(MshEventConverter.encodeEvent(mshEv));
					Midi.FreeEv(mshEv);
				}
				
			}catch (InvalidMidiDataException e1) {}
			
		}
	}

	public static interface MshMidiInListener
	{
		public void dequeueEvent(MidiEvent event);
	}
}



/*** MshMidiIn.java ***/

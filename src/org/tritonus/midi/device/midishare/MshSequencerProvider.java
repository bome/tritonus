/*
 *	MshSequencerProvider.java
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
/*
import	com.sun.java.util.collections.ArrayList;
import	com.sun.java.util.collections.List;
import	com.sun.java.util.collections.Iterator;
*/

import	java.util.ArrayList;
import	java.util.List;
import	java.util.Iterator;


import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.spi.MidiDeviceProvider;

public class MshSequencerProvider
	extends		MidiDeviceProvider
{
	private MidiDevice.Info		m_info;


	public MshSequencerProvider()
	{
		m_info = new MidiDevice.Info("Tritonus MidiShare sequencer", "Tritonus is free software. See http://www.tritonus.org/", "this sequencer uses MidiShare", "0.0");
	}


	public MidiDevice.Info[] getDeviceInfo()
	{
		MidiDevice.Info[]	infos = new MidiDevice.Info[1];
		infos[0] = m_info;
		return infos;
	}


	public MidiDevice getDevice(MidiDevice.Info info)
	{
		if (info.equals(m_info))
		{
			return new MshSequencer(m_info);
		}
		else
		{
			throw new IllegalArgumentException("no device for " + info);
		}
	}

}



/*** MshSequencerProvider.java ***/

/*
 *	MshMidiDeviceProvider.java
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

import	org.tritonus.lowlevel.alsa.ASequencer;

import	org.tritonus.TDebug;


public class MshMidiDeviceProvider
	extends		MidiDeviceProvider
{
	// perhaps move to superclass
	private static final MidiDevice.Info[]	EMPTY_INFO_ARRAY = new MidiDevice.Info[0];

	/*
	 *	The Sun jdk 1.3 creates new instances of service provider
	 *	classes on each request. Due to that, and because
	 *	MidiDevice.Infos are
	 *	compared by object reference, we need a static instance.
	 */
	private static List		m_devices;


	public MshMidiDeviceProvider()
	{
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("MshMidiDeviceProvider.<init>: called");
		}
		synchronized (MshMidiDeviceProvider.class)
		{
			if (m_devices == null)
			{
				m_devices = new ArrayList();
				MshMidiDevice device = new MshMidiDevice();
				m_devices.add(device);
			}
		}
	}


	public MidiDevice.Info[] getDeviceInfo()
	{
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("MshMidiDeviceProvider.getDeviceInfo(): called");
		}
		List		infos = new ArrayList();
		Iterator	iterator = m_devices.iterator();
		while (iterator.hasNext())
		{
			MidiDevice	device = (MidiDevice) iterator.next();
			MidiDevice.Info	info = device.getDeviceInfo();
			infos.add(info);
		}
		return (MidiDevice.Info[]) infos.toArray(EMPTY_INFO_ARRAY);
	}



	public MidiDevice getDevice(MidiDevice.Info info)
	{
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("MshMidiDeviceProvider.getDevice(): called");
		}
		Iterator	iterator = m_devices.iterator();
		while (iterator.hasNext())
		{
			MidiDevice	device = (MidiDevice) iterator.next();
			MidiDevice.Info	info2 = device.getDeviceInfo();
			if (info.equals(info2))
			{
				return device;
			}
		}
		throw new IllegalArgumentException("no device for " + info);
	}

}



/*** MshMidiDeviceProvider.java ***/

/*
 *	AlsaMidiDeviceProvider.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.midi.device.alsa;


import	java.util.ArrayList;
import	java.util.List;
import	java.util.Iterator;

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.spi.MidiDeviceProvider;

import	org.tritonus.lowlevel.alsa.AlsaSeq;

import	org.tritonus.share.TDebug;




public class AlsaMidiDeviceProvider
	extends		MidiDeviceProvider
{
	// perhaps move to superclass
	private static final MidiDevice.Info[]	EMPTY_INFO_ARRAY = new MidiDevice.Info[0];

	private static List		m_devices;
	private static AlsaSeq	m_aSequencer;



	public AlsaMidiDeviceProvider()
	{
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("AlsaMidiDeviceProvider.<init>(): begin");
		}
		synchronized (AlsaMidiDeviceProvider.class)
		{
			//TDebug.out("gaga");
			if (m_devices == null)
			{
				//TDebug.out("gaga2");
				m_devices = new ArrayList();
				TDebug.out("gaga2a");
				m_aSequencer = new AlsaSeq("Tritonus ALSA device manager");
				//TDebug.out("gaga3");
				scanPorts();
			}
		}
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("AlsaMidiDeviceProvider.<init>(): end");
		}
	}



	public MidiDevice.Info[] getDeviceInfo()
	{
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
		Iterator	iterator = m_devices.iterator();
		while (iterator.hasNext())
		{
			MidiDevice	device = (MidiDevice) iterator.next();
			MidiDevice.Info	info2 = device.getDeviceInfo();
			if (info != null && info.equals(info2))
			{
				return device;
			}
		}
		throw new IllegalArgumentException("no device for " + info);
	}



	private void scanPorts()
	{
		if (TDebug.TraceMidiDeviceProvider)
		{
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): begin");
		}
		Iterator	clients = m_aSequencer.getClientInfos();
		// TDebug.out("" + clients);
		while (clients.hasNext())
		{
			AlsaSeq.ClientInfo	clientInfo = (AlsaSeq.ClientInfo) clients.next();
			int	nClient = clientInfo.getClientId();
			if (TDebug.TracePortScan)
			{
				TDebug.out("AlsaMidiDeviceProvider.scanPorts(): client: " + nClient);
			}
			Iterator	ports = m_aSequencer.getPortInfos(nClient);
			// TDebug.out("" + clients);
			while (ports.hasNext())
			{
				AlsaSeq.PortInfo	portInfo = (AlsaSeq.PortInfo) ports.next();
				int	nPort = portInfo.getPort();
				int	nType = portInfo.getType();
				if (TDebug.TracePortScan)
				{
					TDebug.out("AlsaMidiDeviceProvider.scanPorts(): port: " + nPort);
					TDebug.out("AlsaMidiDeviceProvider.scanPorts(): type: " + nType);
				}
				if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_GENERIC) != 0)
				{
					// TDebug.out("generic midi");
					MidiDevice	device = null;
					if ((nType & (AlsaSeq.SND_SEQ_PORT_TYPE_SYNTH | AlsaSeq.SND_SEQ_PORT_TYPE_DIRECT_SAMPLE | AlsaSeq.SND_SEQ_PORT_TYPE_SAMPLE)) != 0)
					{
						device = new AlsaSynthesizer(nClient, nPort);
					}
					else	// ordinary midi port
					{
						device = new AlsaMidiDevice(nClient, nPort);
					}
					m_devices.add(device);
				}
/*
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_GM) != 0)
  {
  TDebug.out("gm");
  }
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_GS) != 0)
  {
  TDebug.out("gs");
  }
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_XG) != 0)
  {
  TDebug.out("xg");
  }
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_SYNTH) != 0)
  {
  TDebug.out("synth");
  }
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_DIRECT_SAMPLE) != 0)
  {
  TDebug.out("direct sample");
  }
  if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_SAMPLE) != 0)
  {
  TDebug.out("sample");
  }
*/
			}
		}
	}

}



/*** AlsaMidiDeviceProvider.java ***/

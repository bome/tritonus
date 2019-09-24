/*
 *	AlsaMidiDeviceProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer
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

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

import org.tritonus.lowlevel.alsa.AlsaSeq;
import org.tritonus.lowlevel.alsa.AlsaSeqClientInfo;
import org.tritonus.lowlevel.alsa.AlsaSeqPortInfo;

import org.tritonus.share.TDebug;




public class AlsaMidiDeviceProvider
extends MidiDeviceProvider
{
	// perhaps move to superclass
	private static final MidiDevice.Info[]	EMPTY_INFO_ARRAY = new MidiDevice.Info[0];
	private static final int	READ_CAPABILITY = AlsaSeq.SND_SEQ_PORT_CAP_READ | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_READ;
	private static final int	WRITE_CAPABILITY = AlsaSeq.SND_SEQ_PORT_CAP_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_WRITE;

	private static List<MidiDevice>		m_devices;
	private static AlsaSeq	m_alsaSeq;



	public AlsaMidiDeviceProvider()
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.<init>(): begin"); }
		synchronized (AlsaMidiDeviceProvider.class)
		{
			if (m_devices == null)
			{
				m_devices = new ArrayList<MidiDevice>();
				if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.<init>(): creating AlsaSeq..."); }
				// try{
				m_alsaSeq = new AlsaSeq("Tritonus ALSA device manager");
				// }catch (Throwable t) { TDebug.out(t); }
				if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.<init>(): ...done"); }
				scanPorts();
			}
		}
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.<init>(): end"); }
	}



	public MidiDevice.Info[] getDeviceInfo()
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDeviceInfo(): begin"); }
		List<MidiDevice.Info>	infoList = new ArrayList<MidiDevice.Info>();
		Iterator<MidiDevice>	iterator = m_devices.iterator();
		while (iterator.hasNext())
		{
			MidiDevice	device = iterator.next();
			MidiDevice.Info	info = device.getDeviceInfo();
			infoList.add(info);
		}
		MidiDevice.Info[]	infos = infoList.toArray(EMPTY_INFO_ARRAY);
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDeviceInfo(): end"); }
		return infos;
	}



	public MidiDevice getDevice(MidiDevice.Info info)
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDevice(): begin"); }
		MidiDevice	returnedDevice = null;
		Iterator<MidiDevice>	iterator = m_devices.iterator();
		while (iterator.hasNext())
		{
			MidiDevice	device = iterator.next();
			MidiDevice.Info	info2 = device.getDeviceInfo();
			if (info != null && info.equals(info2))
			{
				returnedDevice = device;
				break;
			}
		}
		if (returnedDevice == null)
		{
			throw new IllegalArgumentException("no device for " + info);
		}
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDevice(): end"); }
		return returnedDevice;
	}



	private void scanPorts()
	{
		if (TDebug.TraceMidiDeviceProvider || TDebug.TracePortScan) { TDebug.out("AlsaMidiDeviceProvider.scanPorts(): begin"); }
		Iterator	clients = m_alsaSeq.getClientInfos();
		while (clients.hasNext())
		{
			AlsaSeqClientInfo	clientInfo = (AlsaSeqClientInfo) clients.next();
			int	nClient = clientInfo.getClient();
			if (TDebug.TracePortScan) { TDebug.out("AlsaMidiDeviceProvider.scanPorts(): client: " + nClient); }
			Iterator	ports = m_alsaSeq.getPortInfos(nClient);
			while (ports.hasNext())
			{
				AlsaSeqPortInfo	portInfo = (AlsaSeqPortInfo) ports.next();
				handlePort(clientInfo, portInfo);
			}
		}
		if (TDebug.TraceMidiDeviceProvider || TDebug.TracePortScan) { TDebug.out("AlsaMidiDeviceProvider.scanPorts(): end"); }
	}


	private void handlePort(AlsaSeqClientInfo clientInfo,
				AlsaSeqPortInfo portInfo)
	{
		int	nClient = clientInfo.getClient();
		int	nPort = portInfo.getPort();
		int	nType = portInfo.getType();
		int	nCapability = portInfo.getCapability();
		int	nSynthVoices = portInfo.getSynthVoices();
		if (TDebug.TracePortScan)
		{
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): port: " + nPort);
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): type: " + nType);
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): cap: " + nCapability);
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): midi channels: " + portInfo.getMidiChannels());
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): midi voices: " + portInfo.getMidiVoices());
			TDebug.out("AlsaMidiDeviceProvider.scanPorts(): synth voices: " + portInfo.getSynthVoices());
		}
		if ((nType & AlsaSeq.SND_SEQ_PORT_TYPE_MIDI_GENERIC) != 0)
		{
			// TDebug.out("generic midi");
			MidiDevice	device = null;
			if ((nType & (AlsaSeq.SND_SEQ_PORT_TYPE_SYNTH | AlsaSeq.SND_SEQ_PORT_TYPE_DIRECT_SAMPLE | AlsaSeq.SND_SEQ_PORT_TYPE_SAMPLE)) != 0)
			{
				boolean	bWriteSubscriptionAllowed = (nCapability & WRITE_CAPABILITY) == WRITE_CAPABILITY;
				if (bWriteSubscriptionAllowed)
				{
					device = new AlsaSynthesizer(nClient, nPort, nSynthVoices);
				}
				else
				{
					if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDevice(): port does not allows write subscription, not used"); }
				}
			}
			else	// ordinary midi port
			{
				boolean	bReadSubscriptionAllowed = (nCapability & READ_CAPABILITY) == READ_CAPABILITY;
				boolean	bWriteSubscriptionAllowed = (nCapability & WRITE_CAPABILITY) == WRITE_CAPABILITY;
				if (bReadSubscriptionAllowed || bWriteSubscriptionAllowed)
				{
					device = new AlsaMidiDevice(nClient, nPort, bReadSubscriptionAllowed, bWriteSubscriptionAllowed);
				}
				else
				{
					if (TDebug.TraceMidiDeviceProvider) { TDebug.out("AlsaMidiDeviceProvider.getDevice(): port allows neither read nor write subscription, not used"); }
				}
			}
			if (device != null)
			{
				m_devices.add(device);
			}
		}
	}
}



/*** AlsaMidiDeviceProvider.java ***/

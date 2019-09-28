/*
 *	MidiDeviceTestCase.java
 */

/*
 *  Copyright (c) 2003 by Matthias Pfisterer
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

package org.tritonus.test.api.midi.device;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/** Class for tests of javax.sound.midi.MidiDevice.
 */
public class MidiDeviceTestCase
extends BaseMidiDeviceTestCase
{
    @Test
	public void testGetDeviceInfo()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					MidiDevice.Info info = device.getDeviceInfo();
					assertNotNull(info, "getDeviceInfo()");
					assertNotNull(info.getName(), "DeviceInfo.getName()");
					assertNotNull(info.getVendor(), "DeviceInfo.getVendor()");
					assertNotNull(info.getDescription(), "DeviceInfo.getDescription()");
					assertNotNull(info.getVersion(), "DeviceInfo.getVersion()");
				}
			};
		checkMidiDevice(check);
	}

	@Test
	public void testOpenClose()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					assertTrue(! device.isOpen(), "closed");
					device.open();
					assertTrue(device.isOpen(), "open");
					device.close();
					assertTrue(! device.isOpen(), "closed");
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetMicrosecondPosition()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					long lPosition = device.getMicrosecondPosition();
					assertTrue(lPosition == -1 || lPosition == 0, "getMicrosecondPosition() before open");
					device.open();
					lPosition = device.getMicrosecondPosition();
					assertTrue(lPosition == -1 || lPosition >= 0, "getMicrosecondPosition() after open");
					device.close();
					lPosition = device.getMicrosecondPosition();
					assertTrue(lPosition == -1 || lPosition == 0, "getMicrosecondPosition() after close");
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetMaxReceivers()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
				{
					int nMax = device.getMaxReceivers();
					assertTrue(nMax == -1 || nMax == 0, "getMaxReceivers()");
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetMaxTransmitters()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
				{
					int nMax = device.getMaxTransmitters();
					assertTrue(nMax == -1 || nMax == 0, "getMaxTransmitters()");
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetReceiver()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					int nMax = device.getMaxReceivers();
					if (nMax != 0)
					{
						nMax = (nMax == -1) ? 100: nMax;
						Receiver[] aReceivers = new Receiver[nMax];
						for (int i = 0; i < nMax; i++)
						{
							aReceivers[i] = device.getReceiver();
							assertNotNull(aReceivers[i], "getReceiver()");
							for (int j = 0; j < i - 1; j++)
							{
								assertTrue(aReceivers[i] != aReceivers[j], "Receiver objects unique");
							}
						}
						for (int i = 0; i < nMax; i++)
						{
							aReceivers[i].close();
						}
					}
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetReceivers()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					assertEquals(0, device.getReceivers().size(), "getReceivers() length");
					int nMax = device.getMaxReceivers();
					if (nMax != 0)
					{
						nMax = (nMax == -1) ? 100: nMax;
						Receiver[] aReceivers = new Receiver[nMax];
						for (int i = 0; i < nMax; i++)
						{
							aReceivers[i] = device.getReceiver();
							assertTrue(device.getReceivers().contains(aReceivers[i]),
									   "Receiver in getReceivers()");
						}
						assertEquals(nMax, device.getReceivers().size(), "getReceivers() length");
						for (int i = 0; i < nMax; i++)
						{
							aReceivers[i].close();
							assertTrue(! device.getReceivers().contains(aReceivers[i]),
									   "Receiver not in getReceivers()");
						}
					}
					assertEquals(0, device.getReceivers().size(), "getReceivers() length");
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetTransmitter()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					int nMax = device.getMaxTransmitters();
					if (nMax != 0)
					{
						nMax = (nMax == -1) ? 100: nMax;
						Transmitter[] aTransmitters = new Transmitter[nMax];
						for (int i = 0; i < nMax; i++)
						{
							aTransmitters[i] = device.getTransmitter();
							assertNotNull(aTransmitters[i], "getTransmitter()");
							for (int j = 0; j < i - 1; j++)
							{
								assertTrue(aTransmitters[i] != aTransmitters[j], "Transmitter objects unique");
							}
						}
						for (int i = 0; i < nMax; i++)
						{
							aTransmitters[i].close();
						}
					}
				}
			};
		checkMidiDevice(check);
	}


    @Test
	public void testGetTransmitters()
		throws Exception
	{
		Check check = new Check()
			{
				public void check(MidiDevice device)
					throws Exception
				{
					assertEquals(0, device.getTransmitters().size(), "getTransmitters() length");
					int nMax = device.getMaxTransmitters();
					if (nMax != 0)
					{
						nMax = (nMax == -1) ? 100: nMax;
						Transmitter[] aTransmitters = new Transmitter[nMax];
						for (int i = 0; i < nMax; i++)
						{
							aTransmitters[i] = device.getTransmitter();
							assertTrue(device.getTransmitters().contains(aTransmitters[i]),
									   "Transmitter in getTransmitters()");
						}
						assertEquals(nMax, device.getTransmitters().size(), "getTransmitters() length");
						for (int i = 0; i < nMax; i++)
						{
							aTransmitters[i].close();
							assertTrue(! device.getTransmitters().contains(aTransmitters[i]),
									   "Transmitter not in getTransmitters()");
						}
					}
					assertEquals(0, device.getTransmitters().size(), "getTransmitters() length");
				}
			};
		checkMidiDevice(check);
	}
}



/*** MidiDeviceTestCase.java ***/

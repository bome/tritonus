/*
 *	MidiSystemTestCase.java
 */

/*
 *  Copyright (c) 2004 by Matthias Pfisterer
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

package org.tritonus.test.api.midi.midisystem;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
//import javax.sound.midi.Sequencer;
//import javax.sound.midi.Synthesizer;



public class MidiSystemTestCase
{
	private static final float DELTA = 1E-9F;


    @Test
	public void testGetDevices()
		throws Exception
	{
		assertNotNull(MidiSystem.getSynthesizer(), "getSynthesizer()");
System.err.println(MidiSystem.getSynthesizer());
		assertNotNull(MidiSystem.getSequencer(), "getSequencer()");
System.err.println(MidiSystem.getSequencer());
 		assertNotNull(MidiSystem.getSequencer(true), "getSequencer(true)");
System.err.println(MidiSystem.getSequencer(true));
 		assertNotNull(MidiSystem.getSequencer(false), "getSequencer(false)");
System.err.println(MidiSystem.getSequencer(false));
		assertNotNull(MidiSystem.getReceiver(), "getReceiver()");
System.err.println(MidiSystem.getReceiver());
        try {
            assertNotNull(MidiSystem.getTransmitter(), "getTransmitter()");
System.err.println(MidiSystem.getTransmitter());
        } catch (MidiUnavailableException e) {
            // accept
        }
	}


    @Test
	public void testGetEachMidiDevice()
		throws Exception
	{
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		assertTrue(infos.length > 0, "MidiDevice.Info array");
		for (int i = 0; i < infos.length; i++)
		{
			assertNotNull(MidiSystem.getMidiDevice(infos[i]), "getMidiDevice()");
		}
	}


    @Test
	public void testGetWrongMidiDevice()
		throws Exception
	{
		MidiDevice.Info info = new TestInfo("name", "vendor",
											"description", "version");
		try
		{
			MidiSystem.getMidiDevice(info);
			fail("wrong MidiDevice.Info should throw exception");
		}
		catch (IllegalArgumentException e)
		{
		}
	}


	private class TestInfo
	extends MidiDevice.Info
	{
		public TestInfo(String name, String vendor,
						String description, String version)
		{
			super(name, vendor,
				  description, version);
		}
	}
}



/*** MidiSystemTestCase.java ***/

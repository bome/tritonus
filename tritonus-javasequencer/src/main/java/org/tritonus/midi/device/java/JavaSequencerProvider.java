/*
 *	JavaSequencerProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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

package org.tritonus.midi.device.java;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

import org.tritonus.share.TDebug;
import org.tritonus.share.GlobalInfo;
import org.tritonus.share.midi.TMidiDevice;




public class JavaSequencerProvider
extends MidiDeviceProvider
{
	private static MidiDevice.Info		sm_info;



	public JavaSequencerProvider()
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.<init>(): begin"); }
		synchronized (JavaSequencerProvider.class)
		{
			if (sm_info == null)
			{
				sm_info = new TMidiDevice.Info(
					"Tritonus Java Sequencer",
					GlobalInfo.getVendor(),
					"this is a pure-java sequencer",
					GlobalInfo.getVersion());
			}
		}
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.<init>(): end"); }
	}



	public MidiDevice.Info[] getDeviceInfo()
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.getDeviceInfo(): begin"); }
		MidiDevice.Info[]	infos = new MidiDevice.Info[1];
		infos[0] = sm_info;
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.getDeviceInfo(): end"); }
		return infos;
	}



	public MidiDevice getDevice(MidiDevice.Info info)
	{
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.getDevice(): begin"); }
		MidiDevice	device = null;
		if (info != null && info.equals(sm_info))
		{
			device = new JavaSequencer(sm_info);
		}
		if (device == null)
		{
			throw new IllegalArgumentException("no device for " + info);
		}
		if (TDebug.TraceMidiDeviceProvider) { TDebug.out("JavaSequencerProvider.getDevice(): end"); }
		return device;
	}
}



/*** JavaSequencerProvider.java ***/

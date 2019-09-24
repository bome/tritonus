/*
 *	FluidSynthesizerProvider.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2006 by Matthias Pfisterer
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

package org.tritonus.midi.device.fluidsynth;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.spi.MidiDeviceProvider;

import org.tritonus.share.TDebug;
import org.tritonus.share.GlobalInfo;
import org.tritonus.share.midi.TMidiDevice;




public class FluidSynthesizerProvider
extends MidiDeviceProvider
{
	private static MidiDevice.Info		sm_info;



	public FluidSynthesizerProvider()
	{
		if (TDebug.TraceMidiDeviceProvider)
			TDebug.out("FluidSynthesizerProvider.<init>(): begin");
		synchronized (FluidSynthesizerProvider.class)
		{
			if (sm_info == null)
			{
				sm_info = new TMidiDevice.Info(
					"Tritonus fluidsynth Synthesizer",
					GlobalInfo.getVendor(),
					"a synthesizer based on fluidsynth",
					GlobalInfo.getVersion());
			}
		}
		if (TDebug.TraceMidiDeviceProvider)
			TDebug.out("FluidSynthesizerProvider.<init>(): end");
	}



	public MidiDevice.Info[] getDeviceInfo()
	{
		if (TDebug.TraceMidiDeviceProvider) TDebug.out("FluidSynthesizerProvider.getDeviceInfo(): begin");
		MidiDevice.Info[]	infos = new MidiDevice.Info[1];
		infos[0] = sm_info;
		if (TDebug.TraceMidiDeviceProvider) TDebug.out("FluidSynthesizerProvider.getDeviceInfo(): end");
		return infos;
	}



	public MidiDevice getDevice(MidiDevice.Info info)
	{
		if (TDebug.TraceMidiDeviceProvider) TDebug.out("FluidSynthesizerProvider.getDevice(): begin");
		MidiDevice	device = null;
		if (info != null && info.equals(sm_info))
		{
			try
			{
				device = new FluidSynthesizer(sm_info);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("unable to create device for " + info, e);
			}
		}
		else
		{
			throw new IllegalArgumentException("no device for " + info);
		}
		if (TDebug.TraceMidiDeviceProvider) TDebug.out("FluidSynthesizerProvider.getDevice(): end");
		return device;
	}
}

/*** FluidSynthesizerProvider.java ***/

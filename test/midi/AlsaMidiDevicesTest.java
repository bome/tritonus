/*
 *	AlsaMidiDevicesTest.java
 */

/*
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.ShortEvent;
*/
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiSystem;

import	org.tritonus.midi.device.alsa.AlsaMidiDevice;

import	org.tritonus.TDebug;



public class AlsaMidiDevicesTest
{
	public static void main(String[] args)
	{
		org.tritonus.TDebug.TraceASequencer = false;
		org.tritonus.TDebug.TraceInit = false;

		MidiDevice.Info[]	infos = MidiSystem.getMidiDeviceInfo();
		TDebug.out("after MidiSystem.getMidiDeviceInfo()");
		for (int i = 0; i < infos.length; i++)
		{
			out(infos[i]);
		}
	}



	public static void out(MidiDevice.Info info)
	{
		TDebug.out("name: " + info.getName());
	}
}



/*** AlsaMidiDevicesTest.java ***/

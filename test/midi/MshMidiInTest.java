/*
 *	MshMidiInTest.java
 */


import	javax.sound.midi.Sequence;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiSystem;

import	org.tritonus.midi.device.midishare.MshMidiDevice;

import	org.tritonus.TDebug;



public class MshMidiInTest
{
	public static void main(String[] args)
	{
		org.tritonus.TDebug.TraceInit = false;
		MidiDevice.Info[]	infos = MidiSystem.getMidiDeviceInfo();
		TDebug.out("after MidiSystem.getMidiDeviceInfo()");
	
		MshMidiDevice	device = new MshMidiDevice();
		Receiver	r = new DumpReceiver(System.out);
		
		try
		{
			device.open();
			Transmitter	t = device.getTransmitter();
			t.setReceiver(r);
			
			while (true){
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e){}
			}
	
		}catch (MidiUnavailableException e){
			e.printStackTrace();
		}
		
	}
}

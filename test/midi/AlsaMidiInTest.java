/*
 *	AlsaMidiInTest.java
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

import	org.tritonus.share.TDebug;



public class AlsaMidiInTest
{
	public static void main(String[] args)
	{
		MidiDevice.Info[]	infos = MidiSystem.getMidiDeviceInfo();
		TDebug.out("after MidiSystem.getMidiDeviceInfo()");
		int	nChannel = 64;
		int	nPort = 0;
		if (args.length == 2)
		{
			nChannel = Integer.parseInt(args[0]);
			nPort = Integer.parseInt(args[1]);
		}
		AlsaMidiDevice	device = new AlsaMidiDevice(nChannel, nPort);
		try
		{
			device.open();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		Receiver	r = new DumpReceiver(System.out);
		try
		{
			Transmitter	t = device.getTransmitter();
			t.setReceiver(r);
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		while (true)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// System.out.println("in endless loop");
		}
	}
}

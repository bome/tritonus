/*
 *	AlsaMidiOutTest.java
 */

import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;

import	org.tritonus.midi.device.alsa.AlsaMidiDevice;



public class AlsaMidiOutTest
{
	public static void main(String[] args)
	{
		org.tritonus.TDebug.TraceASequencer = true;
		org.tritonus.TDebug.TraceASequencerDetails = true;
		org.tritonus.TDebug.TraceAlsaMidiOut = true;
		org.tritonus.TDebug.TraceTMidiDevice = true;

		int	nChannel = 64;
		int	nPort = 0;
		if (args.length == 2)
		{
			nChannel = Integer.parseInt(args[0]);
			nPort = Integer.parseInt(args[1]);
		}
		AlsaMidiDevice	device = new AlsaMidiDevice(nChannel, nPort, false, true);
		device.open();
		Receiver	r = null;
		try
		{
			r = device.getReceiver();

		}
		catch (MidiUnavailableException e)
		{
		}
		System.out.println("Receiver: " + r);
		try
		{
			ShortMessage	m0 = new ShortMessage();
			m0.setMessage(0x90, 0, 61, 80);
			r.send(m0, /*-1*/0);
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
			}
			ShortMessage	m1 = new ShortMessage();
			m1.setMessage(0x80, 0, 61, 0);
			r.send(m1, /*-1*/0);
		}
		catch (InvalidMidiDataException e)
		{
		}

		r.close();
		device.close();
	}
}

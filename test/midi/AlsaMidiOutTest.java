/*
 *	AlsaMidiOutTest.java
 */

import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;

import	org.tritonus.midi.device.alsa.AlsaMidiDevice;

import	org.tritonus.share.TDebug;


public class AlsaMidiOutTest
{
	public static void main(String[] args)
	{
		int	nChannel = 64;
		int	nPort = 0;
		if (args.length == 2)
		{
			nChannel = Integer.parseInt(args[0]);
			nPort = Integer.parseInt(args[1]);
		}
		AlsaMidiDevice	device = new AlsaMidiDevice(nChannel, nPort, false, true);
		try
		{
			device.open();
		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
		}
		Receiver	r = null;
		try
		{
			r = device.getReceiver();

		}
		catch (MidiUnavailableException e)
		{
			e.printStackTrace();
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
				e.printStackTrace();
			}
			ShortMessage	m1 = new ShortMessage();
			m1.setMessage(0x80, 0, 61, 0);
			r.send(m1, /*-1*/0);
		}
		catch (InvalidMidiDataException e)
		{
			e.printStackTrace();
		}

		r.close();
		device.close();
	}
}

/*
 *	MshMidiOutTest.java
 */

import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiUnavailableException;

import	org.tritonus.midi.device.midishare.MshMidiDevice;



public class MshMidiOutTest
{
	public static void main(String[] args)
	{
		
		MshMidiDevice	device = new MshMidiDevice();	
		Receiver	r = null;
		try
		{
			device.open();
			r = device.getReceiver();
			System.out.println("Receiver: " + r);
			
			for (int i = 0; i<50; i++){
				try
				{
					ShortMessage	m0 = new ShortMessage();
					m0.setMessage(0x90, 0, 61, 80);
					//r.send(m0, -1);  // for current date
					r.send(m0, device.getMicroSecondPosition() + 1000000);
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e)
					{
					}
					ShortMessage	m1 = new ShortMessage();
					m1.setMessage(0x80, 0, 61, 0);
					//r.send(m1, -1);  // for current date
					r.send(m1, device.getMicroSecondPosition() + 1000000);
				}
				catch (InvalidMidiDataException e) {}
			}

			r.close();
			device.close();
			
		}catch (MidiUnavailableException e){
			e.printStackTrace();
		}

	}
}

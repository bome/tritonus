/*
 *	AlsaSequencer.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


package	org.tritonus.midi.device.alsa;

import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Track;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.MidiDevice;

import	org.tritonus.TDebug;
import	org.tritonus.lowlevel.alsa.ASequencer;
import	org.tritonus.midi.device.TSequencer;



public class AlsaSequencer
	extends		TSequencer
	implements	AlsaMidiIn.AlsaMidiInListener
{
	private ASequencer	m_controlASequencer;
	private ASequencer	m_dataASequencer;
	private int		m_nControlClient;
	private int		m_nDataClient;
	private int		m_nControlPort;
	private int		m_nDataPort;
	private int		m_nQueue;
	private AlsaMidiIn	m_alsaMidiIn;
	private AlsaMidiOut	m_alsaMidiOut;
	private Thread		m_loaderThread;



	public AlsaSequencer(MidiDevice.Info info)
	{
		super(info);
	}



	private int getControlClient()
	{
		return m_nControlClient;
	}



	private int getControlPort()
	{
		return m_nControlPort;
	}



	private int getDataClient()
	{
		return m_nDataClient;
	}



	private int getDataPort()
	{
		return m_nDataPort;
	}



	private int getQueue()
	{
		return m_nQueue;
	}



	protected void openImpl()
	{
		m_controlASequencer = new ASequencer("Tritonus ALSA Sequencer (control)");
		m_nControlClient = m_controlASequencer.getClientId();
		m_nControlPort = m_controlASequencer.createPort("control port", ASequencer.SND_SEQ_PORT_CAP_WRITE | ASequencer.SND_SEQ_PORT_CAP_SUBS_WRITE | ASequencer.SND_SEQ_PORT_CAP_READ | ASequencer.SND_SEQ_PORT_CAP_SUBS_READ, 0, ASequencer.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		m_dataASequencer = new ASequencer("Tritonus ALSA Sequencer (data)");
		m_nDataClient = m_dataASequencer.getClientId();
		m_nDataPort = m_dataASequencer.createPort("data port", ASequencer.SND_SEQ_PORT_CAP_WRITE | ASequencer.SND_SEQ_PORT_CAP_SUBS_WRITE | ASequencer.SND_SEQ_PORT_CAP_READ | ASequencer.SND_SEQ_PORT_CAP_SUBS_READ, 0, ASequencer.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		// m_nQueue = m_controlASequencer.allocQueue();
		m_nQueue = m_dataASequencer.allocQueue();
		m_dataASequencer.setQueueLocked(getQueue(), false);
		m_alsaMidiOut = new AlsaMidiOut(m_dataASequencer, getDataPort(), getQueue());
		m_alsaMidiOut.setHandleMetaMessages(true);

		// this establishes the subscription, too
		m_alsaMidiIn = new AlsaMidiIn(m_dataASequencer, getDataPort(), getDataClient(), getDataPort(), this);
		// start the receiving thread
		m_alsaMidiIn.start();
	}



	protected void closeImpl()
	{
		m_alsaMidiIn.interrupt();
		m_alsaMidiIn = null;
		// TODO:
		// m_aSequencer.releaseQueue(getQueue());
		// m_aSequencer.destroyPort(getPort());
		m_controlASequencer.close();
		m_controlASequencer = null;
		m_dataASequencer.close();
		m_dataASequencer = null;
	}



	public void start()
	{
/*	TODO:
	setTempoInMPQ(500000);
*/
		m_controlASequencer.setQueueTempo(getQueue(), getSequence().getResolution(), 500000/*getBPM() * getFactor()*/);
		startSeq();
		m_loaderThread = new LoaderThread();
		m_loaderThread.start();
	}



	public void stop()
	{
		stopSeq();
	}






	public boolean isRunning()
	{
		// TODO:
		return false;
	}



	public void startRecording()
	{
	}



	public void stopRecording()
	{
		// TODO:
	}



	public boolean isRecording()
	{
		return false;
	}



	// name should be: enableRecording
	public void recordEnable(Track track, int nChannel)
	{
		// TODO:
	}

	// name should be: disableRecording
	public void recordDisable(Track track)
	{
		// TODO:
	}



	protected float getTempoNative()
	{
		if (isOpen())
		{
			return m_controlASequencer.getQueueTempo(getQueue());
		}
		else
		{
			return 0.0F;
		}
	}



	protected void setTempoNative(float fRealMPQ)
	{
		if (isOpen())
		{
			m_controlASequencer.setQueueTempo(getQueue(), getSequence().getResolution(), (int) fRealMPQ);
		}
	}



	public long getTickPosition()
	{
		if (isOpen())
		{
			return m_controlASequencer.getQueuePositionTick(getQueue());
		}
		else
		{
			return 0;
		}
	}



	public void setTickPosition(long lTick)
	{
		if (isOpen())
		{
			m_controlASequencer.setQueuePositionTick(getControlPort(), getQueue(), lTick);
		}
	}



	public long getMicrosecondPosition()
	{
		if (isOpen())
		{
			long	lNanoSeconds = m_controlASequencer.getQueuePositionTime(getQueue()) / 1000;
			return lNanoSeconds / 1000;
		}
		else
		{
			return 0;
		}
	}



	public void setMicrosecondPosition(long lMicroseconds)
	{
		if (isOpen())
		{
			long	lNanoSeconds = lMicroseconds * 1000;
			m_controlASequencer.setQueuePositionTime(getControlPort(), getQueue(), lNanoSeconds);
		}
	}





	public Sequencer.SyncMode getMasterSyncMode()
	{
		// TODO:
		return null;
	}



	public void setMasterSyncMode(Sequencer.SyncMode syncMode)
	{
		// TODO:
	}



	public Sequencer.SyncMode[] getMasterSyncModes()
	{
		// TODO:
		return new Sequencer.SyncMode[0];
	}



	public Sequencer.SyncMode getSlaveSyncMode()
	{
		// TODO:
		return null;
	}



	public void setSlaveSyncMode(Sequencer.SyncMode syncMode)
	{
		// TODO:
	}


	public Sequencer.SyncMode[] getSlaveSyncModes()
	{
		// TODO:
		return new Sequencer.SyncMode[0];
	}





	public boolean getTrackMute(int nTrack)
	{
		// TODO:
		return false;
	}



	public void setTrackMute(int nTrack, boolean bMute)
	{
		// TODO:
	}



	public boolean getTrackSolo(int nTrack)
	{
		// TODO:
		return false;
	}



	public void setTrackSolo(int nTrack, boolean bSolo)
	{
		// TODO:
	}



/*
  private void loadSequenceToNative()
  {
  Sequence	sequence = getSequence();
  Track[]	aTracks = sequence.getTracks();
  TDebug.out("# tracks: " + aTracks.length);
  for (int nTrack = 0; nTrack < aTracks.length; nTrack++)
  {
  TDebug.out("track " + nTrack);
  Track	track = aTracks[nTrack];
  for (int nEvent = 0; nEvent < track.size(); nEvent++)
  {
  MidiEvent	event = track.get(nEvent);
  MidiMessage	message = event.getMessage();
  long		lTick = event.getTick();
  m_alsaMidiOut.enqueueMessage(message, lTick);
  }
  }
  }
*/


	private void loadSequenceToNative()
	{
		Sequence	sequence = getSequence();
		Track[]	aTracks = sequence.getTracks();
		int[]	anTrackPositions = new int[aTracks.length];
		for (int i = 0; i < aTracks.length; i++)
		{
			anTrackPositions[i] = 0;
		}
		while (true)
		{
			boolean		bTrackPresent = false;
			long	lBestTick = Long.MAX_VALUE;
			int	nBestTrack = -1;
			for (int nTrack = 0; nTrack < aTracks.length; nTrack++)
			{
				// TDebug.out("track " + nTrack);
				// Track	track = aTracks[nTrack];
				if (anTrackPositions[nTrack] < aTracks[nTrack].size())
				{
					bTrackPresent = true;
					MidiEvent	event = aTracks[nTrack].get(anTrackPositions[nTrack]);
					long		lTick = event.getTick();
					if (lTick < lBestTick)
					{
						lBestTick = lTick;
						nBestTrack = nTrack;
					}
				}
			}
			if (!bTrackPresent)
			{
				break;
			}
			MidiEvent	event = aTracks[nBestTrack].get(anTrackPositions[nBestTrack]);
			anTrackPositions[nBestTrack]++;
			MidiMessage	message = event.getMessage();
			long		lTick = event.getTick();
			if (TDebug.TraceAlsaSequencer)
			{
				TDebug.out("AlsaSequencer.loadSequenceToNative(): enqueueing event with tick " + lTick);
			}
			m_alsaMidiOut.enqueueMessage(message, lTick);
		}
	}



	// for AlsaMidiIn.AlsaMidiInListener
	// passes events to the receivers
	public void dequeueEvent(MidiEvent event)
	{
		MidiMessage	message = event.getMessage();
		if (TDebug.TraceAlsaSequencer)
		{
			TDebug.out("AlsaSequencer.dequeueEvent(): message: " + message);
		}
		if (message instanceof MetaMessage)
		{
			MetaMessage	metaMessage = (MetaMessage) message;
			// TDebug.out("meta!");
			if (metaMessage.getType() == 0x51)	// set tempo
			{
				byte[]	abData = metaMessage.getData();
				int	nTempo = signedByteToUnsigned(abData[0]) * 65536 +
					signedByteToUnsigned(abData[1]) * 256 +
					signedByteToUnsigned(abData[2]);
				// TDebug.out("tempo (us/quarter note): " + nTempo);
				setTempoInMPQ((float) nTempo);
					
			}
		}
		sendImpl(message, -1L);
		if (message instanceof MetaMessage)
		{
			// TODO: use extra thread for event delivery
			sendMetaMessage((MetaMessage) message);
		}
		else if (message instanceof ShortMessage && ((ShortMessage) message).getCommand() == ShortMessage.CONTROL_CHANGE)
		{
			sendControllerEvent((ShortMessage) message);
		}
	}





	private void startSeq()
	{
		m_controlASequencer.startQueue(getQueue(), getControlPort());
	}



	private void stopSeq()
	{
		m_controlASequencer.stopQueue(getQueue(), getControlPort());
	}

	///////////////////////////////////////////////////


	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new AlsaTransmitter();
	}


/////////////////// INNER CLASSES //////////////////////////////////////

	private class AlsaTransmitter
		extends		TTransmitter
	{
		private boolean		m_bReceiverSubscribed;



		public AlsaTransmitter()
		{
			super();
			m_bReceiverSubscribed = false;
		}



		public void setReceiver(Receiver receiver)
		{
			super.setReceiver(receiver);
			/*
			 *	Try to establish a subscription of the Receiver
			 *	to the ALSA seqencer client of the device this
			 *	Transmitter belongs to.
			 */
			if (receiver instanceof AlsaSequencerReceiver)
			{
				// TDebug.out("AlsaSequencer.AlsaTransmitter.setReceiver(): trying to establish subscription");
				m_bReceiverSubscribed = ((AlsaSequencerReceiver) receiver).subscribeTo(getDataClient(), getDataPort());
				// TDebug.out("AlsaSequencer.AlsaTransmitter.setReceiver(): subscription established: " + m_bReceiverSubscribed);
			}
		}



		public void send(MidiMessage message, long lTimeStamp)
		{
			/*
			 *	Send message via Java methods only if not
			 *	subscription was established. If there is a
			 *	subscription, the message is routed inside of
			 *	the ALSA sequencer.
			 */
			if (! m_bReceiverSubscribed)
			{
				super.send(message, lTimeStamp);
			}
		}



		public void close()
		{
			super.close();
			// TODO: remove subscription
		}
	}




	private class LoaderThread
		extends	Thread
	{
		public LoaderThread()
		{
		}


		public void run()
		{
			loadSequenceToNative();
		}
	}



/*
  public TimeMBT getMBT()
  {
  // TODO:
  return null;
  }
*/


/*
  public void setMBT(TimeMBT time)
  {
  }
*/
/*
  public TimeSMPTE getSMPTE()
  {
  return null;
  }
*/
/*
  public void setSMPTE(TimeSMPTE time)
  {
  }
*/


	private static int signedByteToUnsigned(byte b)
	{
		if (b >= 0)
		{
			return (int) b;
		}
		else
		{
			return 256 + (int) b ;
		}
	}
}



/*** AlsaSequencer.java ***/

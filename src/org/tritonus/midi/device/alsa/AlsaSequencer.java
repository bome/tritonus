/*
 *	AlsaSequencer.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	java.util.Arrays;

import	javax.sound.midi.InvalidMidiDataException;
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

import	org.tritonus.lowlevel.alsa.AlsaSeq;
import	org.tritonus.share.TDebug;
import	org.tritonus.share.midi.MidiUtils;
import	org.tritonus.share.midi.TSequencer;



public class AlsaSequencer
	extends		TSequencer
	implements	AlsaMidiIn.AlsaMidiInListener
{
	private static final SyncMode[]	MASTER_SYNC_MODES = {SyncMode.INTERNAL_CLOCK};
	private static final SyncMode[]	SLAVE_SYNC_MODES = {SyncMode.NO_SYNC};

	private AlsaSeq			m_controlAlsaSeq;
	private AlsaSeq			m_dataAlsaSeq;
	private AlsaSeq.QueueInfo	m_queueInfo;
	private AlsaSeq.QueueStatus	m_queueStatus;
	private AlsaSeq.QueueTempo	m_queueTempo;
	private int			m_nControlClient;
	private int			m_nDataClient;
	private int			m_nControlPort;
	private int			m_nDataPort;
	private int			m_nQueue;
	private AlsaMidiIn		m_alsaMidiIn;
	private AlsaMidiOut		m_alsaMidiOut;
	private Thread			m_loaderThread;



	public AlsaSequencer(MidiDevice.Info info)
	{
		super(info,
		      Arrays.asList(MASTER_SYNC_MODES),
		      Arrays.asList(SLAVE_SYNC_MODES));
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.<init>(): begin"); }
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.<init>(): end"); }
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



	private AlsaSeq.QueueStatus getQueueStatus()
	{
		return m_queueStatus;
	}



	private AlsaSeq.QueueTempo getQueueTempo()
	{
		return m_queueTempo;
	}



	private AlsaSeq getDataAlsaSeq()
	{
		return m_dataAlsaSeq;
	}



	private AlsaSeq getControlAlsaSeq()
	{
		return m_controlAlsaSeq;
	}



	private void updateQueueStatus()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.updateQueueStatus(): begin"); }
		// TODO: error handling
		getControlAlsaSeq().getQueueStatus(getQueue(), getQueueStatus());
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.updateQueueStatus(): end"); }
	}



	protected void openImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.openImpl(): begin"); }
		m_controlAlsaSeq = new AlsaSeq("Tritonus ALSA Sequencer (control)");
		m_nControlClient = getControlAlsaSeq().getClientId();
		m_nControlPort = getControlAlsaSeq().createPort("control port", AlsaSeq.SND_SEQ_PORT_CAP_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_READ | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_READ, 0, AlsaSeq.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		m_dataAlsaSeq = new AlsaSeq("Tritonus ALSA Sequencer (data)");
		m_nDataClient = getDataAlsaSeq().getClientId();
		m_nDataPort = getDataAlsaSeq().createPort("data port", AlsaSeq.SND_SEQ_PORT_CAP_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_READ | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_READ, 0, AlsaSeq.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		// m_nQueue = getControlAlsaSeq().allocQueue();
		m_nQueue = getDataAlsaSeq().allocQueue();
		m_queueInfo = new AlsaSeq.QueueInfo();
		m_queueStatus = new AlsaSeq.QueueStatus();
		m_queueTempo = new AlsaSeq.QueueTempo();
		getDataAlsaSeq().getQueueInfo(getQueue(), m_queueInfo);
		m_queueInfo.setLocked(false);
		getDataAlsaSeq().setQueueInfo(getQueue(), m_queueInfo);
		m_alsaMidiOut = new AlsaMidiOut(getDataAlsaSeq(), getDataPort(), getQueue());
		m_alsaMidiOut.setHandleMetaMessages(true);

		// this establishes the subscription, too
		m_alsaMidiIn = new AlsaMidiIn(getDataAlsaSeq(), getDataPort(), getDataClient(), getDataPort(), this);
		// start the receiving thread
		m_alsaMidiIn.start();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.openImpl(): end"); }
	}



	protected void closeImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.closeImpl(): begin"); }
		m_alsaMidiIn.interrupt();
		m_alsaMidiIn = null;
		getQueueStatus().free();
		m_queueStatus = null;
		getQueueTempo().free();
		m_queueTempo = null;
		// TODO:
		// m_aSequencer.releaseQueue(getQueue());
		// m_aSequencer.destroyPort(getPort());
		getControlAlsaSeq().close();
		m_controlAlsaSeq = null;
		getDataAlsaSeq().close();
		m_dataAlsaSeq = null;
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.closeImpl(): end"); }
	}



	protected void startImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.startImpl(): begin"); }
		setTempoInMPQ(500000);
		startSeq();
		m_loaderThread = new LoaderThread();
		m_loaderThread.start();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.startImpl(): end"); }
	}



	protected void stopImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.stopImpl(): begin"); }
		stopSeq();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.stopImpl(): end"); }
	}






	public boolean isRunning()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.isRunning(): begin"); }
		updateQueueStatus();
		int	nStatus = getQueueStatus().getStatus();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.isRunning(): queueu status: " + nStatus); }
		boolean	bRunning = nStatus != 0;
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.isRunning(): end"); }
		return bRunning;
	}



	public void startRecording()
	{
		// TODO:
	}



	public void stopRecording()
	{
		// TODO:
	}



	public boolean isRecording()
	{
		// TODO:
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



	protected void setTempoImpl(float fRealMPQ)
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTempoImpl(): begin"); }
		if (isOpen())
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTempoImpl(): setting tempo to " + (int) fRealMPQ); }
			getQueueTempo().setTempo((int) fRealMPQ);
			getQueueTempo().setPpq(getResolution());
			getControlAlsaSeq().setQueueTempo(getQueue(), getQueueTempo());
		}
		else
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTempoImpl(): ignoring because sequencer is not opened"); }
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTempoImpl(): end"); }
	}



	public long getTickPosition()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getTickPosition(): begin"); }
		long	lPosition;
		if (isOpen())
		{
			updateQueueStatus();
			lPosition = getQueueStatus().getTickTime();
		}
		else
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getTickPosition(): sequencer not open, returning 0"); }
			lPosition = 0;
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getTickPosition(): end"); }
		return lPosition;
	}



	public void setTickPosition(long lTick)
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTickPosition(): begin"); }
		if (isOpen())
		{
			getControlAlsaSeq().setQueuePositionTick(getControlPort(), getQueue(), lTick);
		}
		else
		{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTickPosition(): ignored because sequencer is not open"); }
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setTickPosition(): end"); }
	}



	public long getMicrosecondPosition()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getMicrosecondPosition(): begin"); }
		long	lPosition;
		if (isOpen())
		{
			updateQueueStatus();
			long	lNanoSeconds = getQueueStatus().getRealTime();
			lPosition = lNanoSeconds / 1000;
		}
		else
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getMicrosecondPosition(): sequencer not open, returning 0"); }
			lPosition = 0;
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.getMicrosecondPosition(): end"); }
		return lPosition;
	}



	public void setMicrosecondPosition(long lMicroseconds)
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setMicrosecondPosition(): begin"); }
		if (isOpen())
		{
			long	lNanoSeconds = lMicroseconds * 1000;
			getControlAlsaSeq().setQueuePositionTime(getControlPort(), getQueue(), lNanoSeconds);
		}
		else
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setMicrosecondPosition(): ignoring because sequencer is not open"); }
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.setMicrosecondPosition(): end"); }
	}





	protected void setMasterSyncModeImpl(SyncMode syncMode)
	{
		// TODO:
	}



	protected void setSlaveSyncModeImpl(SyncMode syncMode)
	{
		// TODO:
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



	private void loadSequenceToNative()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.loadSequenceToNative(): begin"); }
		Sequence	sequence = getSequence();
		Track[]	aTracks = sequence.getTracks();
		int[]	anTrackPositions = new int[aTracks.length];
		for (int i = 0; i < aTracks.length; i++)
		{
			anTrackPositions[i] = 0;
		}
		// this is used to get a useful tick value for the end of track message
		long	lHighestTick = 0;
		while (true)
		{
			boolean		bTrackPresent = false;
			long		lBestTick = Long.MAX_VALUE;
			int		nBestTrack = -1;
			for (int nTrack = 0; nTrack < aTracks.length; nTrack++)
			{
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
				MetaMessage	metaMessage = new MetaMessage();
				try
				{
					metaMessage.setMessage(0x2F, new byte[0], 0);
				}
				catch (InvalidMidiDataException e)
				{
					if (TDebug.TraceAllExceptions) { TDebug.out(e); }
				}
				if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.loadSequenceToNative(): sending End of Track message with tick " + (lHighestTick + 1)); }
				enqueueMessage(metaMessage, lHighestTick + 1);
				break;
			}
			MidiEvent	event = aTracks[nBestTrack].get(anTrackPositions[nBestTrack]);
			anTrackPositions[nBestTrack]++;
			MidiMessage	message = event.getMessage();
			long		lTick = event.getTick();
			lHighestTick = Math.max(lHighestTick, lTick);
			if (message instanceof MetaMessage && ((MetaMessage) message).getType() == 0x2F)
			{
				if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.loadSequenceToNative(): ignoring End of Track message with tick " + lTick); }
			}
			else
			{
				if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.loadSequenceToNative(): enqueueing event with tick " + lTick); }
				enqueueMessage(message, lTick);
			}
		}
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.loadSequenceToNative(): end"); }
	}



	private void enqueueMessage(MidiMessage message, long lTick)
	{
		m_alsaMidiOut.enqueueMessage(message, lTick);
	}



	// for AlsaMidiIn.AlsaMidiInListener
	// passes events to the receivers
	public void dequeueEvent(MidiEvent event)
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.dequeueEvent(): begin"); }
		MidiMessage	message = event.getMessage();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.dequeueEvent(): message: " + message); }
		if (message instanceof MetaMessage)
		{
			MetaMessage	metaMessage = (MetaMessage) message;
			// TDebug.out("meta!");
			if (metaMessage.getType() == 0x51)	// set tempo
			{
				byte[]	abData = metaMessage.getData();
				int	nTempo = MidiUtils.getUnsignedInteger(abData[0]) * 65536 +
					MidiUtils.getUnsignedInteger(abData[1]) * 256 +
					MidiUtils.getUnsignedInteger(abData[2]);
				// TDebug.out("tempo (us/quarter note): " + nTempo);
				setTempoInMPQ((float) nTempo);
					
			}
		}
		sendImpl(message, -1L);
		notifyListeners(message);
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.dequeueEvent(): end"); }
	}




	private void startSeq()
	{
		getControlAlsaSeq().startQueue(getQueue(), getControlPort());
	}



	private void stopSeq()
	{
		getControlAlsaSeq().stopQueue(getQueue(), getControlPort());
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
}



/*** AlsaSequencer.java ***/

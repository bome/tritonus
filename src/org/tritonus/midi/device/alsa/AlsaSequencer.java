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
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.ShortMessage;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;

import	org.tritonus.lowlevel.alsa.AlsaSeq;
import	org.tritonus.share.TDebug;
import	org.tritonus.share.midi.MidiUtils;
import	org.tritonus.share.midi.TSequencer;



public class AlsaSequencer
	extends		TSequencer
{
	private static final SyncMode[]	MASTER_SYNC_MODES = {SyncMode.INTERNAL_CLOCK};
	private static final SyncMode[]	SLAVE_SYNC_MODES = {SyncMode.NO_SYNC};

	private static final ShortMessage	CLOCK_MESSAGE = new ShortMessage();
	static
	{
		try
		{
			CLOCK_MESSAGE.setMessage(ShortMessage.TIMING_CLOCK);
		}
		catch (InvalidMidiDataException e)
		{
			if (TDebug.TraceSequencer || TDebug.TraceAllExceptions) { TDebug.out(e); }
		}
	}

	private AlsaSeq			m_playbackAlsaSeq;
	private AlsaSeq			m_recordingAlsaSeq;
	private int			m_nRecordingPort;
	private int			m_nPlaybackPort;
	private int			m_nQueue;
	private AlsaSeq.QueueInfo	m_queueInfo;
	private AlsaSeq.QueueStatus	m_queueStatus;
	private AlsaSeq.QueueTempo	m_queueTempo;
	private AlsaMidiIn		m_playbackAlsaMidiIn;
	private AlsaMidiOut		m_playbackAlsaMidiOut;
	private AlsaMidiIn		m_recordingAlsaMidiIn;
	private Thread			m_loaderThread;
	private Thread			m_syncThread;
	private AlsaSeq.Event		m_queueControlEvent;
	private boolean			m_bRecording;
	private Track			m_track;



	public AlsaSequencer(MidiDevice.Info info)
	{
		super(info,
		      Arrays.asList(MASTER_SYNC_MODES),
		      Arrays.asList(SLAVE_SYNC_MODES));
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.<init>(): begin"); }
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.<init>(): end"); }
	}



	private int getPlaybackClient()
	{
		int	nClient = getPlaybackAlsaSeq().getClientId();
		return nClient;
	}



	private int getPlaybackPort()
	{
		return m_nPlaybackPort;
	}



	private int getRecordingClient()
	{
		int	nClient = getRecordingAlsaSeq().getClientId();
		return nClient;
	}



	private int getRecordingPort()
	{
		return m_nRecordingPort;
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



	private AlsaSeq getPlaybackAlsaSeq()
	{
		return m_playbackAlsaSeq;
	}



	private AlsaSeq getRecordingAlsaSeq()
	{
		return m_recordingAlsaSeq;
	}



	private void updateQueueStatus()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.updateQueueStatus(): begin"); }
		// TODO: error handling
		// getRecordingAlsaSeq().getQueueStatus(getQueue(), getQueueStatus());
		getPlaybackAlsaSeq().getQueueStatus(getQueue(), getQueueStatus());
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.updateQueueStatus(): end"); }
	}



	protected void openImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.openImpl(): begin"); }
		m_recordingAlsaSeq = new AlsaSeq("Tritonus ALSA Sequencer (recording/synchronization)");
		m_nRecordingPort = getRecordingAlsaSeq().createPort("recording/synchronization port", AlsaSeq.SND_SEQ_PORT_CAP_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_READ | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_READ, 0, AlsaSeq.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		m_playbackAlsaSeq = new AlsaSeq("Tritonus ALSA Sequencer (playback)");
		m_nPlaybackPort = getPlaybackAlsaSeq().createPort("playback port", AlsaSeq.SND_SEQ_PORT_CAP_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_WRITE | AlsaSeq.SND_SEQ_PORT_CAP_READ | AlsaSeq.SND_SEQ_PORT_CAP_SUBS_READ, 0, AlsaSeq.SND_SEQ_PORT_TYPE_APPLICATION, 0, 0, 0);

		m_nQueue = getPlaybackAlsaSeq().allocQueue();
		m_queueInfo = new AlsaSeq.QueueInfo();
		m_queueStatus = new AlsaSeq.QueueStatus();
		m_queueTempo = new AlsaSeq.QueueTempo();
		getPlaybackAlsaSeq().getQueueInfo(getQueue(), m_queueInfo);
		m_queueInfo.setLocked(false);
		getPlaybackAlsaSeq().setQueueInfo(getQueue(), m_queueInfo);
		m_playbackAlsaMidiOut = new AlsaMidiOut(getPlaybackAlsaSeq(), getPlaybackPort(), getQueue());
		m_playbackAlsaMidiOut.setHandleMetaMessages(true);

		// this establishes the subscription, too
		AlsaMidiIn.AlsaMidiInListener	playbackListener = new PlaybackAlsaMidiInListener();
		m_playbackAlsaMidiIn = new AlsaMidiIn(getPlaybackAlsaSeq(), getPlaybackPort(), getPlaybackClient(), getPlaybackPort(), playbackListener);
		// start the receiving thread
		m_playbackAlsaMidiIn.start();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.openImpl(): end"); }
		m_queueControlEvent = new AlsaSeq.Event();
	}



	protected void closeImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.closeImpl(): begin"); }
		m_playbackAlsaMidiIn.interrupt();
		m_playbackAlsaMidiIn = null;
		getQueueStatus().free();
		m_queueStatus = null;
		getQueueTempo().free();
		m_queueTempo = null;
		// TODO:
		// m_aSequencer.releaseQueue(getQueue());
		// m_aSequencer.destroyPort(getPort());
		getRecordingAlsaSeq().close();
		m_recordingAlsaSeq = null;
		getPlaybackAlsaSeq().close();
		m_playbackAlsaSeq = null;
		m_queueControlEvent.free();
		m_queueControlEvent = null;
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.closeImpl(): end"); }
	}



	protected void startImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.startImpl(): begin"); }
		// TODO: start may also be a re-start after pausing. In this case, the tempo shouldn't be altered
		setTempoInMPQ(500000);
		startQueue();
//  		m_syncThread = new MasterSynchronizer();
//  		m_syncThread.start();
		m_loaderThread = new LoaderThread();
		m_loaderThread.start();
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.startImpl(): end"); }
	}



	protected void stopImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.stopImpl(): begin"); }
		stopQueue();
		stopRecording();
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
		m_bRecording = true;
		start();
	}



	public void stopRecording()
	{
		m_bRecording = false;
	}



	public boolean isRecording()
	{
		return m_bRecording;
	}



	// name should be: enableRecording
	public void recordEnable(Track track, int nChannel)
	{
		// TODO: hacky
		m_track = track;
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
			getPlaybackAlsaSeq().setQueueTempo(getQueue(), getQueueTempo());
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
			int	nSourcePort = getRecordingPort();
			int	nQueue = getQueue();
			long	lTime = lTick;
			sendQueueControlEvent(
				AlsaSeq.SND_SEQ_EVENT_SETPOS_TICK,
				AlsaSeq.SND_SEQ_TIME_STAMP_REAL | AlsaSeq.SND_SEQ_TIME_MODE_REL, 0, AlsaSeq.SND_SEQ_QUEUE_DIRECT, 0L,
				nSourcePort, AlsaSeq.SND_SEQ_CLIENT_SYSTEM, AlsaSeq.SND_SEQ_PORT_SYSTEM_TIMER,
				nQueue, 0, lTime);
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
			int	nSourcePort = getRecordingPort();
			int	nQueue = getQueue();
			long	lTime = lNanoSeconds;
			sendQueueControlEvent(
				AlsaSeq.SND_SEQ_EVENT_SETPOS_TIME,
				AlsaSeq.SND_SEQ_TIME_STAMP_REAL | AlsaSeq.SND_SEQ_TIME_MODE_REL, 0, AlsaSeq.SND_SEQ_QUEUE_DIRECT, 0L,
				nSourcePort, AlsaSeq.SND_SEQ_CLIENT_SYSTEM, AlsaSeq.SND_SEQ_PORT_SYSTEM_TIMER,
				nQueue, 0, lTime);
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



	/*
	  This method has to be synchronized because it is called
	  from sendMessageTick() as well as from loadSequenceToNative().
	 */
	private synchronized void enqueueMessage(MidiMessage message, long lTick)
	{
		m_playbackAlsaMidiOut.enqueueMessage(message, lTick);
	}



	/**	Put a message into the queue.
		This is Claus-Dieter's special method: it puts the message to
		the ALSA queue for delivery at the specified time.
		The time has to be given in ticks according to the resolution
		of the currently active Sequence. For this method to work,
		the Sequencer has to be started. The message is delivered
		the same way as messages from a Sequence, i.e. to all
		registered Transmitters. If the current queue position (as
		returned by getTickPosition()) is
		already behind the desired schedule time, the message is
		ignored.

		@param message the MidiMessage to put into the queue.

		@param lTick the desired schedule time in ticks.
	 */
	public void sendMessageTick(MidiMessage message, long lTick)
	{
		enqueueMessage(message, lTick);
	}



	private void startQueue()
	{
		controlQueue(AlsaSeq.SND_SEQ_EVENT_START);
	}



	private void stopQueue()
	{
		controlQueue(AlsaSeq.SND_SEQ_EVENT_STOP);
	}



	private void controlQueue(int nType)
	{
		int	nSourcePort = getRecordingPort();
		int	nQueue = getQueue();
		sendQueueControlEvent(
			nType, AlsaSeq.SND_SEQ_TIME_STAMP_REAL | AlsaSeq.SND_SEQ_TIME_MODE_REL, 0, AlsaSeq.SND_SEQ_QUEUE_DIRECT, 0L,
			nSourcePort, AlsaSeq.SND_SEQ_CLIENT_SYSTEM, AlsaSeq.SND_SEQ_PORT_SYSTEM_TIMER,
			nQueue, 0, 0);
	}


	// NOTE: also used for setting position
	public void sendQueueControlEvent(
		int nType, int nFlags, int nTag, int nQueue, long lTime,
		int nSourcePort, int nDestClient, int nDestPort,
		int nControlQueue, int nControlValue, long lControlTime)
	{
		m_queueControlEvent.setCommon(nType, nFlags, nTag, nQueue, lTime,
		0, nSourcePort, nDestClient, nDestPort);
		m_queueControlEvent.setQueueControl(nControlQueue, nControlValue, lControlTime);
		getPlaybackAlsaSeq().eventOutputDirect(m_queueControlEvent);
	}



	/**	Receive a correctely timestamped event.
		This method expects that the timestamp is in ticks,
		appropriate for the Sequence currently running.
	 */
	private void receiveTimestamped(MidiMessage message, long lTimestamp)
	{
		if (isRecording())
		{
			// TODO: this is hacky; should implement correct track mapping
			Track	track = m_track;
			MidiEvent	event = new MidiEvent(message, lTimestamp);
			track.add(event);
		}
		// TODO: entering an event into the sequence
	}



	/**	Receive an event from a Receiver.
	 	This method is called by AlsaSequencer.AlsaSequencerReceiver
		on receipt of a MidiMessage.
	 */
	protected void receive(MidiMessage message, long lTimestamp)
	{
		lTimestamp = getTickPosition();
		receiveTimestamped(message, lTimestamp);
	}



	///////////////////////////////////////////////////


	public Receiver getReceiver()
		throws	MidiUnavailableException
	{
		return new AlsaSequencerReceiver();
	}



	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		return new AlsaSequencerTransmitter();
	}


/////////////////// INNER CLASSES //////////////////////////////////////



	private class PlaybackAlsaMidiInListener
		implements AlsaMidiIn.AlsaMidiInListener
	{
		public void dequeueEvent(MidiMessage message, long lTimestamp)
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.PlaybackAlsaMidiInListener.dequeueEvent(): begin"); }
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.PlaybackAlsaMidiInListener.dequeueEvent(): message: " + message); }
			if (message instanceof MetaMessage)
			{
				MetaMessage	metaMessage = (MetaMessage) message;
				if (metaMessage.getType() == 0x51)	// set tempo
				{
					byte[]	abData = metaMessage.getData();
					int	nTempo = MidiUtils.getUnsignedInteger(abData[0]) * 65536 +
						MidiUtils.getUnsignedInteger(abData[1]) * 256 +
						MidiUtils.getUnsignedInteger(abData[2]);
					setTempoInMPQ((float) nTempo);
				}
			}
			// passes events to the receivers
			sendImpl(message, -1L);
			// calls control and meta listeners
			notifyListeners(message);
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.PlaybackAlsaMidiInListener.dequeueEvent(): end"); }
		}
	}



	private class RecordingAlsaMidiInListener
		implements AlsaMidiIn.AlsaMidiInListener
	{
		public void dequeueEvent(MidiMessage message, long lTimestamp)
		{
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.RecordingAlsaMidiInListener.dequeueEvent(): begin"); }
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.RecordingAlsaMidiInListener.dequeueEvent(): message: " + message); }
			AlsaSequencer.this.receiveTimestamped(message, lTimestamp);
			if (TDebug.TraceSequencer) { TDebug.out("AlsaSequencer.RecordingAlsaMidiInListener.dequeueEvent(): end"); }
		}
	}




	private class AlsaSequencerReceiver
		extends		TReceiver
		implements	AlsaReceiver
	{



		public AlsaSequencerReceiver()
		{
			super();
		}



		/**	Subscribe to the passed port.
		 *	This establishes a subscription in the ALSA sequencer
		 *	so that the device this Receiver belongs to receives
		 *	event from the client:port passed as parameters.
		 *
		 *	@return true if subscription was established,
		 *		false otherwise
		 */
		public boolean subscribeTo(int nClient, int nPort)
		{
			try
			{
				AlsaSeq.PortSubscribe	portSubscribe = new AlsaSeq.PortSubscribe();
				portSubscribe.setSender(nClient, nPort);
				portSubscribe.setDest(AlsaSequencer.this.getRecordingClient(), AlsaSequencer.this.getRecordingPort());
				portSubscribe.setQueue(AlsaSequencer.this.getQueue());
				portSubscribe.setExclusive(false);
				portSubscribe.setTimeUpdate(true);
				portSubscribe.setTimeReal(false);
				AlsaSequencer.this.getRecordingAlsaSeq().subscribePort(portSubscribe);
				portSubscribe.free();
				return true;
			}
			catch (RuntimeException e)
			{
				if (TDebug.TraceAllExceptions) { TDebug.out(e); }
				return false;
			}
		}


	}



	private class AlsaSequencerTransmitter
		extends		TTransmitter
	{
		private boolean		m_bReceiverSubscribed;



		public AlsaSequencerTransmitter()
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
			if (receiver instanceof AlsaReceiver)
			{
				// TDebug.out("AlsaSequencer.AlsaSequencerTransmitter.setReceiver(): trying to establish subscription");
				m_bReceiverSubscribed = ((AlsaReceiver) receiver).subscribeTo(getPlaybackClient(), getPlaybackPort());
				// TDebug.out("AlsaSequencer.AlsaSequencerTransmitter.setReceiver(): subscription established: " + m_bReceiverSubscribed);
			}
		}



		public void send(MidiMessage message, long lTimeStamp)
		{
			/*
			 *	Send message via Java methods only if no
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



	// TODO: start/stop; on/off
	// TODO: change to use AlsaSeq.Event directely
	private class MasterSynchronizer
		extends	Thread
	{
		public MasterSynchronizer()
		{
			// setPriority(10);
		}


		public void run()
		{
			long	lTickMax = getSequence().getTickLength();
			long	lTickStep = getSequence().getResolution() / 24;
			for (long lTick = 0; lTick < lTickMax; lTick += lTickStep)
			{
				// TDebug.out("MasterSynchronizer.run(): enqueueing clock message with tick " + lTick);
				enqueueMessage(CLOCK_MESSAGE, lTick);
			}
		}
	}
}



/*** AlsaSequencer.java ***/

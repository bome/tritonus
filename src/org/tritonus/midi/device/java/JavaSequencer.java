/*
 *	JavaSequencer.java
 */

package	org.tritonus.midi.device.java;


import	java.io.ByteArrayOutputStream;
import	java.io.InputStream;
import	java.io.IOException;
import	java.util.Arrays;

import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MidiSystem;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Track;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.InvalidMidiDataException;

import	org.tritonus.share.TDebug;
import	org.tritonus.share.midi.MidiUtils;
import	org.tritonus.share.midi.TSequencer;



public class JavaSequencer
	extends		TSequencer
	implements	Runnable
{
	private static final SyncMode[]	MASTER_SYNC_MODES = {SyncMode.INTERNAL_CLOCK};
	private static final SyncMode[]	SLAVE_SYNC_MODES = {SyncMode.NO_SYNC};

	// private Receiver	m_receiver;
	private Thread		m_thread;
	private long		m_lMicroSecondsPerTick;



	public JavaSequencer(MidiDevice.Info info)
	{
		super(info,
		      Arrays.asList(MASTER_SYNC_MODES),
		      Arrays.asList(SLAVE_SYNC_MODES));
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.<init>(): begin"); }
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.<init>(): end"); }
	}



	protected void openImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.openImpl(): begin"); }
		m_thread = new Thread(this);
		m_thread.setPriority(Thread.MAX_PRIORITY);
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.openImpl(): starting thread"); }
		m_thread.start();
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.openImpl(): end"); }
	}



	protected void closeImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.closeImpl(): begin"); }
		if (isRunning())
		{
			stop();
			// TODO: wait for real stopped state
		}
		// now the thread should terminate
		m_thread = null;
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.closeImpl(): end"); }
	}



	protected void startImpl()
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.startImpl(): begin"); }
		// TODO: move to base class?
		if (! isOpen())
		{
			// TODO: throw some exception
		}
		if (getSequence() == null)
		{
			// TODO: throw some exception
		}

		synchronized (this)
		{
			this.notifyAll();
		}
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.startImpl(): end"); }
	}

// currently not needed
//
// 	protected void stopImpl()
// 	{
// 		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.stopImpl(): begin"); }
// 		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.stopImpl(): end"); }
// 	}



	public void run()
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): begin"); }
		while (isOpen())
		{
			synchronized (this)
			{
				while (! isRunning())
				{
					if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): waiting to become running"); }
					try
					{
						this.wait();
					}
					catch (InterruptedException e)
					{
						if (TDebug.TraceAllExceptions) { TDebug.out(e); }
					}
				}
			}
			if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): now running"); }
			Sequence	sequence = getSequence();
			Track[]	aTracks = sequence.getTracks();
			int[]	anTrackPositions = new int[aTracks.length];
			for (int i = 0; i < aTracks.length; i++)
			{
				anTrackPositions[i] = 0;
			}
			//NOTE: all time calculations are done in microseconds
			long	lStartTime = System.currentTimeMillis() * 1000;
			// Track	track = getSequence().getTracks()[0];
			// int	nIndex = 0;
			// ByteArrayOutputStream	baos = new ByteArrayOutputStream();
		// this is used to get a useful time value for the end of track message
			long	lHighestTime = 0;
			while (isRunning())
			{
				// searching for the next event
				boolean		bTrackPresent = false;
				long		lBestTick = Long.MAX_VALUE;
				int		nBestTrack = -1;
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
					MetaMessage	metaMessage = new MetaMessage();
					try
					{
						metaMessage.setMessage(0x2F, new byte[0], 0);
					}
					catch (InvalidMidiDataException e)
					{
						if (TDebug.TraceAllExceptions) { TDebug.out(e); }
					}
					if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): sending End of Track message with time (micros) " + (lHighestTime + 1)); }
					// TODO: calulate µs
					deliverEvent(metaMessage, lHighestTime + 1);
					break;
				}
				MidiEvent	event = aTracks[nBestTrack].get(anTrackPositions[nBestTrack]);
				anTrackPositions[nBestTrack]++;
				MidiMessage	message = event.getMessage();
				long lScheduledTime = event.getTick() * m_lMicroSecondsPerTick + lStartTime;
				lHighestTime = Math.max(lHighestTime, lScheduledTime);
				if (message instanceof MetaMessage && ((MetaMessage) message).getType() == 0x2F)
				{
					if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): ignoring End of Track message with time " + lScheduledTime); }
				}
				else
				{
					deliverEvent(message, lScheduledTime);
				}
			} // while (isRuning())

// 			byte[]	abDifferences = baos.toByteArray();
// 			for (int i = 0; i < abDifferences.length; i++)
// 			{
// 				System.out.print(" " + abDifferences[i]);
// 			}
		} // while (isOpen())
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.run(): end"); }
	}



//////////////////////////////////////////////////////////////////////////
// OLD METHOD for event delivery
//
// 				long	lDeltaTick = 0;
// 				if (nIndex > 0)
// 				{
// 					MidiEvent	previous = track.get(nIndex - 1);
// 					lDeltaTick = event.getTick() - previous.getTick();
// 				}
// 				else
// 				{
// 					lDeltaTick = 0;
// 				}
// 				long	lMillisToWait = (long) (lDeltaTick * pause);
// 				if (lMillisToWait > 0)
// 				{
// 					try
// 					{
// 						Thread.sleep(lMillisToWait);
// 					}
// 					catch (InterruptedException e)
// 					{
// 						// e.printStackTrace();
// 					}
// 				}
//////////////////////////////////////////////////////////////////////////



	// lScheduledTime is in microseconds
	private void deliverEvent(MidiMessage message, long lScheduledTime)
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.deliverEvent(): begin"); }
		while (System.currentTimeMillis() * 1000 < lScheduledTime)
		{
			try
			{
				Thread.sleep(0);
			}
			catch (InterruptedException e)
			{
				if (TDebug.TraceAllExceptions) { TDebug.out(e); }
			}
		}

		if (message instanceof MetaMessage)
		{
			MetaMessage	metaMessage = (MetaMessage) message;
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

		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.deliverEvent(): sending message: " + message + " at: " + lScheduledTime); }
		// sendImpl(message, event.getTick());
		sendImpl(message, -1);
		notifyListeners(message);
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.deliverEvent(): end"); }
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



	public boolean getTrackMute(int nTrack)
	{
		// TODO:
		return false;
	}



	public void setTrackMute(int nTrack, boolean bMute)
	{
		// TODO:
	}


	protected void setMasterSyncModeImpl(SyncMode syncMode)
	{
		// TODO:
	}



	protected void setSlaveSyncModeImpl(SyncMode syncMode)
	{
		// TODO:
	}



	public void setMicrosecondPosition(long lPosition)
	{
	}



	public void setTickPosition(long lPosition)
	{
	}



	public long getTickPosition()
	{
		return 0L;
	}



	public void recordDisable(Track track)
	{
	}



	public void recordEnable(Track track)
	{
	}



	public void recordEnable(Track track, int nChannel)
	{
	}



	public boolean isRecording()
	{
		return false;
	}



	public void stopRecording()
	{
	}



	public void startRecording()
	{
	}



	protected void setTempoImpl(float fMPQ)
	{
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.setTempoImpl(): begin"); }
		int	nResolution = getResolution();
		m_lMicroSecondsPerTick = (long) fMPQ / nResolution;
		if (TDebug.TraceSequencer) { TDebug.out("JavaSequencer.setTempoImpl(): end"); }
	}



	protected float getTempoImpl()
	{
		return 0.0F;
	}
}



/*** JavaSequencer.java ***/

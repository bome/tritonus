/*
 *	MshSequencer.java
 */

/*
 *   Copyright © Grame 2000 for the Tritonus project by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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
 *  Grame Research Laboratory, 9, rue du Garet 69001 Lyon - France
 *  grame@grame.fr
 *
 */


package	org.tritonus.midi.device.midishare;

import	javax.sound.midi.Sequence;
import	javax.sound.midi.Sequencer;
import	javax.sound.midi.Track;
import	javax.sound.midi.MidiEvent;
import	javax.sound.midi.MidiMessage;
import	javax.sound.midi.MetaMessage;
import	javax.sound.midi.Transmitter;
import	javax.sound.midi.Receiver;
import	javax.sound.midi.MidiUnavailableException;
import	javax.sound.midi.InvalidMidiDataException;
import	javax.sound.midi.MidiDevice;
import	javax.sound.midi.MidiSystem;


import	org.tritonus.TDebug;
import	org.tritonus.midi.device.TSequencer;
import	org.tritonus.midi.device.midishare.*;


import	grame.midishare.*;
import	grame.midishare.player.*;
import	grame.midishare.tools.*;

final public class MshSequencer
	extends		TSequencer
{

	private float		m_fTempoInMPQ;
	private float		m_fTempoFactor;
	
	private int 		m_refNum  = -1; 	// Player renum
	private PlayerState 	m_state = null;		// Player state
	private int 		m_recTrack = -1;	// Track in record mode
	

	public MshSequencer(MidiDevice.Info info)
	{
		super(info);
		m_fTempoInMPQ = 500000;
	}

	
	protected void openImpl() throws MidiUnavailableException
	{
		try {
		
			if (Midi.Share() == 0)  throw  new MidiUnavailableException("MidiShare not installed");
			
			if (m_refNum == -1) {
				// Opens the MidiShare native sequencer
			 	m_refNum = MidiPlayer.Open(availableName("JavaSound Player"));
			 	if (m_refNum < 0)  throw  new MidiUnavailableException("MidiShare MidiOpen error");
			 	
			 	// To be changed later
			 	Midi.Connect(0,m_refNum,1);
			 	Midi.Connect(m_refNum,0,1);
			 	
			 	m_state = new PlayerState();
				//MidiPlayer.SetSynchroIn(m_refNum, MidiPlayer.kClockSync);
			}
		
		}catch(MidiUnavailableException e) {
		 	throw  e;
		}catch(UnsatisfiedLinkError e1) { 
			throw  new MidiUnavailableException("MidiShare native JPlayer library not installed");
		}catch(Error e2) { 
			throw  new MidiUnavailableException("MidiShare sequencer open error");
		}
	}

	protected void closeImpl()
	{
		if (m_refNum  > 0 ) {
			MidiPlayer.Stop(m_refNum);
			MidiPlayer.Close(m_refNum);
			m_refNum = -1;
		}
	}
	

	public void setSequence(Sequence sequence)
		throws	InvalidMidiDataException
	{
		// TODO: what if playing is in progress?
		//m_sequence = sequence;
		
		// yes, this is required by the specification
		setTempoFactor(1.0F);
		
		Track[]	aTracks = sequence.getTracks();
		int mshEv, mshSeq, mshSeq1, nTrack, nEv, eventNum = 0; 
		
		// Count the event number, allocate MidiShare events if necessary
		for (nTrack = 0; nTrack < aTracks.length; nTrack++){ eventNum+=aTracks[nTrack].size();}
		if (Midi.FreeSpace() < eventNum) Midi.GrowSpace(eventNum);
		
		if (((mshSeq = Midi.NewSeq()) != 0) && ((mshSeq1 = Midi.NewSeq()) != 0)) {
			for (nTrack = 0; nTrack < aTracks.length; nTrack++){
				for (nEv = 0; nEv<aTracks[nTrack].size() ; nEv++) {
					
					try{
						mshEv = MshEventConverter.decodeEvent(aTracks[nTrack].get(nEv));
						Midi.SetRefnum(mshEv,IsTempoMap(Midi.GetType(mshEv)) ? 0 : Math.min(256,nTrack+1));
						Midi.AddSeq(mshSeq1,mshEv);
			
					}catch (InvalidMidiDataException e) {  	// MidiEvent that can not be converted
					}catch (MidiException e) {		// MidiShare allocation error
						Midi.FreeSeq(mshSeq);	
						Midi.FreeSeq(mshSeq1);
						throw new InvalidMidiDataException("No more MidiShare events");	
					}
					
				}
				// Mix the temporary sequence in the result sequence
				MidiSequence.Mix(mshSeq1,mshSeq);
				Midi.SetFirstEv(mshSeq1, 0);
				Midi.SetLastEv(mshSeq1, 0);
			}
			int res = MidiPlayer.SetAllTrack(m_refNum,mshSeq,sequence.getResolution());
			if (res != MidiPlayer.PLAYERnoErr) throw new InvalidMidiDataException("Can not setSequence to native sequencer");
		}else {
			throw new InvalidMidiDataException("No more MidiShare events");
		}
	}
	
	
	public void start() {
		//MidiPlayer.Start(m_refNum);
		MidiPlayer.Cont(m_refNum);
	}

	public void stop()
	{
		MidiPlayer.Stop(m_refNum);
		MidiPlayer.Record(m_refNum, MidiPlayer.kNoTrack);
	}

	public boolean isRunning()
	{
		MidiPlayer.GetState(m_refNum, m_state);
		return (m_state.state == MidiPlayer.kPlaying || m_state.state == MidiPlayer.kRecording);
	}

	public void startRecording()
	{
		MidiPlayer.Record(m_refNum, m_recTrack);
		MidiPlayer.Cont(m_refNum);
	}

	public void stopRecording()
	{
		// TODO:
		MidiPlayer.Record(m_refNum, MidiPlayer.kNoTrack);
	}

	public boolean isRecording()
	{
		MidiPlayer.GetState(m_refNum, m_state);
		return (m_state.state == MidiPlayer.kRecording);
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

	public float getTempoInBPM()
	{	
		return 6.0E7F / getTempoInMPQ();
	}


	public void setTempoInBPM(float fBPM)
	{
		setTempoInMPQ(6.0E7F / fBPM);
	}

	public float getTempoInMPQ()
	{
		MidiPlayer.GetState(m_refNum, m_state);
		m_fTempoInMPQ = m_state.tempo;
		return m_fTempoInMPQ;
	}

	public void setTempoInMPQ(float fMPQ)
	{
		m_fTempoInMPQ = fMPQ;
		setTempoImpl();
	}


	public float getTempoFactor()
	{
		return m_fTempoFactor;
	}

	public void setTempoFactor(float fFactor)
	{
		m_fTempoFactor = fFactor;
		setTempoImpl();
	}

	private void setTempoImpl()
	{
		float	fRealTempo = getTempoInMPQ() * getTempoFactor();
		/*
		if (TDebug.TraceMidiShareSequencer)
		{
			TDebug.out("MidiShareSequencer.setTempoImpl(): real tempo: " + fRealTempo);
		}
		*/
		MidiPlayer.SetTempo(m_refNum,(int)fRealTempo);
	}

	public long getTickLength()
	{
		// TODO:
		MidiPlayer.GetEndScore(m_refNum, m_state);
		return 0;
	}

	public long getTickPosition()
	{
		// TODO:
		MidiPlayer.GetState(m_refNum, m_state);
		return 0;
	}

	public void setTickPosition(long lTick)
	{
		// TODO:
		//MidiPlayer.SetPosTicks(m_refNum, lTick);
		MidiPlayer.SetPosMs(m_refNum, 0); // To be changed
	}

	public long getMicrosecondLength()
	{
		MidiPlayer.GetEndScore(m_refNum, m_state);
		return m_state.date*1000;
	}

	public long getMicrosecondPosition()
	{
		MidiPlayer.GetState(m_refNum, m_state);
		return m_state.date*1000;
	}

	public void setMicrosecondPosition(long lMicroseconds)
	{
		MidiPlayer.SetPosMs(m_refNum, (int)lMicroseconds/1000);
	}
	

	public Sequencer.SyncMode getMasterSyncMode()
	{
		MidiPlayer.GetState(m_refNum, m_state);
		return MshSync2SequencerSync(m_state.syncin);
	}


	public void setMasterSyncMode(Sequencer.SyncMode syncMode)
	{
		MidiPlayer.SetSynchroIn(m_refNum,Sequencer2MshSync(syncMode));
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
		//MidiPlayer.SetSynchroOut(refnum, sync);
	}


	public Sequencer.SyncMode[] getSlaveSyncModes()
	{
		// TODO:
		return new Sequencer.SyncMode[0];
	}

	public boolean getTrackMute(int nTrack)
	{
		// TODO: VERIFIER numero de piste
		return  (MidiPlayer.GetParam(m_refNum,nTrack, MidiPlayer.kMute) == MidiPlayer.kMuteOn);
	}

	public void setTrackMute(int nTrack, boolean bMute)
	{
		// TODO: VERIFIER numero de piste
		MidiPlayer.SetParam(m_refNum,nTrack,  MidiPlayer.kMute ,  (bMute) ? MidiPlayer.kMuteOn : MidiPlayer.kMuteOff);
	}

	public boolean getTrackSolo(int nTrack)
	{
		// TODO: VERIFIER numero de piste
		return  (MidiPlayer.GetParam(m_refNum,nTrack, MidiPlayer.kSolo) == MidiPlayer.kSoloOn);
	}

	public void setTrackSolo(int nTrack, boolean bSolo)
	{
		// TODO: VERIFIER numero de piste
		MidiPlayer.SetParam(m_refNum,nTrack,  MidiPlayer.kSolo ,  (bSolo) ? MidiPlayer.kSoloOn : MidiPlayer.kSoloOff);
	}


	// Private methods

	private Sequencer.SyncMode MshSync2SequencerSync(int sync) 
	{
		switch (sync) {
			case MidiPlayer.kInternalSync:
				return  Sequencer.SyncMode.INTERNAL_CLOCK;
			case MidiPlayer.kClockSync:
				return  Sequencer.SyncMode.MIDI_SYNC;
			case MidiPlayer.kSMPTESync:
				return  Sequencer.SyncMode.MIDI_TIME_CODE;
		}
		return null;
	}
	
	private int Sequencer2MshSync( Sequencer.SyncMode sync) 
	{
		if (sync.equals(Sequencer.SyncMode.INTERNAL_CLOCK))
			return MidiPlayer.kInternalSync;
		else if (sync.equals(Sequencer.SyncMode.MIDI_SYNC))
			return MidiPlayer.kClockSync;
		else if (sync.equals(Sequencer.SyncMode.MIDI_TIME_CODE))
			return MidiPlayer.kSMPTESync;
		else 
			return MidiPlayer.kInternalSync;	
	}
	
	private static	String availableName(String name) {
		int num = Midi.CountAppls();
		for (int i = 0 ; i< num; i++) {
			if (Midi.GetNamedAppl(name+(i+1)) < 0) return name + (i+1);
		}
		return name + num;
	}
	
	private boolean  IsTempoMap(int t){return (t == Midi.typeCopyright) 
  						|| (t== Midi.typeMarker) 
  						|| ((t >=Midi.typeTempo) && (t<=Midi.typeKeySign)); }
 



	///////////////////////////////////////////////////


	public Transmitter getTransmitter()
		throws	MidiUnavailableException
	{
		// TODO: check number
		return new MidiShareTransmitter();
	}


/////////////////// INNER CLASSES //////////////////////////////////////

	private class MidiShareTransmitter
		extends		TTransmitter
	{
		private boolean		m_bReceiverSubscribed;



		public MidiShareTransmitter()
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
			 	/*
			if (receiver instanceof AlsaSequencerReceiver)
			{
				TDebug.out("AlsaSequencer.AlsaTransmitter.setReceiver(): trying to establish subscription");
				m_bReceiverSubscribed = ((AlsaSequencerReceiver) receiver).subscribeTo(getDataClient(), getDataPort());
				TDebug.out("AlsaSequencer.AlsaTransmitter.setReceiver(): subscription established: " + m_bReceiverSubscribed);
			}
			*/
		}


		public void send(MidiMessage message, long lTimeStamp)
		{
			/*
			 *	Send message via Java methods only if not
			 *	subscription was established. If there is a
			 *	subscription, the message is routed with MidiShare
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
	
	protected  float getTempoNative() {return 0;}
	
	protected  void setTempoNative(float fMPQ){}


}



/*** MidiShareSequencer.java ***/

/*
 *	Sequencer.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 - 2004 by Matthias Pfisterer
 *
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.midi;

import java.io.InputStream;
import java.io.IOException;


public interface Sequencer
extends MidiDevice
{
	public static final int LOOP_CONTINUOUSLY = -1;


	public void setSequence(Sequence sequence)
		throws InvalidMidiDataException;




	public void setSequence(InputStream inputStream)
		throws InvalidMidiDataException, IOException;



	public Sequence getSequence();


	public void start();




	public void setLoopStartPoint(long lTick);


	public long getLoopStartPoint();


	public void setLoopEndPoint(long lTick);


	public long getLoopEndPoint();


	public void setLoopCount(int nLoopCount);

	public int getLoopCount();



	public void stop();


	public boolean isRunning();

	public void startRecording();

	public void stopRecording();

	public boolean isRecording();

	// name should be: enableRecording
	public void recordEnable(Track track, int nChannel);

	// name should be: disableRecording
	public void recordDisable(Track track);


	public float getTempoInBPM();

	public void setTempoInBPM(float fBPM);


	public float getTempoInMPQ();

	public void setTempoInMPQ(float fMPQ);


	public float getTempoFactor();

	public void setTempoFactor(float fFactor);


	public long getTickLength();

	public long getTickPosition();

	public void setTickPosition(long lTick);
    

	public long getMicrosecondLength();

	public long getMicrosecondPosition();

	public void setMicrosecondPosition(long lMicroseconds);


	public Sequencer.SyncMode getMasterSyncMode();

	public void setMasterSyncMode(Sequencer.SyncMode syncMode);

	public Sequencer.SyncMode[] getMasterSyncModes();


	public Sequencer.SyncMode getSlaveSyncMode();

	public void setSlaveSyncMode(Sequencer.SyncMode syncMode);

	public Sequencer.SyncMode[] getSlaveSyncModes();


	public void setTrackMute(int nTrack, boolean bMute);

	public boolean getTrackMute(int nTrack);

	public void setTrackSolo(int nTrack, boolean bSolo);

	public boolean getTrackSolo(int nTrack);


	public boolean addMetaEventListener(MetaEventListener listener);

	public void removeMetaEventListener(MetaEventListener listener);


	public int[] addControllerEventListener(ControllerEventListener listener, int[] anControllers);
	public int[] removeControllerEventListener(ControllerEventListener listener, int[] anControllers);


////////////////////////// INNER CLASSES //////////////////////////////

	public static class SyncMode
	{
		public static final SyncMode	INTERNAL_CLOCK = new SyncMode("Internal Clock");
		public static final SyncMode	MIDI_SYNC = new SyncMode("MIDI Sync");
		public static final SyncMode	MIDI_TIME_CODE = new SyncMode("MIDI Time Code");
		public static final SyncMode	NO_SYNC = new SyncMode("No Timing");


		private String		m_strName;




		protected SyncMode(String strName)
		{
			m_strName = strName;
		}



		public final boolean equals(Object obj)
		{
			return super.equals(obj);
		}



		public final int hashCode()
		{
			return super.hashCode();
		}



		public final String toString()
		{
			return super.toString() + "[name=" + m_strName + "]";
		}



	}
}



/*** Sequencer.java ***/

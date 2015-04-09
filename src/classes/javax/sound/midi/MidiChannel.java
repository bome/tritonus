/*
 *	MidiChannel.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer
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



public interface MidiChannel
{
	public void noteOn(int nNoteNumber, int nVelocity);
	public void noteOff(int nNoteNumber, int nVelocity);
	public void noteOff(int nNoteNumber);
	public void setPolyPressure(int nNoteNumber, int nPressure);
	public int getPolyPressure(int nNoteNumber);
	public void setChannelPressure(int nPressure);
	public int getChannelPressure();
	public void controlChange(int nController, int nValue);
	public int getController(int nController);
	public void programChange(int nProgram);
	public void programChange(int nBank, int nProgram);
	public int getProgram();
	public void setPitchBend(int nBend);
	public int getPitchBend();
	public void resetAllControllers();
	public void allNotesOff();
	public void allSoundOff();
	public boolean localControl(boolean bOn);
	public void setMono(boolean bMono);
	public boolean getMono();
	public void setOmni(boolean bOmni);
	public boolean getOmni();
	public void setMute(boolean bMute);
	public boolean getMute();
	public void setSolo(boolean bSolo);
	public boolean getSolo();
}



/*** MidiChannel.java ***/

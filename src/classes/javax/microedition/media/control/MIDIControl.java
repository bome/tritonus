/*
 *	MIDIControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;


/**	TODO:
*/
public interface MIDIControl
extends Control
{
	/**	TODO:
	*/
	public static final int		NOTE_ON = 0x90;


	/**	TODO:
	*/
	public static final int		CONTROL_CHANGE = 0xB0;


	/**	TODO:
	*/
	public boolean isBankQuerySupported();


	/**	TODO:
	*/
	public int[] getProgram(int nChannel)
		throws MediaException;


	/**	TODO:
	*/
	public int getChannelVolume(int nChannel);


	/**	TODO:
	*/
	public void setProgram(int nChannel,
			       int nBank,
			       int nProgram);


	/**	TODO:
	*/
	public void setChannelVolume(int nChannel,
				     int nVolume);


	/**	TODO:
	*/
	public int[] getBankList(boolean bCustom)
		throws MediaException;


	/**	TODO:
	*/
	public int[] getProgramList(int nBank)
		throws MediaException;


	/**	TODO:
	*/
	public String getProgramName(int nBank,
				     int nProgram)
		throws MediaException;


	/**	TODO:
	*/
	public String getKeyName(int nBank,
				 int nProgram,
				 int nKey)
		throws MediaException;


	/**	TODO:
	*/
	public void shortMidiEvent(int nType,
				 int nData1,
				 int nData2);


	/**	TODO:
	*/
	public void longMidiEvent(byte[] abData,
				 int nOffset,
				 int nLength);
}



/*** MIDIControl.java ***/

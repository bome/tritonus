/*
 *	ToneGenerator.java
 */

package	org.tritonus.mmapi;

import javax.microedition.media.MediaException;


/**	TODO:
*/
public interface ToneGenerator
{
	public void playTone(int nNote,
			     int nDuration,
			     int nVolume)
		throws MediaException;
}



/*** ToneGenerator.java ***/

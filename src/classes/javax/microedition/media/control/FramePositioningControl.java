/*
 *	FramePositioningControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.Control;


/**	TODO:
*/
public interface FramePositioningControl
extends Control
{
	/**	TODO:
	*/
	public int seek(int nFrameNumber);


	/**	TODO:
	*/
	public int skip(int nFramesToSkip);


	/**	TODO:
	*/
	public long mapFrameToTime(int nFrameNumber);


	/**	TODO:
	*/
	public int mapTimeToFrame(long lMediaTime);
}



/*** FramePositioningControl.java ***/

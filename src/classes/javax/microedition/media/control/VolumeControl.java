/*
 *	VolumeControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.Control;



/**	TODO:
*/
public interface VolumeControl
extends Control
{
	/**	TODO:
	*/
	public void setMute(boolean bMute);


	/**	TODO:
	*/
	public boolean isMuted();


	/**	TODO:
	*/
	public int setLevel(int nLevel);


	/**	TODO:
	*/
	public int getLevel();
}



/*** VolumeControl.java ***/

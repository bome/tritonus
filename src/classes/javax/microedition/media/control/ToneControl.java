/*
 *	ToneControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.Control;


/**	TODO:
*/
public interface ToneControl
extends Control
{
	/**	TODO:
	*/
	public static final byte	VERSION = -2;


	/**	TODO:
	*/
	public static final byte	TEMPO = -3;


	/**	TODO:
	*/
	public static final byte	RESOLUTION = -4;


	/**	TODO:
	*/
	public static final byte	BLOCK_START = -5;


	/**	TODO:
	*/
	public static final byte	BLOCK_END = -6;


	/**	TODO:
	*/
	public static final byte	PLAY_BLOCK = -7;


	/**	TODO:
	*/
	public static final byte	SET_VOLUME = -8;


	/**	TODO:
	*/
	public static final byte	REPEAT = -9;


	/**	TODO:
	*/
	public static final byte	C4 = 60;


	/**	TODO:
	*/
	public static final byte	SILENCE = -1;


	/**	TODO:
	*/
	public void setSequence(byte[] abSequence);
}



/*** ToneControl.java ***/

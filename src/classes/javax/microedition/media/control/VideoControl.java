/*
 *	VideoControl.java
 */

package	javax.microedition.media.control;

import javax.microedition.media.MediaException;


/**	TODO:
*/
public interface VideoControl
extends GUIControl
{
	/**	TODO:
	*/
	public static final int		USE_DIRECT_VIDEO = 1;


	/**	TODO:
	*/
	public Object initDisplayMode(int nMode, Object arg);


	/**	TODO:
	*/
	public void setDisplayLocation(int nX, int nY);


	/**	TODO:
	*/
	public int getDisplayX();


	/**	TODO:
	*/
	public int getDisplayY();


	/**	TODO:
	*/
	public void setVisible(boolean bVisible);


	/**	TODO:
	*/
	public void setDisplaySize(int nWidth, int nHeight)
		throws MediaException;


	/**	TODO:
	*/
	public void setDisplayFullScreen(boolean bFullScreen)
		throws MediaException;


	/**	TODO:
	*/
	public int getSourceWidth();


	/**	TODO:
	*/
	public int getSourceHeight();


	/**	TODO:
	*/
	public int getDisplayWidth();


	/**	TODO:
	*/
	public int getDisplayHeight();


	/**	TODO:
	*/
	public byte[] getSnapshot(String strImageType)
		throws MediaException;
}



/*** VideoControl.java ***/

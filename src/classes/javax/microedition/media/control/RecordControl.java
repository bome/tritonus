/*
 *	RecordControl.java
 */

package	javax.microedition.media.control;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.media.Control;
import javax.microedition.media.MediaException;



/**	TODO:
*/
public interface RecordControl
extends Control
{
	/**	TODO:
	*/
	public void setRecordStream(OutputStream stream);


	/**	TODO:
	*/
	public void setRecordLocation(String strLocator)
		throws IOException, MediaException;


	/**	TODO:
	*/
	public String getContentType();


	/**	TODO:
	*/
	public void startRecord();


	/**	TODO:
	*/
	public void stopRecord();


	/**	TODO:
	*/
	public void commit()
		throws IOException;


	/**	TODO:
	*/
	public int setRecordSizeLimit(int nSize)
		throws MediaException;


	/**	TODO:
	*/
	public void reset()
		throws IOException;
}



/*** RecordControl.java ***/

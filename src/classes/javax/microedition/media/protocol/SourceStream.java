/*
 *	SourceStream.java
 */

package	javax.microedition.media.protocol;


import	java.io.IOException;
import	javax.microedition.media.Controllable;


/**	TODO:
*/
public interface SourceStream
extends Controllable
{
	/**	TODO:
	*/
	public static final int	NOT_SEEKABLE = 0;


	/**	TODO:
	*/
	public static final int	SEEKABLE_TO_START = 1;


	/**	TODO:
	*/
	public static final int	RANDOM_ACCESSIBLE = 2;




	/**	TODO:
	*/
	public ContentDescriptor getContentDescriptor();


	/**	TODO:
	*/
	public long getContentLength();


	/**	TODO:
	*/
	public int read(byte[] abData, int nOffset, int nLength)
		throws IOException;


	/**	TODO:
	*/
	public int getTransferSize();


	/**	TODO:
	*/
	public long seek(long lPosition)
		throws IOException;


	/**	TODO:
	*/
	public long tell();


	/**	TODO:
	*/
	public int getSeekType();
}



/*** SourceStream.java ***/

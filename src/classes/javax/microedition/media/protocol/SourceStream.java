/*
 *	SourceStream.java
 */

package	javax.microedition.media.protocol;


import	java.io.IOException;
import	javax.microedition.media.Controllable;



public interface SourceStream
extends Controllable
{
	public static final int	NOT_SEEKABLE = 0;
	public static final int	SEEKABLE_TO_START = 1;
	public static final int	RANDOM_ACCESSIBLE = 2;


	public ContentDescriptor getContentDescriptor();
	public long getContentLength();
	public int read(byte[] abData, int nOffset, int nLength)
		throws IOException;
	public int getTransferSize();
	public long seek(long lPosition)
		throws IOException;
	public long tell();
	public int getSeekType();
}



/*** SourceStream.java ***/

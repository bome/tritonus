/*
 *	DataSource.java
 */

package	javax.microedition.media.protocol;


import	java.io.IOException;
import	javax.microedition.media.Controllable;



public abstract class DataSource
implements Controllable
{
	private String		m_strLocator;



	public DataSource(String strLocator)
	{
		m_strLocator = strLocator;
	}



	public String getLocator()
	{
		return m_strLocator;
	}



	public abstract String getContentType();


	public abstract void connect()
		throws IOException;


	public abstract void disconnect();


	public abstract void start();


	public abstract void stop();


	public abstract SourceStream[] getStreams();
}



/*** DataSource.java ***/

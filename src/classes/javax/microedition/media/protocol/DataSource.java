/*
 *	DataSource.java
 */

package	javax.microedition.media.protocol;


import	java.io.IOException;
import	javax.microedition.media.Controllable;



public abstract class DataSource
implements Controllable
{
	// TODO: check if final makes sense
	private /*final*/ String		m_strLocator = null;



	public DataSource()
	{
	}


	// TODO: in spec the parameter is called 'source'. Suggest renaming for clearity.
	public DataSource(String strLocator)
	{
		setLocator(strLocator);
	}



	public void setLocator(String strLocator)
	{
		// TODO: check if a RuntimeException is thrown  anyway on an attempt to set a final variable twice.
		if (getLocator() != null)
		{
			throw new RuntimeException("locator has already been set");
		}
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

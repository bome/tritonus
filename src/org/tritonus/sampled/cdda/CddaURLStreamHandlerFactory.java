/*
 *	CddaURLStreamHandlerFactory.java
 */

package	org.tritonus.sampled.cdda;


import	java.net.URLStreamHandler;
import	java.net.URLStreamHandlerFactory;



public class CddaURLStreamHandlerFactory
	implements	URLStreamHandlerFactory
{
	private static URLStreamHandler	m_cddaHandler = null;



	public URLStreamHandler createURLStreamHandler(String strProtocol)
	{
		URLStreamHandler	handler = null;

		if (strProtocol.equals("cdda"))
		{
			if (m_cddaHandler == null)
			{
				m_cddaHandler = new CddaStreamHandler();
			}
			handler = m_cddaHandler;
		}
		return handler;
	}
}



/*** CddaURLStreamHandlerFactory.java ****/

/*
 *	JMFDataSourceTest.java
 */

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.media.MediaLocator;
import javax.media.Manager;
import javax.media.protocol.DataSource;



public class JMFDataSourceTest
{
	public static void main(String[] astrArgs)
		throws Exception
	{
		String	strLocator = astrArgs[0];
		URL		url = new URL(strLocator);

		MediaLocator	locator = new MediaLocator(strLocator);
		DataSource	source = Manager.createDataSource(locator);
		String		strContentType = source.getContentType();
		System.out.println("DataSource (ML) content type: " + strContentType);

		locator = new MediaLocator(url);
		source = Manager.createDataSource(locator);
		strContentType = source.getContentType();
		System.out.println("DataSource (URL) content type: " + strContentType);

		URLConnection	connection = url.openConnection();
		System.out.println("URLConnection: " + connection);
		strContentType = connection.getContentType();
		System.out.println("URL content type: " + strContentType);
	}
}



/*** JMFDataSourceTest.java ***/

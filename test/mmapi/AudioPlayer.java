/*
 *	AudioPlayer.java
 */

import	java.io.IOException;
import	javax.microedition.media.Manager;
import	javax.microedition.media.MediaException;
import	javax.microedition.media.Player;
import	javax.microedition.media.protocol.DataSource;



public class AudioPlayer
{
	public static void main(String[] args)
	{
		String	strLocator = args[0];
		try
		{
		DataSource	dataSource = Manager.createDataSource(strLocator);
		System.out.println("data source: " + dataSource);
		dataSource.connect();
		dataSource.start();
		Player	player = Manager.createPlayer(dataSource);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (MediaException e)
		{
			e.printStackTrace();
		}
	}
}



/*** AudioPlayer.java ***/

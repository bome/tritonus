/*
 *	Clients.java
 */

import	java.util.Iterator;

import	org.tritonus.lowlevel.alsa.ASequencer;
import	org.tritonus.TDebug;



public class Clients
{
	public static void main(String[] args)
	{
		ASequencer	seq = new ASequencer();
		Iterator	clients = seq.getClientInfos();
		// TDebug.out("" + clients);
		while (clients.hasNext())
		{
			ASequencer.ClientInfo	clientInfo = (ASequencer.ClientInfo) clients.next();
			TDebug.out("client: " + clientInfo.getClientId());
			Iterator	ports = seq.getPortInfos(clientInfo.getClientId());
			// TDebug.out("" + clients);
			while (ports.hasNext())
			{
				ASequencer.PortInfo	portInfo = (ASequencer.PortInfo) ports.next();
				TDebug.out("port: " + portInfo.getPort());
			}
		}
	}
}



/*** Clients.java ***/

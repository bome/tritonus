/*
 *	ASequencerTest.java
 */

import	org.gnu.tritonus.lowlevel.alsa.ASequencer;


public class ASequencerTest
{
	public static void main(String[] args)
	{
		ASequencer	seq = new ASequencer();
		System.out.println("Client ID: " + seq.getClientId());
		ASequencer.SystemInfo	systemInfo = seq.getSystemInfo();
		System.out.println("Max. queues: " + systemInfo.getMaxQueues());
		System.out.println("Max. clients: " + systemInfo.getMaxClients());
		System.out.println("Max. ports per client: " + systemInfo.getMaxPortsPerClient());
		System.out.println("Max. channels per port: " + systemInfo.getMaxChannelsPerPort());
		ASequencer.ClientInfo	clientInfo = seq.getClientInfo();
		outputClientInfo(clientInfo);
		for (int nClient = 0; nClient < systemInfo.getMaxClients(); nClient++)
		{
			ASequencer.ClientInfo	clientInfo2 = seq.getClientInfo(nClient);
			if (clientInfo2 != null)
			{
				System.out.println("-----------------------------------------------");
				outputClientInfo(clientInfo2);
			}
		}
		seq.sendNoteOnEvent(0, 0, 61, 30);
		seq.sendNoteOnEvent(1000, 0, 61, 20);
		seq.startTimer();
		try
		{
			Thread.sleep(10000);
		}
		catch (InterruptedException e)
		{
		}
		// seq.stopTimer();
		seq.close();
	}



	private static void outputClientInfo(ASequencer.ClientInfo clientInfo)
	{
		System.out.println("Client id: " + clientInfo.getClientId());
		System.out.println("Client type: " + clientInfo.getClientType());
		System.out.println("Client name: " + clientInfo.getName());
/*
		System.out.println("Client id: " + clientInfo.getClientId());
		System.out.println("Client id: " + clientInfo.getClientId());
		System.out.println("Client id: " + clientInfo.getClientId());
*/
	}
}

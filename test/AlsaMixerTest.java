/*
  For testing the ALSA mixer low-level.
 */

import	org.tritonus.lowlevel.alsa.AlsaMixer;


public class AlsaMixerTest
{
	public static void main(String[] args)
		throws Exception
	{
		AlsaMixer.setTrace(true);
		String	strMixerName = "hw:0";
		if (args.length > 0)
		{
			strMixerName = args[0];
		}
		System.out.println("Mixer: " + strMixerName);
		AlsaMixer	mixer = new AlsaMixer(strMixerName);
		int[]		anIndices = new int[200];
		String[]	astrNames = new String[200];
		int	nReturn = mixer.readControlList(anIndices, astrNames);
		System.out.println("readControlList() returns: " + nReturn);
		if (nReturn > 0)
		{
			System.out.println("Mixer controls:");
			for (int i = 0; i < nReturn; i++)
			{
				System.out.println("" + i + " " + anIndices[i] + " " + astrNames[i]);
			}
		}
		mixer.close();
	}
}

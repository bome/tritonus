/*
  For testing the ALSA mixer low-level.
 */

import	org.tritonus.lowlevel.alsa.AlsaMixer;
import	org.tritonus.lowlevel.alsa.AlsaMixerElement;


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
				AlsaMixerElement	element = new AlsaMixerElement(mixer, anIndices[i], astrNames[i]);
				output(element);
			}
		}
		mixer.close();
	}



	private static void output(AlsaMixerElement element)
	{
		System.out.println("  name: " + element.getName());
		System.out.println("  index: " + element.getName());
		System.out.println("  playback mono: " + element.isPlaybackMono());
		for (int nChannel = AlsaMixerElement.SND_MIXER_SCHN_FRONT_LEFT;
		     nChannel <= AlsaMixerElement.SND_MIXER_SCHN_WOOFER;
		     nChannel++)
		{
			System.out.println("  playback channel (" + AlsaMixerElement.getChannelName(nChannel) + "): " + element.hasPlaybackChannel(nChannel));
		}
		System.out.println("  capture mono: " + element.isCaptureMono());
		// System.out.println("  capture channel:" + element.hasCaptureChannel());
		System.out.println("  common volume: " + element.hasCommonVolume());
		System.out.println("  playback volume: " + element.hasPlaybackVolume());
		System.out.println("  playback volume joined: " + element.hasPlaybackVolumeJoined());
		System.out.println("  capture volume: " + element.hasCaptureVolume());
		System.out.println("  capture volume joined: " + element.hasCaptureVolumeJoined());
		System.out.println("  common switch: " + element.hasCommonSwitch());
		System.out.println("  playback switch: " + element.hasPlaybackSwitch());
		System.out.println("  playback switch joined: " + element.hasPlaybackSwitchJoined());
		System.out.println("  capture switch: " + element.hasCaptureSwitch());
		System.out.println("  capture switch joined: " + element.hasCaptureSwitchJoinded());
		System.out.println("  capture switch exclusive: " + element.hasCaptureSwitchExclusive());
		if (element.hasCaptureSwitchExclusive())
		{
			System.out.println("  capture group: " + element.getCaptureGroup());
		}
/*
		System.out.println("  : " + element.get());
		System.out.println("  : " + element.get());
		System.out.println("  : " + element.get());
		System.out.println("  : " + element.get());
		System.out.println("  : " + element.get());
		System.out.println("  : " + element.get());
*/
	}
}

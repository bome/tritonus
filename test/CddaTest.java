/*
 *	CddaTest.java
 */

/*
  programs related to cdda:
  command-line extractor
  cd player
  extractor/mp3 encoder
 */

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.LineUnavailableException;

import	org.tritonus.lowlevel.cdda.CDDA;
import	org.tritonus.lowlevel.cdda.cooked_ioctl.CookedIoctl;


public class CddaTest
{
	public static void main(String[] args)
	{
		boolean		bTocOnly = true;
		int		nTrack = 0;
		if (args.length < 1)
		{
			bTocOnly = true;
		}
		else if (args.length == 1)
		{
			nTrack = Integer.parseInt(args[0]);
			bTocOnly = false;
		}
		CDDA	cdda = new CookedIoctl();
		int[]	anValues = new int[2];
		int[]	anStart = new int[100];
		int[]	anType = new int[100];
		cdda.readTOC(anValues, anStart, anType);
		System.out.println("First track: " + anValues[0]);
		System.out.println("last track: " + anValues[1]);
		int	nTracks = anValues[1] - anValues[0] + 1;
		for (int i = 0; i < nTracks; i++)
		{
			System.out.println("Track " + (i + anValues[0]) + " start: " + anStart[i]);
			System.out.println("Track " + (i + anValues[0]) + " type: " + anType[i]);
			System.out.println("Track " + (i + anValues[0]) + " length (s): " + (anStart[i + 1] - anStart[i])/75);
		}
		if (! bTocOnly)
		{
			SourceDataLine	line = null;
			AudioFormat	audioFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				44100.0F, 16, 2, 4, 44100.0F, false);
			Line.Info	info = new DataLine.Info(SourceDataLine.class, audioFormat);
			byte[]	abData = new byte[2352 * 8];
			try
			{
				line = (SourceDataLine) AudioSystem.getLine(info);
				line.open();
				line.start();
			}
			catch (LineUnavailableException e)
			{
				e.printStackTrace();
			}
			for (int i = anStart[nTrack - 1]; i < anStart[nTrack ]; i++)
			{
				cdda.readFrame(i, 1, abData);
				line.write(abData, 0, 2352);
			}
		}
		cdda.close();
	}
}


/*** CddaTest.java ****/

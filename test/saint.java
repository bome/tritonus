/*
 *	saint.java
 *
 *	Standalone program to demonstrate the usage of the Saint class.
 */

import	java.io.InputStream;
import	java.io.IOException;
import	java.io.FileInputStream;
import	java.io.FileOutputStream;
import	java.io.OutputStream;

import	gnu.getopt.Getopt;

import	org.tritonus.lowlevel.saint.Saint;



public class saint
{
	public static void main(String[] args)
		throws	IOException
	{
		InputStream	bitstream = null;
		InputStream	orchestra = null;
		InputStream	score = null;
		OutputStream	output = null;
		int		nOutputFormat = 10;	// s32l
		int	c;

		// TODO: what is this option "-prob" (real time stream simulation)
		// TODO: option for audio input files
		Getopt	g = new Getopt("saint", args, "hVb:c:s:o:f:");
		while ((c = g.getopt()) != -1)
		{
			switch (c)
			{
			case 'h':
				printUsageAndExit();

			case 'V':
				printVersionAndExit();

			case 'b':
				bitstream = new FileInputStream(g.getOptarg());
				break;

			case 'c':
				orchestra = new FileInputStream(g.getOptarg());
				break;

			case 's':
				score = new FileInputStream(g.getOptarg());
				break;

			case 'o':
				output = new FileOutputStream(g.getOptarg());
				break;

			case 'f':
				nOutputFormat = Integer.parseInt(g.getOptarg());
				break;
			}
		}
		if (output == null)
		{
			System.out.println("no output specified!");
			printUsageAndExit();
		}
		Saint	saint = null;
		// specifying a bitstream overrides specifying orchestra & score files.
		if (bitstream != null)
		{
			saint = new Saint(bitstream, output, nOutputFormat);
		}
		else if (orchestra != null)
		{
			if (score != null)
			{
				saint = new Saint(orchestra, score, output, nOutputFormat);
			}
			else
			{
				System.out.println("no score file specified!");
				printUsageAndExit();
			}
		}
		else
		{
			System.out.println("neither bitstream nor orchestra specified!");
			printUsageAndExit();
		}
		System.out.println("output will be produces with " + saint.getChannelCount() + " channel(s) at " + saint.getSamplingRate() + " Hz\n");
		saint.run();
	}



	public static void
	printUsageAndExit()
	{
		System.out.println("usage:");
		System.out.println("\t[TODO]:");
		System.exit(1);
	}


	public static void
	printVersionAndExit()
	{
		System.out.println("saint (new) version 0.1");
		System.exit(0);
	}
}


/*** saint.java ***/

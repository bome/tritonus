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

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.LineUnavailableException;

import	gnu.getopt.Getopt;

import	org.tritonus.lowlevel.saint.Saint;

import	SourceDataLineOutputStream;


public class saint
{
	private static class SupportedFormat
	{
		private String	m_strName;
		private int	m_nNumber;

		public SupportedFormat(String strName, int nNumber)
		{
			m_strName = strName;
			m_nNumber = nNumber;
		}

		public String getName()
		{
			return m_strName;
		}

		public int getNumber()
		{
			return m_nNumber;
		}
	}

	private static final SupportedFormat[]	SUPPORTED_FORMATS =
	{
		new SupportedFormat("s8", Saint.SND_PCM_SFMT_S8),
		new SupportedFormat("u8", Saint.SND_PCM_SFMT_U8),
		new SupportedFormat("s16l", Saint.SND_PCM_SFMT_S16_LE),
		new SupportedFormat("s16b", Saint.SND_PCM_SFMT_S16_BE),
		new SupportedFormat("u16l", Saint.SND_PCM_SFMT_U16_LE),
		new SupportedFormat("u16b", Saint.SND_PCM_SFMT_U16_BE),
		new SupportedFormat("s24l", Saint.SND_PCM_SFMT_S24_LE),
		new SupportedFormat("s24b", Saint.SND_PCM_SFMT_S24_BE),
		new SupportedFormat("u24l", Saint.SND_PCM_SFMT_U24_LE),
		new SupportedFormat("u24b", Saint.SND_PCM_SFMT_U24_BE),
		new SupportedFormat("s32l", Saint.SND_PCM_SFMT_S32_LE),
		new SupportedFormat("s32b", Saint.SND_PCM_SFMT_S32_BE),
		new SupportedFormat("u32l", Saint.SND_PCM_SFMT_U32_LE),
		new SupportedFormat("u32b", Saint.SND_PCM_SFMT_U32_BE),
		new SupportedFormat("f32l", Saint.SND_PCM_SFMT_S32_LE),
		new SupportedFormat("f32b", Saint.SND_PCM_SFMT_S32_BE),
	};

	private static final int	DEFAULT_FORMAT = Saint.SND_PCM_SFMT_S32_LE;


	public static void main(String[] args)
		throws	IOException
	{
		InputStream	bitstream = null;
		InputStream	orchestra = null;
		InputStream	score = null;
		OutputStream	output = null;
		int		nOutputFormat = DEFAULT_FORMAT;
		boolean		bLineOutput = false;
		int	c;

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
				if (g.getOptarg().equals("-"))
				{
					output = System.out;
				}
				if (g.getOptarg().equals("+"))
				{
					bLineOutput = true;
				}
				else
				{
					output = new FileOutputStream(g.getOptarg());
				}
				break;

			case 'f':
				int	nNewOutputFormat = -1;
				for (int i = 0; i < SUPPORTED_FORMATS.length; i++)
				{
					if (SUPPORTED_FORMATS[i].getName().equals(g.getOptarg()))
					{
						nNewOutputFormat = SUPPORTED_FORMATS[i].getNumber();
					}
				}
				if (nNewOutputFormat != -1)
				{
					nOutputFormat = nNewOutputFormat;
				}
				else
				{
					System.err.println("warning: output format " + g.getOptarg() + "not supported; using default output format");
				}
				break;
			}
		}
		if (output == null && ! bLineOutput)
		{
			System.out.println("no output specified!");
			printUsageAndExit();
		}
		Saint	saint = null;
		// specifying a bitstream overrides specifying orchestra & score files.
		if (bitstream != null)
		{
			saint = new Saint(bitstream);
		}
		else if (orchestra != null)
		{
			if (score != null)
			{
				saint = new Saint(orchestra, score);
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
		System.err.println("output will be produces with " + saint.getChannelCount() + " channel(s) at " + saint.getSamplingRate() + " Hz\n");
		if (bLineOutput)
		{
			AudioFormat	format = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				saint.getSamplingRate(),
				16,
				saint.getChannelCount(),
				saint.getChannelCount() * 2,
				saint.getSamplingRate(),
				false);
			DataLine.Info	info = new DataLine.Info(
				SourceDataLine.class, format);
			SourceDataLine	line = null;
			try
			{
				line = (SourceDataLine) AudioSystem.getLine(info);
			}
			catch (LineUnavailableException e)
			{
			}
			output = new SourceDataLineOutputStream(line);
		}
		saint.setOutput(output, nOutputFormat);
		saint.run();
		try
		{
			output.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
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

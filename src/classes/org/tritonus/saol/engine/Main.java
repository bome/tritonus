/*
 *	Main.java
 */

import	java.io.File;
import	java.io.FileInputStream;
import	java.io.IOException;
import	java.io.InputStream;


import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioFileFormat;



public class Main
{
	public static void main(String[] args)
		throws IOException
	{
		File	outputFile = new File("output.au");
		AudioFileFormat.Type	targetType = AudioFileFormat.Type.WAVE;
		AudioFormat		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4, 44100.0F, false);
		Output	output = new FileOutput(outputFile, targetType, audioFormat);
		RTSystem	rtSystem = new RTSystem(output);
		rtSystem.start();
		InputStream	saslInputStream = new FileInputStream("sine.sasl");
		SaslParser	saslParser = new SaslParser(rtSystem, saslInputStream);
		new Thread(saslParser).start();
	}
}



/*** Main.java ***/

/*
 *	FileOutput.java
 */

import	java.io.IOException;
import	java.io.File;

import	javax.sound.sampled.AudioFileFormat;
import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;


/*
 *      Tritonus classes.
 *      Using these makes the program not portable to other
 *      Java Sound implementations.
 */
import	org.tritonus.share.sampled.AudioSystemShadow;
import  org.tritonus.share.sampled.file.AudioOutputStream;



public class FileOutput
extends AudioOutputStreamOutput
{
	public FileOutput(File outputFile, AudioFileFormat.Type targetType, AudioFormat audioFormat)
		throws IOException
	{
		super(AudioSystemShadow.getAudioOutputStream(
			targetType,
			audioFormat,
			AudioSystem.NOT_SPECIFIED,
			outputFile));
	}
}



/*** FileOutput.java ***/

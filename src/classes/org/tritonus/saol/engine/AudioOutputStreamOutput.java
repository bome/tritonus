/*
 *	AudioOutputStreamOutput.java
 */

import	java.io.IOException;

/*
 *      Tritonus classes.
 *      Using these makes the program not portable to other
 *      Java Sound implementations.
 */
import  org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.TConversionTool;
import  org.tritonus.share.sampled.file.AudioOutputStream;



public class AudioOutputStreamOutput
implements Output
{
	private AudioOutputStream	m_audioOutputStream;
	private float			m_fOutput;



	public AudioOutputStreamOutput(AudioOutputStream audioOutputStream)
	{
		m_audioOutputStream = audioOutputStream;
	}




	public void init()
	{
		m_fOutput = 0.0F;
	}



	public void output(float fValue)
	{
		// TDebug.out("output(): value: " + fValue);
		m_fOutput += fValue;
	}



	public void emit()
		throws IOException
	{
		float	fOutput = Math.max(Math.min(m_fOutput, 1.0F), -1.0F);
		// assumes 16 bit linear
		int	nOutput = (int) (fOutput * 32767.0F);
		byte[]	abBuffer = new byte[2];
		TConversionTool.shortToBytes16((short) nOutput, abBuffer, 0, false);
		m_audioOutputStream.write(abBuffer, 0, abBuffer.length);
	}



	public void close()
		throws IOException
	{
		m_audioOutputStream.close();
	}
}



/*** AudioOutputStreamOutput.java ***/

/*
 *	Saint.java
 */

package	org.tritonus.lowlevel.saint;


import	java.io.InputStream;
import	java.io.OutputStream;



public class Saint
{
	static
	{
		System.loadLibrary("tritonussaint");
	}


	/* copied from /usr/include/linux/asound.h */
	public static final int		SND_PCM_SFMT_S8 = 0;
	public static final int		SND_PCM_SFMT_U8	= 1;
	public static final int		SND_PCM_SFMT_S16_LE = 2;
	public static final int		SND_PCM_SFMT_S16_BE = 3;
	public static final int		SND_PCM_SFMT_U16_LE = 4;
	public static final int		SND_PCM_SFMT_U16_BE = 5;
	public static final int		SND_PCM_SFMT_S24_LE = 6;	/* low three bytes */
	public static final int		SND_PCM_SFMT_S24_BE = 7;	/* low three bytes */
	public static final int		SND_PCM_SFMT_U24_LE = 8;	/* low three bytes */
	public static final int		SND_PCM_SFMT_U24_BE = 9;	/* low three bytes */
	public static final int		SND_PCM_SFMT_S32_LE = 10;
	public static final int		SND_PCM_SFMT_S32_BE = 11;
	public static final int		SND_PCM_SFMT_U32_LE = 12;
	public static final int		SND_PCM_SFMT_U32_BE = 13;
	public static final int		SND_PCM_SFMT_FLOAT_LE = 14;	/* 4-byte float, IEEE-754 32-bit */
	public static final int		SND_PCM_SFMT_FLOAT_BE = 15;	/* 4-byte float, IEEE-754 32-bit */
	public static final int		SND_PCM_SFMT_FLOAT64_LE = 16;	/* 8-byte float, IEEE-754 64-bit */
	public static final int		SND_PCM_SFMT_FLOAT64_BE = 17;	/* 8-byte float, IEEE-754 64-bit */
	public static final int		SND_PCM_SFMT_IEC958_SUBFRAME_LE = 18;	/* IEC-958 subframe, Little Endian */
	public static final int		SND_PCM_SFMT_IEC958_SUBFRAME_BE = 19;	/* IEC-958 subframe, Big Endian */
	public static final int		SND_PCM_SFMT_MU_LAW = 20;
	public static final int		SND_PCM_SFMT_A_LAW = 21;
	public static final int		SND_PCM_SFMT_IMA_ADPCM = 22;
	public static final int		SND_PCM_SFMT_MPEG = 23;
	public static final int		SND_PCM_SFMT_GSM = 24;
	public static final int		SND_PCM_SFMT_SPECIAL = 31;


	private long	m_lNativeHandle;



	public Saint(InputStream bitStream)
	{
		init(bitStream);
	}



	public Saint(InputStream orchestraStream,
		     InputStream scoreStream)
	{
		init(orchestraStream,
		     scoreStream);
	}


	private native void init(
		InputStream bitStream);

	private native void init(
		InputStream orchestraStream,
		InputStream scoreStream);


	// TODO: finalizer??
/*
Saint::~Saint()
{
	if (m_pScheduler)
	{
		// with saint 1.33 (prerelease), a segfault occures if the destructor is called
		// delete m_pScheduler;
	}
}
*/



	public native int getSamplingRate();



	public native int getChannelCount();

	public native void setOutput(
		OutputStream outputStream,
		int nOutputFormat);

	public native void run();
}


/*** Saint.cpp ***/

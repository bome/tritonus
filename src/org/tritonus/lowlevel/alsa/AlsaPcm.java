/*
 *	AlsaPcm.java
 */

/*
 *  Copyright (c) 2000 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
 *
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */


package	org.tritonus.lowlevel.alsa;


import	org.tritonus.share.TDebug;


public class AlsaPcm
{
	public static final int	SND_PCM_STREAM_PLAYBACK = 0;
	/** Capture stream */
	public static final int	SND_PCM_STREAM_CAPTURE = 1;

/** PCM access type */
/* snd_pcm_access_t */
	/** mmap access with simple interleaved channels */
	public static final int	SND_PCM_ACCESS_MMAP_INTERLEAVED = 0;
	/** mmap access with simple non interleaved channels */
	public static final int	SND_PCM_ACCESS_MMAP_NONINTERLEAVED = 1;
	/** mmap access with complex placement */
	public static final int	SND_PCM_ACCESS_MMAP_COMPLEX = 2;
	/** snd_pcm_readi/snd_pcm_writei access */
	public static final int	SND_PCM_ACCESS_RW_INTERLEAVED = 3;
	/** snd_pcm_readn/snd_pcm_writen access */
	public static final int	SND_PCM_ACCESS_RW_NONINTERLEAVED = 4;


	public static final int	SND_PCM_FORMAT_UNKNOWN =		-1;
	public static final int	SND_PCM_FORMAT_S8 =			0;
	public static final int	SND_PCM_FORMAT_U8 =			1;
	public static final int	SND_PCM_FORMAT_S16_LE =			2;
	public static final int	SND_PCM_FORMAT_S16_BE =			3;
	public static final int	SND_PCM_FORMAT_U16_LE =			4;
	public static final int	SND_PCM_FORMAT_U16_BE =			5;
	public static final int	SND_PCM_FORMAT_S24_LE =			6;	/* low three bytes */
	public static final int	SND_PCM_FORMAT_S24_BE =			7;	/* low three bytes */
	public static final int	SND_PCM_FORMAT_U24_LE =			8;	/* low three bytes */
	public static final int	SND_PCM_FORMAT_U24_BE =			9;	/* low three bytes */
	public static final int	SND_PCM_FORMAT_S32_LE =			10;
	public static final int	SND_PCM_FORMAT_S32_BE =			11;
	public static final int	SND_PCM_FORMAT_U32_LE =			12;
	public static final int	SND_PCM_FORMAT_U32_BE =			13;
	public static final int	SND_PCM_FORMAT_FLOAT_LE =		14;	/* 4-byte float, IEEE-754 32-bit */
	public static final int	SND_PCM_FORMAT_FLOAT_BE =		15;	/* 4-byte float, IEEE-754 32-bit */
	public static final int	SND_PCM_FORMAT_FLOAT64_LE =		16;	/* 8-byte float, IEEE-754 64-bit */
	public static final int	SND_PCM_FORMAT_FLOAT64_BE =		17;	/* 8-byte float, IEEE-754 64-bit */
	public static final int	SND_PCM_FORMAT_IEC958_SUBFRAME_LE =	18;	/* IEC-958 subframe, Little Endian */
	public static final int	SND_PCM_FORMAT_IEC958_SUBFRAME_BE =	19;	/* IEC-958 subframe, Big Endian */
	public static final int	SND_PCM_FORMAT_MU_LAW =			20;
	public static final int	SND_PCM_FORMAT_A_LAW =			21;
	public static final int	SND_PCM_FORMAT_IMA_ADPCM =		22;
	public static final int	SND_PCM_FORMAT_MPEG =			23;
	public static final int	SND_PCM_FORMAT_GSM =			24;
	public static final int	SND_PCM_FORMAT_SPECIAL =		31;
	// currently missing: cpu endianedd formats

/*
  #ifdef SND_LITTLE_ENDIAN
  public static final int	SND_PCM_SFMT_S16 =		SND_PCM_SFMT_S16_LE
  public static final int	SND_PCM_SFMT_U16		SND_PCM_SFMT_U16_LE
  public static final int	SND_PCM_SFMT_S24		SND_PCM_SFMT_S24_LE
  public static final int	SND_PCM_SFMT_U24		SND_PCM_SFMT_U24_LE
  public static final int	SND_PCM_SFMT_S32		SND_PCM_SFMT_S32_LE
  public static final int	SND_PCM_SFMT_U32		SND_PCM_SFMT_U32_LE
  public static final int	SND_PCM_SFMT_FLOAT		SND_PCM_SFMT_FLOAT_LE
  public static final int	SND_PCM_SFMT_FLOAT64		SND_PCM_SFMT_FLOAT64_LE
  public static final int	SND_PCM_SFMT_IEC958_SUBFRAME	SND_PCM_SFMT_IEC958_SUBFRAME_LE
  #endif
  #ifdef SND_BIG_ENDIAN
  public static final int	SND_PCM_SFMT_S16		SND_PCM_SFMT_S16_BE
  public static final int	SND_PCM_SFMT_U16		SND_PCM_SFMT_U16_BE
  public static final int	SND_PCM_SFMT_S24		SND_PCM_SFMT_S24_BE
  public static final int	SND_PCM_SFMT_U24		SND_PCM_SFMT_U24_BE
  public static final int	SND_PCM_SFMT_S32		SND_PCM_SFMT_S32_BE
  public static final int	SND_PCM_SFMT_U32		SND_PCM_SFMT_U32_BE
  public static final int	SND_PCM_SFMT_FLOAT		SND_PCM_SFMT_FLOAT_BE
  public static final int	SND_PCM_SFMT_FLOAT64		SND_PCM_SFMT_FLOAT64_BE
  public static final int	SND_PCM_SFMT_IEC958_SUBFRAME	SND_PCM_SFMT_IEC958_SUBFRAME_BE
  #endif
*/

/*
  #ifdef SND_LITTLE_ENDIAN
  public static final int	SND_PCM_FMT_S16 =			SND_PCM_FMT_S16_LE
  public static final int	SND_PCM_FMT_U16			SND_PCM_FMT_U16_LE
  public static final int	SND_PCM_FMT_S24			SND_PCM_FMT_S24_LE
  public static final int	SND_PCM_FMT_U24			SND_PCM_FMT_U24_LE
  public static final int	SND_PCM_FMT_S32			SND_PCM_FMT_S32_LE
  public static final int	SND_PCM_FMT_U32			SND_PCM_FMT_U32_LE
  public static final int	SND_PCM_FMT_FLOAT		SND_PCM_FMT_FLOAT_LE
  public static final int	SND_PCM_FMT_FLOAT64		SND_PCM_FMT_FLOAT64_LE
  public static final int	SND_PCM_FMT_IEC958_SUBFRAME	SND_PCM_FMT_IEC958_SUBFRAME_LE
  #endif
  #ifdef SND_BIG_ENDIAN
  public static final int	SND_PCM_FMT_S16			SND_PCM_FMT_S16_BE
  public static final int	SND_PCM_FMT_U16			SND_PCM_FMT_U16_BE
  public static final int	SND_PCM_FMT_S24			SND_PCM_FMT_S24_BE
  public static final int	SND_PCM_FMT_U24			SND_PCM_FMT_U24_BE
  public static final int	SND_PCM_FMT_S32			SND_PCM_FMT_S32_BE
  public static final int	SND_PCM_FMT_U32			SND_PCM_FMT_U32_BE
  public static final int	SND_PCM_FMT_FLOAT		SND_PCM_FMT_FLOAT_BE
  public static final int	SND_PCM_FMT_FLOAT64		SND_PCM_FMT_FLOAT64_BE
  public static final int	SND_PCM_FMT_IEC958_SUBFRAME	SND_PCM_FMT_IEC958_SUBFRAME_BE
  #endif
*/


	/** PCM state (snd_pcm_state_t) */
	/** Open */
	public static final int	SND_PCM_STATE_OPEN = 0;
	/** Setup installed */ 
	public static final int	SND_PCM_STATE_SETUP = 1;
	/** Ready to start */
	public static final int	SND_PCM_STATE_PREPARED = 2;
	/** Running */
	public static final int	SND_PCM_STATE_RUNNING = 3;
	/** Stopped: underrun (playback) or overrun (capture) detected */
	public static final int	SND_PCM_STATE_XRUN = 4;
	/** Draining: running (playback) or stopped (capture) */
	public static final int	SND_PCM_STATE_DRAINING = 5;
	/** Paused */
	public static final int	SND_PCM_STATE_PAUSED = 6;


/** PCM start mode (snd_pcm_start_t) */
	/** Automatic start on data read/write */
	public static final int	SND_PCM_START_DATA = 0;
	/** Explicit start */
	public static final int	SND_PCM_START_EXPLICIT = 1;


/** PCM xrun mode (snd_pcm_xrun_t) */
	/** Xrun detection disabled */
	public static final int	SND_PCM_XRUN_NONE = 0;
	/** Stop on xrun detection */
	public static final int	SND_PCM_XRUN_STOP = 1;


/** PCM timestamp mode (snd_pcm_tstamp_t) */
	/** No timestamp */
	public static final int	SND_PCM_TSTAMP_NONE = 0;
	/** Update mmap'ed timestamp */
	public static final int	SND_PCM_TSTAMP_MMAP = 1;




	static
	{
		Alsa.loadNativeLibrary();
		if (TDebug.TraceAlsaPcmNative)
		{
			setTrace(true);
		}
	}



	/**
	 *	Holds the pointer to snd_pcm_t for the native code.
	 *	This must be long to be 64bit-clean.
	 */
	private long	m_lNativeHandle;


	/**
	 *	For parameter documentation, see open().
	 */
	public AlsaPcm(String strPcmName,
		       int nDirection,
		       int nMode)
		throws	Exception
	{
		int	nReturn;
		nReturn = open(strPcmName,
			       nDirection,
			       nMode);
		if (nReturn < 0)
		{
			throw new Exception(Alsa.getStringError(nReturn));
		}
	}



	/**
	 *	Calls snd_pcm_open().
	 *
	 *	@param strPcmName An ALSA pcm name, e.g. 'hw:0,0'.
	 *
	 *	@param nDirection one of SND_PCM_STREAM_PLAYBACK, SND_PCM_STREAM_CAPTURE.
	 *
	 *	@param nMode optional file open modes (non-blocking,...)

	*/
	private native int open(String strPcmName,
				int nDirection,
				int nMode);



	/**
	 *	Calls snd_pcm_close().
	 */
	public native int close();


	/**
	 *	Calls snd_pcm_hw_params_any().
	 */
	public native int getAnyHWParams(HWParams hwParams);


	/**
	 *	Calls snd_pcm_hw_params_set_access().
	 */
	public native int setHWParamsAccess(HWParams hwParams, int nAccess);



	/**
	 *	Calls snd_pcm_hw_params_set_format().
	 */
	public native int setHWParamsFormat(HWParams hwParams, int nFormat);

	/**
	 *	Calls snd_pcm_hw_params_set_format_mask().
	 */
	public native int setHWParamsFormatMask(HWParams hwParams, HWParams.FormatMask mask);

	/**
	 *	Calls snd_pcm_hw_params_set_channels().
	 */
	public native int setHWParamsChannels(HWParams hwParams, int nChannels);

	/**
	 *	Calls snd_pcm_hw_params_set_rate_near().
	 */
	public native int setHWParamsRateNear(HWParams hwParams, int nRate /* missing: out parameter direction? */);

	/**
	 *	Calls snd_pcm_hw_params_set_buffer_time_near().
	 */
	public native int setHWParamsBufferTimeNear(HWParams hwParams, int nBufferTime /* missing: out parameter direction? */);

	/**
	 *	Calls snd_pcm_hw_params_set_period_time_near().
	 */
	public native int setHWParamsPeriodTimeNear(HWParams hwParams, int nPeriodTime /* missing: out parameter direction? */);

	/**
	 *	Calls snd_pcm_hw_params().
	 */
	public native int setHWParams(HWParams hwParams);


	public native int getSWParams(SWParams swParams);
	public native int setSWParamsStartMode(SWParams swParams, int n);
	public native int setSWParamsXrunMode(SWParams swParams, int n);
	public native int setSWParamsTStampMode(SWParams swParams, int n);
	public native int setSWParamsSleepMin(SWParams swParams, int n);
	public native int setSWParamsAvailMin(SWParams swParams, int n);
	public native int setSWParamsXferAlign(SWParams swParams, int n);
	// TODO: should be long (snd_pcm_uframes_t)
	public native int setSWParamsStartThreshold(SWParams swParams, int n);
	public native int setSWParamsStopThreshold(SWParams swParams, int n);
	public native int setSWParamsSilenceThreshold(SWParams swParams, int n);
	public native int setSWParamsSilenceSize(SWParams swParams, int n);
	public native int setSWParams(SWParams swParams);

	public native long writei(byte[] abBuffer, long lOffset, long lFrameCount);
	public native long readi(byte[] abBuffer, long lOffset, long lFrameCount);

	public static native void setTrace(boolean bTrace);


//-------------------------------------------------------------------


	public static class HWParams
	{
		/**
		 *	Holds the pointer to snd_pcm_hw_params_t
		 *	for the native code.
		 *	This must be long to be 64bit-clean.
		 */
		/*private*/ long	m_lNativeHandle;



		public HWParams()
		{
			if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.HWParams.<init>(): begin"); }
			int	nReturn = malloc();
			if (nReturn < 0)
			{
				throw new RuntimeException("malloc of hw_params failed");
			}
			if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.HWParams.<init>(): end"); }
		}



		public void finalize()
		{
			// TODO: call free()
			// call super.finalize() first or last?
			// and introduce a flag if free() has already been called?
		}



		private native int malloc();
		public native void free();

		/**
		 *	Calls snd_pcm_hw_params_get_rate_numden().
		 *
		 *	alValues[0]:	numerator
		 *	alValues[1]:	denominator
		 */
		public native int getRate(long[] alValues);
		public double getRate()
		{
			long[]	alValues = new long[2];
			int	nReturn;

			nReturn = getRate(alValues);
			double	dRate = (double) -1;
			if (nReturn >= 0)
			{
				dRate = (double) alValues[0] / (double) alValues[1];
			}
			return dRate;
		}

		public native int getSBits();
		public native int getFifoSize();
		public native int getAccess();
		public native int getFormat();
		public native void getFormatMask(FormatMask mask);
		public native int getSubformat();
		public native int getChannels();
		public native int getChannelsMin();
		public native int getChannelsMax();


		/**	Gets approximate rate.
		 *	Calls snd_pcm_hw_params_get_rate().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getRate(int[] anValues);

		/**	Gets approximate minimum rate.
		 *	Calls snd_pcm_hw_params_get_rate_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getRateMin(int[] anValues);

		/**	Gets approximate maximum rate.
		 *	Calls snd_pcm_hw_params_get_rate_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getRateMax(int[] anValues);

		/**	Gets approximate period time.
		 *	Calls snd_pcm_hw_params_get_period_time().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodTime(int[] anValues);

		/**	Gets approximate minimum period time.
		 *	Calls snd_pcm_hw_params_get_period_time_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodTimeMin(int[] anValues);

		/**	Gets approximate maximum period time.
		 *	Calls snd_pcm_hw_params_get_period_time_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodTimeMax(int[] anValues);


		/**	Gets approximate period size.
		 *	Calls snd_pcm_hw_params_get_period_size().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodSize(int[] anValues);

		/**	Gets approximate minimum period size.
		 *	Calls snd_pcm_hw_params_get_period_size_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodSizeMin(int[] anValues);

		/**	Gets approximate maximum period size.
		 *	Calls snd_pcm_hw_params_get_period_size_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodSizeMax(int[] anValues);


		/**	Gets approximate periods.
		 *	Calls snd_pcm_hw_params_get_periods().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriods(int[] anValues);

		/**	Gets approximate minimum periods.
		 *	Calls snd_pcm_hw_params_get_periods_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodsMin(int[] anValues);

		/**	Gets approximate maximum periods.
		 *	Calls snd_pcm_hw_params_get_periods_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getPeriodsMax(int[] anValues);

		/**	Gets approximate buffer time.
		 *	Calls snd_pcm_hw_params_get_buffer_time().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getBufferTime(int[] anValues);

		/**	Gets approximate minimum buffer time.
		 *	Calls snd_pcm_hw_params_get_buffer_time_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getBufferTimeMin(int[] anValues);

		/**	Gets approximate maximum buffer time.
		 *	Calls snd_pcm_hw_params_get_buffer_time_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getBufferTimeMax(int[] anValues);


		/**	Gets approximate buffer size.
		 *	Calls snd_pcm_hw_params_get_buffer_size().
		 */
		public native int getBufferSize();

		/**	Gets approximate minimum buffer size.
		 *	Calls snd_pcm_hw_params_get_buffer_size_min().
		 */
		public native int getBufferSizeMin();

		/**	Gets approximate maximum buffer size.
		 *	Calls snd_pcm_hw_params_get_buffer_size_max().
		 */
		public native int getBufferSizeMax();


		/**	Gets approximate tick time.
		 *	Calls snd_pcm_hw_params_get_tick_time().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getTickTime(int[] anValues);

		/**	Gets approximate minimum tick time.
		 *	Calls snd_pcm_hw_params_get_tick_time_min().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getTickTimeMin(int[] anValues);

		/**	Gets approximate maximum tick time.
		 *	Calls snd_pcm_hw_params_get_tick_time_max().
		 *	anValues[0]:	-1, 0 or +1, depending on the direction the exact rate differs from the returned value.
		 */
		public native int getTickTimeMax(int[] anValues);




		public static class FormatMask
		{
			/**
			 *	Holds the pointer to snd_pcm_format_mask_t
			 *	for the native code.
			 *	This must be long to be 64bit-clean.
			 */
			/*private*/ long	m_lNativeHandle;



			public FormatMask()
			{
				if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.FormatMask.<init>(): begin"); }
				int	nReturn = malloc();
				if (nReturn < 0)
				{
					throw new RuntimeException("malloc of format_mask failed");
				}
				if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.FormatMask.<init>(): end"); }
			}



			public void finalize()
			{
				// TODO: call free()
				// call super.finalize() first or last?
				// and introduce a flag if free() has already been called?
			}


			/**
			 *	Calls snd_pcm_format_mask_malloc().
			 */
			private native int malloc();



			/**
			 *	Calls snd_pcm_format_mask_free().
			 */
			public native void free();

			/**
			 *	Calls snd_pcm_format_mask_none().
			 */
			public native void none();

			/**
			 *	Calls snd_pcm_format_mask_any().
			 */
			public native void any();

			/**
			 *	Calls snd_pcm_format_mask_test().
			 */
			public native boolean test(int nFormat);


			/**
			 *	Calls snd_pcm_format_mask_set().
			 */
			public native void set(int nFormat);


			/**
			 *	Calls snd_pcm_format_mask_reset().
			 */
			public native void reset(int nFormat);


			}
		}



	public static class SWParams
	{
		/**
		 *	Holds the pointer to snd_pcm_sw_params_t
		 *	for the native code.
		 *	This must be long to be 64bit-clean.
		 */
		/*private*/ long	m_lNativeHandle;



		public SWParams()
		{
			if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.SWParams.<init>(): begin"); }
			int	nReturn = malloc();
			if (nReturn < 0)
			{
				throw new RuntimeException("malloc of hw_params failed");
			}
			if (TDebug.TraceAlsaPcmNative) { TDebug.out("AlsaPcm.SWParams.<init>(): end"); }
		}



		public void finalize()
		{
			// TODO: call free()
			// call super.finalize() first or last?
			// and introduce a flag if free() has already been called?
		}



		private native int malloc();
		public native void free();

		public native int getStartMode();
		public native int getXrunMode();
		public native int getTStampMode();
		public native int getSleepMin();
		public native int getAvailMin();
		public native int getXferAlign();
		public native int getStartThreshold();
		public native int getStopThreshold();
		public native int getSilenceThreshold();
		public native int getSilenceSize();
	}
}



/*** AlsaPcm.java ***/

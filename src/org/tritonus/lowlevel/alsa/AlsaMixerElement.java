/*
 *	AlsaMixerElement.java
 */

/*
 *  Copyright (c) 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


public class AlsaMixerElement
{
	/*	Channel type constants.
		They mirror the values of snd_mixer_selem_channel_id_t.
	 */

	/** Unknown */
	public static final int SND_MIXER_SCHN_UNKNOWN = -1;
	/** Front left */
	public static final int SND_MIXER_SCHN_FRONT_LEFT = 0;
	/** Front right */
	public static final int SND_MIXER_SCHN_FRONT_RIGHT = 1;
	/** Front center */
	public static final int SND_MIXER_SCHN_FRONT_CENTER = 2;
	/** Rear left */
	public static final int SND_MIXER_SCHN_REAR_LEFT = 3;
	/** Rear right */
	public static final int SND_MIXER_SCHN_REAR_RIGHT = 4;
	/** Woofer */
	public static final int SND_MIXER_SCHN_WOOFER = 5;
	public static final int SND_MIXER_SCHN_LAST = 31;
	/** Mono (Front left alias) */
	public static final int SND_MIXER_SCHN_MONO = SND_MIXER_SCHN_FRONT_LEFT;



	private AlsaMixer	m_mixer;
	private long		m_lNativeHandle;



	static
	{
		Alsa.loadNativeLibrary();
		if (TDebug.TraceAlsaMixerNative)
		{
			setTrace(true);
		}
	}



	public AlsaMixerElement(AlsaMixer mixer,
				int nIndex,
				String strName)
	{
		m_mixer = mixer;
		int	nReturn;
		nReturn = open(getMixer(), nIndex, strName);
		{
			if (nReturn < 0)
			{
				throw new RuntimeException("cannot open");
			}
		}
	}


	private native int open(AlsaMixer mixer,
			       int nIndex,
			       String strName);


	private AlsaMixer getMixer()
	{
		return m_mixer;
	}



	public native String getName();
	public native int getIndex();

	public native boolean isPlaybackMono();
	public native boolean hasPlaybackChannel(int nChannelType);
	public native boolean isCaptureMono();
	public native boolean hasCaptureChannel(int nChannelType);
	public native int getCaptureGroup();
	public native boolean hasCommonVolume();
	public native boolean hasPlaybackVolume();
	public native boolean hasPlaybackVolumeJoined();
	public native boolean hasCaptureVolume();
	public native boolean hasCaptureVolumeJoined();

	public native boolean hasCommonSwitch();
	public native boolean hasPlaybackSwitch();
	public native boolean hasPlaybackSwitchJoined();
	public native boolean hasCaptureSwitch();
	public native boolean hasCaptureSwitchJoinded();
	public native boolean hasCaptureSwitchExclusive();

	public native int getPlaybackVolume(int nChannelType);
	public native int getCaptureVolume(int nChannelType);
	public native int getPlaybackSwitch(int nChannelType);
	public native int getCaptureSwitch(int nChannelType);

	public native void setPlaybackVolume(int nChannelType, int nValue);
	public native void setCaptureVolume(int nChannelType, int nValue);
	public native void setPlaybackVolumeAll(int nValue);
	public native void setCaptureVolumeAll(int nValue);

	public native void setPlaybackSwitch(int nChannelType, int nValue);
	public native void setCaptureSwitch(int nChannelType, int nValue);
	public native void setPlaybackSwitchAll(int nValue);
	public native void setCaptureSwitchAll(int nValue);

	public native void getPlaybackVolumeRange(int[] anValues);
	public native void getCaptureVolumeRange(int[] anValues);
	public native void setPlaybackVolumeRange(int nMin, int nMax);
	public native void setCaptureVolumeRange(int nMin, int nMax);

	public static native String getChannelName(int nChannelType);


	private static native void setTrace(boolean bTrace);
}



/*** AlsaMixerElement.java ***/

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
	AlsaMixer		m_mixer;
	private int		m_nIndex;
	private String		m_strName;
	/*private*/ long		m_nativeHandle;



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
		m_nIndex = nIndex;
		m_strName = strName;
	}



	private AlsaMixer getMixer()
	{
		return m_mixer;
	}

/*
	public int getIndex()
	{
		return m_nIndex;
	}


	public String getName()
	{
		return m_strName;
	}
*/


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

	public native int getPlaybackVolume(int nChannel);

	public static native String getChannelName(int nChannelType);


	private static native void setTrace(boolean bTrace);
}



/*** AlsaMixerElement.java ***/

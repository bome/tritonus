/*
 *	AlsaMixer.java
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	java.util.ArrayList;
import	java.util.List;

import	org.tritonus.share.TDebug;


public class AlsaMixer
{
	private long	m_nativeHandle;
	private List	m_controlsList;



	static
	{
		if (TDebug.TraceAlsaNative)
		{
			System.out.println("AlsaMixer.<clinit>(): loading native library tritonusalsa");
		}
		System.loadLibrary("tritonusalsa");
		setTrace(TDebug.TraceAlsaMixerNative);
	}



	public AlsaMixer(String strName)
		throws	Exception
	{
		if (open(strName) < 0)
		{
			throw new Exception();
		}
	}


	private native int open(String strMixerName);
	public native int close();



	public List getControls()
	{
		if (m_controlsList == null)
		{
			m_controlsList = createControls();
		}
		return m_controlsList;
	}



	private List createControls()
	{
		List	controlsList = new ArrayList();
		// TODO: loop making arrays bigger and bigger
		int		nArraySize = 32;
		int[]		anIndices = new int[nArraySize];
		String[]	astrNames = new String[nArraySize];
		int	nControlsCount = readControlList(anIndices, astrNames);
		System.out.println("num of controls: " + nControlsCount);
		for (int i = 0; i < nControlsCount; i++)
		{
			AlsaMixerControl	control = new AlsaMixerControl(this, anIndices[i], astrNames[i]);
			control.read();
			controlsList.add(control);
		}
		return controlsList;
	}



	/**
	   The caller has to allocate the indices and names arrays big
	   enough to hold information on all controls. If the retrieving
	   of controls is successful, a positive number (the number of
	   controls) is returned. If the arrays are not big enough, -1
	   is returned. In this case, it's the task of the caller to allocate
	   bigger arrays and try again.
	   Both arrays must be of the same size.
	 */
	public native int readControlList(int[] anIndices, String[] astrNames);


	/**
	 *	anValues[0]	capabilities mask
	 *	anValues[1]	active channels mask
	 *	anValues[2]	muted channels mask
	 *	anValues[3]	capture channels mask
	 *	anValues[4]	minimum value
	 *	anValues[5]	maximum value
	 *	anValues[6]	front left value
	 *	anValues[7]	front right value
	 *	anValues[8]	front center value
	 *	anValues[9]	rear left value
	 *	anValues[10]	rear right value
	 *	anValues[11]	woofer value
	 *
	 */
	public native int readControl(int nIndex, String strName,
				      int[] anValues);


	/**
	 *	anValues[0]	muted channels mask
	 *	anValues[1]	capture channels mask
	 *	anValues[2]	front left value
	 *	anValues[3]	front right value
	 *	anValues[4]	front center value
	 *	anValues[5]	rear left value
	 *	anValues[6]	rear right value
	 *	anValues[7]	woofer value
	 */
	public native int writeControl(int nIndex, String strName,
				      int[] anValues);

	private static native void setTrace(boolean bTrace);
}



/*** AlsaMixer.java ***/

/*
 *	AlsaMixerControl.java
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


import	org.tritonus.share.TDebug;


public class AlsaMixerControl
{
	AlsaMixer		m_mixer;
	private int		m_nIndex;
	private String		m_strName;



	public AlsaMixerControl(AlsaMixer mixer,
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


	public int getIndex()
	{
		return m_nIndex;
	}


	public String getName()
	{
		return m_strName;
	}


	public void read()
	{
	}


	public void write()
	{
	}

}



/*** AlsaMixerControl.java ***/

/*
 *	TMixerProvider.java
 */

/*
 *  Copyright (c) 1999 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.sampled.mixer;


import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.spi.MixerProvider;


public abstract class TMixerProvider
	extends	MixerProvider
{
	private Mixer	m_defaultMixer;




	public TMixerProvider()
	{
		m_defaultMixer = null;
	}


	public boolean isMixerSupported(Mixer.Info info)
	{
		Mixer.Info[]	infos = getMixerInfo();
		for (int i = 0; i < infos.length; i++)
		{
			if (infos[i].equals(info))
			{
				return true;
			}
		}
		return false;
	}



	/**
	 *	Currently always returns the default mixer regardless
	 *	of passed Mixer.Info.
	 */
	public Mixer getMixer(Mixer.Info info)
		// Exception ??
		// sun doc says IllegalArgumentException
	{
		if (m_defaultMixer == null)
		{
			m_defaultMixer = createDefaultMixer();
		}
		return m_defaultMixer;
	}



	public Mixer.Info[] getMixerInfo()
	{
		return new Mixer.Info[]{getMixer(null).getMixerInfo()};
	}


	/**	Returns an instance of the default mixer.
	 *	No singleton behaviour is required, since this class
	 *	guarantees that this function is only called once.
	 *	Subclasses must implement this function.
	 */
	protected abstract Mixer createDefaultMixer();
}



/*** TMixerProvider.java ***/

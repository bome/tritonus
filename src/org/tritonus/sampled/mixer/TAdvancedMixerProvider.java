/*
 *	TAdvancedMixerProvider.java
 */

/*
 *  Copyright (c) 1999, 2000 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


import	java.util.ArrayList;
import	java.util.Set;
import	java.util.HashSet;
import	java.util.List;
import	java.util.Iterator;

import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.spi.MixerProvider;


public abstract class TAdvancedMixerProvider
	extends	TMixerProvider
{
	private List	m_mixers;
	private Mixer	m_defaultMixer;




	public TAdvancedMixerProvider()
	{
		m_mixers = new ArrayList();
		m_defaultMixer = null;
	}



	protected void addMixer(Mixer mixer)
	{
		m_mixers.add(mixer);
		if (m_defaultMixer == null)
		{
			m_defaultMixer = mixer;
		}
	}



	/**
	 *	Currently always returns the default mixer regardless
	 *	of passed Mixer.Info.
	 */
	public Mixer getMixer(Mixer.Info info)
		// Exception ??
		// sun doc says IllegalArgumentException
	{
		if (info == null)
		{
			if (m_defaultMixer == null)
			{
				m_defaultMixer = createDefaultMixer();
			}
			return m_defaultMixer;
		}
		else
		{
			Iterator	mixers = m_mixers.iterator();
			while (mixers.hasNext())
			{
				Mixer	mixer = (Mixer) mixers.next();
				if (mixer.getMixerInfo().equals(info))
				{
					return mixer;
				}
			}
		}
		return null;
	}



	public Mixer.Info[] getMixerInfo()
	{
		Set	mixerInfos = new HashSet();
		Iterator	mixers = m_mixers.iterator();
		while (mixers.hasNext())
		{
			Mixer	mixer = (Mixer) mixers.next();
			mixerInfos.add(mixer.getMixerInfo());
		}
		return (Mixer.Info[]) mixerInfos.toArray(new Mixer.Info[0]);
	}



	/**	Dummy only.
	 */
	protected Mixer createDefaultMixer()
	{
		return null;
	}
}



/*** TAdvancedMixerProvider.java ***/

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


import	java.util.ArrayList;
import	java.util.HashMap;
import	java.util.HashSet;
import	java.util.Iterator;
import	java.util.List;
import	java.util.Map;
import	java.util.Set;

import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.spi.MixerProvider;

import	org.tritonus.TDebug;



public abstract class TMixerProvider
	extends	MixerProvider
{
	private static final Mixer.Info[]	EMPTY_MIXER_INFO_ARRAY = new Mixer.Info[0];

	private static Map	sm_mixerProviderStructs = new HashMap();




	public TMixerProvider()
	{
	}



	private MixerProviderStruct getMixerProviderStruct()
	{
		Class			cls = this.getClass();
		if (TDebug.TraceMixerProvider)
		{
			TDebug.out("TMixerProvider.getMixerProviderStruct(): called from " + cls);
			Thread.dumpStack();
		}
		synchronized (TMixerProvider.class)
		{
			MixerProviderStruct	struct = (MixerProviderStruct) sm_mixerProviderStructs.get(cls);
			if (struct == null)
			{
				if (TDebug.TraceMixerProvider)
				{
					TDebug.out("TMixerProvider.getMixerProviderStruct(): creating new MixerProviderStruct for " + cls);
				}
				struct = new MixerProviderStruct();
				sm_mixerProviderStructs.put(cls, struct);
			}
			return struct;
		}
	}



	protected void addMixer(Mixer mixer)
	{
		MixerProviderStruct	struct = getMixerProviderStruct();
		synchronized (struct)
		{
			struct.m_mixers.add(mixer);
			if (struct.m_defaultMixer == null)
			{
				struct.m_defaultMixer = mixer;
			}
		}
	}



	protected void removeMixer(Mixer mixer)
	{
		MixerProviderStruct	struct = getMixerProviderStruct();
		synchronized (struct)
		{
			struct.m_mixers.remove(mixer);
			// TODO: should search for another mixer
			if (struct.m_defaultMixer == mixer)
			{
				struct.m_defaultMixer = null;
			}
		}
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
	 */
	public Mixer getMixer(Mixer.Info info)
	{
		MixerProviderStruct	struct = getMixerProviderStruct();
		synchronized (struct)
		{
			if (info == null)
			{
				if (struct.m_defaultMixer == null)
				{
					throw new IllegalArgumentException("no mixer available for " + info);
				}
				return struct.m_defaultMixer;
			}
			else
			{
				Iterator	mixers = struct.m_mixers.iterator();
				while (mixers.hasNext())
				{
					Mixer	mixer = (Mixer) mixers.next();
					if (mixer.getMixerInfo().equals(info))
					{
						return mixer;
					}
				}
			}
		}
		throw new IllegalArgumentException("no mixer available for " + info);
	}



	public Mixer.Info[] getMixerInfo()
	{
		Set	mixerInfos = new HashSet();
		MixerProviderStruct	struct = getMixerProviderStruct();
		synchronized (struct)
		{
			Iterator	mixers = struct.m_mixers.iterator();
			while (mixers.hasNext())
			{
				Mixer	mixer = (Mixer) mixers.next();
				mixerInfos.add(mixer.getMixerInfo());
			}
		}
		return (Mixer.Info[]) mixerInfos.toArray(EMPTY_MIXER_INFO_ARRAY);
	}



	private class MixerProviderStruct
	{
		public List	m_mixers;
		public Mixer	m_defaultMixer;



		public MixerProviderStruct()
		{
			m_mixers = new ArrayList();
			m_defaultMixer = null;
		}
	}
}



/*** TMixerProvider.java ***/

/*
 *	AlsaPortMixer.java
 */

/*
 *  Copyright (c) 1999 - 2001 by Matthias Pfisterer <Matthias.Pfisterer@gmx.de>
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


package	org.tritonus.sampled.mixer.alsa;


import	java.util.Arrays;
import	java.util.ArrayList;
import	java.util.HashMap;
import	java.util.Map;
import	java.util.HashSet;
import	java.util.Set;
import	java.util.Iterator;
import	java.util.List;

import	javax.sound.sampled.AudioFormat;
import	javax.sound.sampled.AudioSystem;
import	javax.sound.sampled.BooleanControl;
import	javax.sound.sampled.Clip;
import	javax.sound.sampled.Control;
import	javax.sound.sampled.DataLine;
import	javax.sound.sampled.FloatControl;
import	javax.sound.sampled.SourceDataLine;
import	javax.sound.sampled.TargetDataLine;
import	javax.sound.sampled.LineUnavailableException;
import	javax.sound.sampled.Line;
import	javax.sound.sampled.Mixer;
import	javax.sound.sampled.Port;

import	org.tritonus.share.GlobalInfo;
import	org.tritonus.share.TDebug;
import	org.tritonus.share.sampled.mixer.TBooleanControl;
import	org.tritonus.share.sampled.mixer.TFloatControl;
import	org.tritonus.share.sampled.mixer.TMixer;
import	org.tritonus.share.sampled.mixer.TMixerInfo;
import	org.tritonus.share.sampled.mixer.TPort;
import	org.tritonus.share.sampled.mixer.TSoftClip;

import	org.tritonus.lowlevel.alsa.AlsaMixer;
import	org.tritonus.lowlevel.alsa.AlsaMixerElement;



public class AlsaPortMixer
	extends		TMixer
{
	/*
	  For the first shot, we try to create one port line per mixer
	  element. For now, the following two lists should have the same size
	  and related elements at the same index position.
	*/
	private List			m_mixerElements;

	/*
	  Port.Infos are keys, Port instances are values.
	 */
	private Map			m_portMap;

	/*
	  Port.Infos are keys, AlsaMixerElement instances are values.
	 */
	private Map			m_mixerElementMap;



	public AlsaPortMixer(int nCard)
	{
		this("hw:" + nCard);
	}



	public AlsaPortMixer(String strDeviceName)
	{
		super(new TMixerInfo(
			      "Alsa System Mixer (" + strDeviceName + ")",
			      GlobalInfo.getVendor(),
			      "Alsa System Mixer (" + strDeviceName + ")",
			      GlobalInfo.getVersion()),
		      new Line.Info(Mixer.class));
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.<init>: begin"); }
		m_mixerElements = new ArrayList();
		m_mixerElementMap = new HashMap();
		m_portMap = new HashMap();
		AlsaMixer	alsaMixer = null;
		try
		{
			alsaMixer = new AlsaMixer(strDeviceName);
			if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.<init>: successfully created AlsaMixer instance"); }
		}
		catch (Exception e)
		{
			if (TDebug.TraceMixer || TDebug.TraceAllExceptions) { TDebug.out(e); }
		}
		int		nArraySize = 128;
		int[]		anIndices = null;
		String[]	astrNames = null;
		int		nControlCount = 0;
		while (true)
		{
			anIndices = new int[nArraySize];
			astrNames = new String[nArraySize];
			nControlCount = alsaMixer.readControlList(anIndices, astrNames);
			if (nControlCount >= 0)
			{
				break;
			}
			if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.<init>: increating array size for AlsaMixer.readControlList(): now " + nArraySize * 2); }
			nArraySize *= 2;
		}
		List	sourcePortInfos = new ArrayList();
		List	targetPortInfos = new ArrayList();
		for (int i = 0; i < nControlCount; i++)
		{
			if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.<init>(): control " + i + ": " + anIndices[i] + " " + astrNames[i]); }
			AlsaMixerElement	element = new AlsaMixerElement(alsaMixer, anIndices[i], astrNames[i]);
			m_mixerElements.add(element);
			/*TODO: this flag should be refined*/
			boolean	bIsSource = true;
			Port.Info	info = new Port.Info(Port.class,
							     element.getName(), bIsSource);
			if (bIsSource)
			{
				sourcePortInfos.add(info);
			}
			else
			{
				targetPortInfos.add(info);
			}
			m_mixerElementMap.put(info, element);
		}
		setSupportInformation(
			new ArrayList(),
			new ArrayList(),
			sourcePortInfos,
			targetPortInfos);
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.<init>: end."); }
	}



	//////////////// Line //////////////////////////////////////


	// TODO: allow real close and reopen of mixer
	public void open()
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.open(): begin"); }

		// currently does nothing

		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.open(): end"); }
	}



	public void close()
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.close(): begin"); }

		// currently does nothing

		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.close(): end"); }
	}





	//////////////// Mixer //////////////////////////////////////






/*
  public Line.Info getLineInfo(Line.Info info)
  {
  // TODO:
  return null;
  }
*/

	// TODO:
	public int getMaxLines(Line.Info info)
	{
		// TODO:
		return 0;
	}




	protected Port getPort(Port.Info info)
		throws LineUnavailableException
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.getPort(): begin"); }
		Port	port = null;
		port = (Port) m_portMap.get(info);
		if (port == null)
		{
			port = createPort(info);
			m_portMap.put(info, port);
		}
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.getPort(): end"); }
		return port;
	}


	private Port createPort(Port.Info info)
	{
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.createPort(): begin"); }
		AlsaMixerElement	element = (AlsaMixerElement) m_mixerElementMap.get(info);
		if (element == null)
		{
			throw new IllegalArgumentException("no port for this info");
		}
		List	controls = new ArrayList();
		Control	c = createFloatControl(element);
		controls.add(c);
		Port	port = new TPort(this, info, controls);
		if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.createPort(): end"); }
		return port;
	}



	// rename to createVolumeControl?
	private FloatControl createFloatControl(AlsaMixerElement element)
	{
		boolean		bCapture;
		if (element.hasPlaybackVolume())
		{
			bCapture = false;
		}
		else if (element.hasCaptureVolume())
		{
			bCapture = true;
		}
		else
		{
			// no volume control at all
			return null;
		}
		int[]	anValues = new int[2];
		if (bCapture)
		{
			element.getCaptureVolumeRange(anValues);
		}
		else
		{
			element.getPlaybackVolumeRange(anValues);
		}
		FloatControl	control = new AlsaFloatControl(FloatControl.Type.VOLUME,
							       anValues[0],
							       anValues[1],
							       1.0F,
							       -1,
							       anValues[0],
							       "", "", "", "",
							       element,
							       -99,
							       bCapture);
		return control;
	}


	//////////////// inner classes //////////////////////////////////////



	private class AlsaFloatControl
		extends FloatControl
	{
		private AlsaMixerElement	m_element;
		private int			m_nChannel;
		private boolean			m_bCapture;



		public AlsaFloatControl(Type type,
					float fMinimum,
					float fMaximum,
					float fPrecision,
					int nUpdatePeriod,
					float fInitialValue,
					String strUnits,
					String strMinLabel,
					String strMidLabel,
					String strMaxLabel,
					AlsaMixerElement element,
					int nChannel,
					boolean bCapture)
		{
			super(type,
			      fMinimum,
			      fMaximum,
			      fPrecision,
			      nUpdatePeriod,
			      fInitialValue,
			      strUnits,
			      strMinLabel,
			      strMidLabel,
			      strMaxLabel);
			if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.AlsaFloatControl.<init>(): begin"); }
			m_element = element;
			m_nChannel = nChannel;
			m_bCapture = bCapture;
			if (TDebug.TraceMixer) { TDebug.out("AlsaPortMixer.AlsaFloatControl.<init>(): end"); }
		}


		private AlsaMixerElement getElement()
		{
			return m_element;
		}



		private int getChannel()
		{
			return m_nChannel;
		}



		private boolean getCapture()
		{
			return m_bCapture;
		}



		// TODO: respect channels
		public void setValue(float fValue)
		{
			super.setValue(fValue);
			int	nValue = (int) fValue;
			if (getCapture())
			{
				getElement().setCaptureVolumeAll(nValue);
			}
			else
			{
				getElement().setPlaybackVolumeAll(nValue);
			}
		}
	}
}



/*** AlsaPortMixer.java ***/

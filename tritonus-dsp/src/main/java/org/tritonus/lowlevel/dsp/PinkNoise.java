/*
 *	PinkNoise.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2002 by Matthias Pfisterer
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package org.tritonus.lowlevel.dsp;


import java.util.Random;


// for 44.1 kHz only !!!!
/*
[0.05 dB ripple:]

   b0 = 0.99886 * b0 + white * 0.0555179;
   b1 = 0.99332 * b1 + white * 0.0750759;
   b2 = 0.96900 * b2 + white * 0.1538520;
   b3 = 0.86650 * b3 + white * 0.3104856;
   b4 = 0.55000 * b4 + white * 0.5329522;
   b5 = -0.7616 * b5 - white * 0.0168980;
   pink = b0 + b1 + b2 + b3 + b4 + b5 + b6 + white * 0.5362;
   b6 = white * 0.115926;

An 'economy' version with accuracy of +/-0.5dB is also available.

  b0 = 0.99765 * b0 + white * 0.0990460;
  b1 = 0.96300 * b1 + white * 0.2965164;
  b2 = 0.57000 * b2 + white * 1.0526913;
  pink = b0 + b1 + b2 + white * 0.1848;



---
paul.kellett@maxim.abel.co.uk
http://www.abel.co.uk/~maxim/
 */
public class PinkNoise
implements Source
{
	private Source		m_whiteNoiseSource;
	private float		m_b0, m_b1, m_b2, m_b3, m_b4, m_b5, m_b6;



	public PinkNoise(float fSampleRate)
	{
		this(fSampleRate, new WhiteNoise());
	}


	public PinkNoise(float fSampleRate, Random random)
	{
		this(fSampleRate, new WhiteNoise(random));
	}


	private PinkNoise(float fSampleRate, Source whiteNoiseSource)
	{
		// TODO: scale filter for sample rate
		m_whiteNoiseSource = whiteNoiseSource;
	}


	public float process()
	{
		float	fWhite = m_whiteNoiseSource.process();

		m_b0 = 0.99886F * m_b0 + fWhite * 0.0555179F;
		m_b1 = 0.99332F * m_b1 + fWhite * 0.0750759F;
		m_b2 = 0.96900F * m_b2 + fWhite * 0.1538520F;
		m_b3 = 0.86650F * m_b3 + fWhite * 0.3104856F;
		m_b4 = 0.55000F * m_b4 + fWhite * 0.5329522F;
		m_b5 = -0.7616F * m_b5 - fWhite * 0.0168980F;
		float	fPink = m_b0 + m_b1 + m_b2 + m_b3 + m_b4 + m_b5 + m_b6 + fWhite * 0.5362F;
		m_b6 = fWhite * 0.115926F;
		return fPink;
	}
} 



/*** PinkNoise.java ***/

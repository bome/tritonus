/*
 *	Bus.java
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

package org.tritonus.saol.engine;




/**	Bus in the SA engine.
	This interface abstracts the way calculated samples are
	output from the engine. The engine only calls this interface,
	while implementations of this interface write the samples to a
	file, a line, a network socket or whatever else.

	@author Matthias Pfisterer
 */
public class Bus
implements Output
{
	private float[]		m_afValues;



	public Bus(int nWidth)
	{
		m_afValues = new float[nWidth];
	}


	/**	Gives the width of this bus.
		@returns	width of the bus (number of channels)
	 */
	public int getWidth()
	{
		return m_afValues.length;
	}


	/**	Initiate the cumulation of a sample value.
		Sets the values of all samples to 0.0.
		This method must be called in an a-cycle before
		any instrument's a-cycle code is executed.
	*/
	public void clear()
	{
		for (int i = 0; i < getWidth(); i++)
		{
			m_afValues[i] = 0.0F;
		}
	}


	/**	Add the sample value of one instrument.
		This method can be called by instrument's a-cycle
		code to output the sample value the instrument has
		calculated for this a-cycle.
		The current hacky version allows only for mono samples.
	*/
	public void output(float fSample)
	{
		for (int i = 0; i < getWidth(); i++)
		{
			m_afValues[i] += fSample;
		}
	}


	/**	Add sample values of one instrument.
		This method can be called by instrument's a-cycle
		code to output the sample value the instrument has
		calculated for this a-cycle.
		The current hacky version allows only for mono samples.
	*/
	public void output(float[] afSamples)
	{
		for (int i = 0; i < getWidth(); i++)
		{
			m_afValues[i] += afSamples[i];
		}
	}


	public float[] getValues()
	{
		return m_afValues;
	}
}



/*** Bus.java ***/

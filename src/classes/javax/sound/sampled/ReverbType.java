/*
 *	ReverbType.java
 *
 *	This file is part of Tritonus: http://www.tritonus.org/
 */

/*
 *  Copyright (c) 2000 by Matthias Pfisterer
 *
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
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
*/

package javax.sound.sampled;




public class ReverbType
{
	private String		m_strName;
	private int		m_nEarlyReflectionDelay;
	private float		m_fEarlyReflectionIntensity;
	private int		m_nLateReflectionDelay;
	private float		m_fLateReflectionIntensity;
	private int		m_nDecayTime;



	protected ReverbType(String strName,
			     int nEarlyReflectionDelay,
			     float fEarlyReflectionIntensity,
			     int nLateReflectionDelay,
			     float fLateReflectionIntensity,
			     int nDecayTime)
	{
		m_strName = strName;
		m_nEarlyReflectionDelay = nEarlyReflectionDelay;
		m_fEarlyReflectionIntensity = fEarlyReflectionIntensity;
		m_nLateReflectionDelay = nLateReflectionDelay;
		m_fLateReflectionIntensity = fLateReflectionIntensity;
		m_nDecayTime = nDecayTime;
	}



	public String getName()
	{
		return m_strName;
	}



	public int getEarlyReflectionDelay()
	{
		return m_nEarlyReflectionDelay;
	}



	public float getEarlyReflectionIntensity()
	{
		return m_fEarlyReflectionIntensity;
	}



	public int getLateReflectionDelay()
	{
		return m_nLateReflectionDelay;
	}



	public float getLateReflectionIntensity()
	{
		return m_fLateReflectionIntensity;
	}



	public int getDecayTime()
	{
		return m_nDecayTime;
	}



	public final boolean equals(Object obj)
	{
		return super.equals(obj);
	}



	public final int hashCode()
	{
		return super.hashCode();
	}



	public final String toString()
	{
		return super.toString() +
			"[name=" + getName() +
			", earlyReflectionDelay = " + getEarlyReflectionDelay() +
			", earlyReflectionIntensity = " + getEarlyReflectionIntensity() +
			", lateReflectionDelay = " + getLateReflectionDelay() +
			", lateReflectionIntensity = " + getLateReflectionIntensity() +
			", decayTime = " + getDecayTime() + "]";
	}
}



/*** ReverbType.java ***/

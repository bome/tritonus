/*
 *	SAOLGlobals.java
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

package org.tritonus.saol.compiler;




public class SAOLGlobals
{
	private static final int		DEFAULT_ARATE = 32000;
	private static final int		DEFAULT_KRATE = 100;
	private static final int		DEFAULT_INCHANNELS = 0; /*?? TODO: */
	private static final int		DEFAULT_OUTCHANNELS = 1;
	private static final int		DEFAULT_INTERP = 0;

	private int		m_nARate;
	private int		m_nKRate;
	private int		m_nInChannels;
	private int		m_nOutChannels;
	private int		m_nInterp;



	public SAOLGlobals()
	{
		this(DEFAULT_ARATE,
		     DEFAULT_KRATE,
		     DEFAULT_INCHANNELS,
		     DEFAULT_OUTCHANNELS,
		     DEFAULT_INTERP);
	}


	private SAOLGlobals(int nDefaultARate,
						int nDefaultKRate,
						int nDefaultInChannels,
						int nDefaultOutChannels,
						int nDefaultInterp)
	{
		m_nARate = nDefaultARate;
		m_nKRate = nDefaultKRate;
		m_nInChannels = nDefaultInChannels;
		m_nOutChannels = nDefaultOutChannels;
		m_nInterp = nDefaultInterp;
	}



	public void setARate(int nARate)
	{
		m_nARate = nARate;
	}



	public int getARate()
	{
		return m_nARate;
	}



	public void setKRate(int nKRate)
	{
		m_nKRate = nKRate;
	}



	public int getKRate()
	{
		return m_nKRate;
	}



	public void setInChannels(int nInChannels)
	{
		m_nInChannels = nInChannels;
	}



	public int getInChannels()
	{
		return m_nInChannels;
	}



	public void setOutChannels(int nOutChannels)
	{
		m_nOutChannels = nOutChannels;
	}



	public int getOutChannels()
	{
		return m_nOutChannels;
	}



	public void setInterp(int nInterp)
	{
		m_nInterp = nInterp;
	}



	public int getInterp()
	{
		return m_nInterp;
	}
}



/*** SAOLGlobals.java ***/

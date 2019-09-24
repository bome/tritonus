/*
 *	WidthAndRate.java
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



public class WidthAndRate
{
	public static final int		WIDTH_UNKNOWN = -1;
	public static final int		WIDTH_INCHANNELS = -2;
	public static final int		WIDTH_OUTCHANNELS = -3;

	// do not change arbitrarily; InstrumentCompilation depends on it!
	public static final int		RATE_UNKNOWN = 0;
	public static final int		RATE_I = 1;
	public static final int		RATE_K = 2;
	public static final int		RATE_A = 3;
	public static final int		RATE_X = 4;
	// not really rates...
	public static final int		RATE_TABLE = 5;
	public static final int		RATE_OPARRAY = 6;



	private int		m_nWidth;
	private int		m_nRate;



	public WidthAndRate(int nWidth, int nRate)
	{
		m_nWidth = nWidth;
		m_nRate = nRate;
	}



	public int getWidth()
	{
		return m_nWidth;
	}



	public int getRate()
	{
		return m_nRate;
	}
}



/*** WidthAndRate.java ***/

/*
 *	VariableEntry.java
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

import org.tritonus.saol.sablecc.node.AInstrdeclInstrdecl;



public class VariableEntry
extends WidthAndRate
{
	private String			m_strVariableName;
	private boolean			m_bImports;
	private boolean			m_bExports;


	public VariableEntry(String strVariableName,
			     int nWidth,
			     int nRate,
			     boolean bImports,
			     boolean bExports)
	{
		super(nWidth, nRate);
		m_strVariableName = strVariableName;
		m_bImports = bImports;
		m_bExports = bExports;
	}



	public String getVariableName()
	{
		return m_strVariableName;
	}



	public boolean getImports()
	{
		return m_bImports;
	}



	public boolean getExports()
	{
		return m_bExports;
	}
}



/*** VariableEntry.java ***/
